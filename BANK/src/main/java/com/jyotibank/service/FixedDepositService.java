package com.jyotibank.service;

import com.jyotibank.config.AppConfig;
import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.FixedDepositDao;
import com.jyotibank.dao.TransactionDao;
import com.jyotibank.exception.AccountNotFoundException;
import com.jyotibank.exception.InsufficientBalanceException;
import com.jyotibank.exception.InvalidAmountException;
import com.jyotibank.model.Account;
import com.jyotibank.model.FixedDeposit;
import com.jyotibank.model.FixedDepositAccount;
import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.FDStatus;
import com.jyotibank.model.enums.TransactionType;
import com.jyotibank.util.AccountNumberGenerator;
import com.jyotibank.util.InputValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * FixedDepositService — full FD lifecycle: open, break early, settle at maturity.
 *
 * <p>Money movement is wrapped in a single JDBC transaction (same ACID pattern
 * as {@link TransferService}): either the principal leaves the linked savings
 * account AND the FD record exists, or neither happens.
 */
public class FixedDepositService {

    private final AccountDao accountDao;
    private final FixedDepositDao fixedDepositDao;
    private final TransactionDao transactionDao;
    private final AuditService auditService;

    public FixedDepositService(AccountDao accountDao, FixedDepositDao fixedDepositDao,
                               TransactionDao transactionDao, AuditService auditService) {
        this.accountDao = accountDao;
        this.fixedDepositDao = fixedDepositDao;
        this.transactionDao = transactionDao;
        this.auditService = auditService;
    }

    /** Result of an early break — what the customer actually receives. */
    public record FdBreakResult(BigDecimal principal, BigDecimal penalty, BigDecimal payout) {}

    // ── Open ─────────────────────────────────────────────────────────────

    public FixedDeposit open(long actorUserId, long linkedAccountId, BigDecimal amount,
                             BigDecimal annualRate, int tenureMonths) {
        BigDecimal minAmount = AppConfig.getInstance().getDecimalProperty("fd.min.amount", new BigDecimal("1000.00"));
        if (!InputValidator.isValidAmount(amount)) {
            throw new InvalidAmountException("FD amount must be greater than zero.");
        }
        if (amount.compareTo(minAmount) < 0) {
            throw new InvalidAmountException("FD amount is below the minimum of " + minAmount);
        }
        if (tenureMonths <= 0) {
            throw new InvalidAmountException("FD tenure must be at least one month.");
        }

        String reference = "FDO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String lockSql = "SELECT balance, minimum_balance FROM accounts WHERE account_id = ? FOR UPDATE";
        String debitSql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        String txSql = """
                INSERT INTO transactions (reference_number, account_id, transaction_type, amount, balance_before, balance_after, description, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'SUCCESS')
                """;

        try (var conn = DatabaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account linked = lockSingle(conn, lockSql, linkedAccountId);
                if (linked.getStatus() != AccountStatus.ACTIVE) {
                    throw new IllegalStateException("Linked account must be active.");
                }
                if (!linked.hasSufficientBalance(amount)) {
                    throw new InsufficientBalanceException(linked.getBalance(), amount);
                }

                // 1. Debit the linked account
                BigDecimal before = linked.getBalance();
                BigDecimal after = before.subtract(amount);
                try (var ps = conn.prepareStatement(debitSql)) {
                    ps.setBigDecimal(1, after);
                    ps.setLong(2, linkedAccountId);
                    ps.executeUpdate();
                }
                insertLedger(conn, txSql, reference, linkedAccountId, TransactionType.WITHDRAWAL,
                        amount, before, after, "Fixed deposit opened");

                // 2. Create the FD holding account
                FixedDepositAccount fdAccount = new FixedDepositAccount();
                fdAccount.setCustomerId(customerIdFor(linkedAccountId));
                fdAccount.setAccountNumber(AccountNumberGenerator.generate(fdAccount.getAccountType()));
                fdAccount.setBalance(amount);
                long fdAccountId = insertAccount(conn, fdAccount);

                // 3. Record the FD contract
                FixedDeposit fd = new FixedDeposit(linkedAccountId, amount, annualRate, tenureMonths);
                fd.setFdAccountId(fdAccountId);

                long fdId = insertFixedDeposit(conn, fd);
                fd.setFdId(fdId);

                // 4. Opening ledger row on the FD account itself
                insertLedger(conn, txSql, reference, fdAccountId, TransactionType.DEPOSIT,
                        amount, BigDecimal.ZERO, amount, "FD principal placed");

                conn.commit();

                auditService.record(actorUserId, "FD_OPEN", "FIXED_DEPOSIT", fdId,
                        "Principal=" + amount + ", tenure=" + tenureMonths + "m, rate=" + annualRate);
                return fd;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (InsufficientBalanceException | InvalidAmountException | AccountNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw new IllegalStateException("FD opening failed due to database error: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("FD opening failed: " + e.getMessage(), e);
        }
    }

    // ── Break early (penalty applies, no interest paid) ──────────────────

    public FdBreakResult breakEarly(long actorUserId, long fdId) {
        FixedDeposit fd = getFd(fdId);
        requireActive(fd);
        BigDecimal penaltyRate = AppConfig.getInstance()
                .getDecimalProperty("fd.early.withdrawal.penalty.rate", new BigDecimal("0.0200"));
        BigDecimal payout = fd.calculateEarlyWithdrawalAmount(penaltyRate);
        BigDecimal penalty = fd.getPrincipalAmount().subtract(payout);

        payOut(actorUserId, fd, payout, "FD broken early (penalty " + penalty + ")", FDStatus.BROKEN, "FD_BREAK");
        return new FdBreakResult(fd.getPrincipalAmount(), penalty, payout);
    }

    // ── Settle at maturity (full maturity amount credited) ───────────────

    public FdBreakResult settleAtMaturity(long actorUserId, long fdId) {
        FixedDeposit fd = getFd(fdId);
        requireActive(fd);
        if (!fd.isMatured()) {
            throw new InvalidAmountException("FD is not mature yet (matures on " + fd.getMaturityDate() + ").");
        }
        BigDecimal payout = fd.getMaturityAmount();

        payOut(actorUserId, fd, payout, "FD matured — settlement credited", FDStatus.MATURED, "FD_MATURE");
        return new FdBreakResult(fd.getPrincipalAmount(), BigDecimal.ZERO, payout);
    }

    // ── Queries ──────────────────────────────────────────────────────────

    public FixedDeposit getFd(long fdId) {
        return fixedDepositDao.findById(fdId)
                .orElseThrow(() -> new AccountNotFoundException("Fixed deposit not found: " + fdId));
    }

    public List<FixedDeposit> getByLinkedAccount(long linkedAccountId) {
        return fixedDepositDao.findByLinkedAccountId(linkedAccountId);
    }

    // ── Internal helpers ─────────────────────────────────────────────────

    /**
     * Shared payout path for break & maturity: close the FD holding account,
     * credit the payout to the linked account, write both ledger rows and
     * update the FD status — atomically.
     */
    private void payOut(long actorUserId, FixedDeposit fd, BigDecimal payout,
                        String description, FDStatus newStatus, String auditAction) {
        String reference = auditAction + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String balanceSql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        String statusSql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        String txSql = """
                INSERT INTO transactions (reference_number, account_id, transaction_type, amount, balance_before, balance_after, description, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'SUCCESS')
                """;

        try (var conn = DatabaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                String lockOneSql = "SELECT balance, minimum_balance FROM accounts WHERE account_id = ? FOR UPDATE";
                Account fdAccount = lockSingle(conn, lockOneSql, fd.getFdAccountId());
                Account linkedFresh = lockSingle(conn, lockOneSql, fd.getLinkedAccountId());

                // Zero out the FD holding account
                BigDecimal fdBalance = fdAccount.getBalance();
                try (var ps = conn.prepareStatement(balanceSql)) {
                    ps.setBigDecimal(1, BigDecimal.ZERO.setScale(2));
                    ps.setLong(2, fd.getFdAccountId());
                    ps.executeUpdate();
                }
                try (var ps = conn.prepareStatement(statusSql)) {
                    ps.setString(1, AccountStatus.CLOSED.name());
                    ps.setLong(2, fd.getFdAccountId());
                    ps.executeUpdate();
                }
                insertLedger(conn, txSql, reference, fd.getFdAccountId(), TransactionType.WITHDRAWAL,
                        fdBalance, fdBalance, BigDecimal.ZERO, description);

                // Credit payout to the linked account
                BigDecimal before = linkedFresh.getBalance();
                BigDecimal after = before.add(payout).setScale(2, java.math.RoundingMode.HALF_UP);
                try (var ps = conn.prepareStatement(balanceSql)) {
                    ps.setBigDecimal(1, after);
                    ps.setLong(2, fd.getLinkedAccountId());
                    ps.executeUpdate();
                }
                insertLedger(conn, txSql, reference, fd.getLinkedAccountId(), TransactionType.DEPOSIT,
                        payout, before, after, description);

                // Update FD contract status
                fd.setStatus(newStatus);
                updateFixedDepositStatus(conn, fd);

                conn.commit();
                auditService.record(actorUserId, auditAction, "FIXED_DEPOSIT", fd.getFdId(),
                        "Payout=" + payout + " credited to account " + fd.getLinkedAccountId());
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (InvalidAmountException | AccountNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw new IllegalStateException(auditAction + " failed due to database error: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(auditAction + " failed: " + e.getMessage(), e);
        }
    }

    private void requireActive(FixedDeposit fd) {
        if (fd.getStatus() != FDStatus.ACTIVE) {
            throw new InvalidAmountException("Fixed deposit is already " + fd.getStatus() + ".");
        }
    }

    private Account lockSingle(java.sql.Connection conn, String sql, long id) throws SQLException {
        try (var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) throw new AccountNotFoundException("Account not found: " + id);
                BigDecimal balance = rs.getBigDecimal("balance");
                Account a = new com.jyotibank.model.SavingsAccount(); // lightweight holder for balance/min-balance
                a.setAccountId(id);
                a.setBalance(balance);
                a.setMinimumBalance(rs.getBigDecimal("minimum_balance"));
                return a;
            }
        }
    }

    private long customerIdFor(long accountId) {
        return accountDao.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId))
                .getCustomerId();
    }

    private long insertAccount(java.sql.Connection conn, Account account) throws SQLException {
        String sql = """
                INSERT INTO accounts (account_number, customer_id, account_type, balance, status, interest_rate, minimum_balance)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (var ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getAccountNumber());
            ps.setLong(2, account.getCustomerId());
            ps.setString(3, account.getAccountType().name());
            ps.setBigDecimal(4, account.getBalance());
            ps.setString(5, account.getStatus().name());
            ps.setBigDecimal(6, account.getInterestRate());
            ps.setBigDecimal(7, account.getMinimumBalance());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new SQLException("No generated key for FD account.");
        }
    }

    private long insertFixedDeposit(java.sql.Connection conn, FixedDeposit fd) throws SQLException {
        String sql = """
                INSERT INTO fixed_deposits (
                  linked_account_id, fd_account_id, principal_amount, interest_rate, tenure_months,
                  maturity_amount, start_date, maturity_date, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, fd.getLinkedAccountId());
            ps.setLong(2, fd.getFdAccountId());
            ps.setBigDecimal(3, fd.getPrincipalAmount());
            ps.setBigDecimal(4, fd.getInterestRate());
            ps.setInt(5, fd.getTenureMonths());
            ps.setBigDecimal(6, fd.getMaturityAmount());
            ps.setDate(7, java.sql.Date.valueOf(fd.getStartDate()));
            ps.setDate(8, java.sql.Date.valueOf(fd.getMaturityDate()));
            ps.setString(9, fd.getStatus().name());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new SQLException("No generated key for fixed deposit.");
        }
    }

    private void updateFixedDepositStatus(java.sql.Connection conn, FixedDeposit fd) throws SQLException {
        String sql = "UPDATE fixed_deposits SET status = ? WHERE fd_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, fd.getStatus().name());
            ps.setLong(2, fd.getFdId());
            ps.executeUpdate();
        }
    }

    private void insertLedger(java.sql.Connection conn, String txSql, String reference, long accountId,
                              TransactionType type, BigDecimal amount, BigDecimal before,
                              BigDecimal after, String description) throws SQLException {
        try (var ps = conn.prepareStatement(txSql)) {
            ps.setString(1, reference);
            ps.setLong(2, accountId);
            ps.setString(3, type.name());
            ps.setBigDecimal(4, amount);
            ps.setBigDecimal(5, before);
            ps.setBigDecimal(6, after);
            ps.setString(7, description);
            ps.executeUpdate();
        }
    }
}
