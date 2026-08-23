package com.jyotibank.service;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.exception.AccountNotFoundException;
import com.jyotibank.exception.InsufficientBalanceException;
import com.jyotibank.exception.InvalidAmountException;
import com.jyotibank.model.Account;
import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.TransactionType;
import com.jyotibank.util.InputValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

public class TransferService {

    private final AccountService accountService;
    private final AuditService auditService;

    public TransferService(AccountService accountService, AuditService auditService) {
        this.accountService = accountService;
        this.auditService = auditService;
    }

    public String transfer(long actorUserId, String fromAccountNumber, String toAccountNumber,
                           BigDecimal amount, String description) {
        if (!InputValidator.isValidAmount(amount)) {
            throw new InvalidAmountException("Transfer amount must be greater than zero.");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidAmountException("Source and destination accounts cannot be the same.");
        }

        Account from = accountService.getAccountByNumber(fromAccountNumber);
        Account to = accountService.getAccountByNumber(toAccountNumber);
        if (from.getStatus() != AccountStatus.ACTIVE || to.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Both accounts must be active for transfer.");
        }
        if (!from.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException(from.getBalance(), amount);
        }

        String reference = "TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String lockSql = "SELECT account_id, balance, minimum_balance FROM accounts WHERE account_id IN (?, ?) FOR UPDATE";
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        String txSql = """
                INSERT INTO transactions (reference_number, account_id, transaction_type, amount, balance_before, balance_after, description, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'SUCCESS')
                """;

        try (var conn = DatabaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (var lockStmt = conn.prepareStatement(lockSql);
                 var updateStmt = conn.prepareStatement(updateSql);
                 var txStmt = conn.prepareStatement(txSql)) {

                long firstId = Math.min(from.getAccountId(), to.getAccountId());
                long secondId = Math.max(from.getAccountId(), to.getAccountId());
                lockStmt.setLong(1, firstId);
                lockStmt.setLong(2, secondId);
                lockStmt.executeQuery();

                BigDecimal fromBefore = from.getBalance();
                BigDecimal toBefore = to.getBalance();
                BigDecimal fromAfter = fromBefore.subtract(amount);
                BigDecimal toAfter = toBefore.add(amount);

                if (fromAfter.compareTo(from.getMinimumBalance()) < 0) {
                    throw new InsufficientBalanceException(fromBefore, amount);
                }

                updateStmt.setBigDecimal(1, fromAfter);
                updateStmt.setLong(2, from.getAccountId());
                updateStmt.executeUpdate();

                updateStmt.setBigDecimal(1, toAfter);
                updateStmt.setLong(2, to.getAccountId());
                updateStmt.executeUpdate();

                insertTransferTransaction(txStmt, reference, from.getAccountId(), TransactionType.TRANSFER,
                        amount, fromBefore, fromAfter, description == null ? "Transfer sent to " + toAccountNumber : description);
                insertTransferTransaction(txStmt, reference, to.getAccountId(), TransactionType.TRANSFER,
                        amount, toBefore, toAfter, description == null ? "Transfer received from " + fromAccountNumber : description);

                conn.commit();
                auditService.record(actorUserId, "TRANSFER", "ACCOUNT", from.getAccountId(),
                        reference + ": " + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
                return reference;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (AccountNotFoundException | InsufficientBalanceException | InvalidAmountException e) {
            throw e;
        } catch (SQLException e) {
            throw new IllegalStateException("Transfer failed due to database error: " + e.getMessage(), e);
        }
    }

    private void insertTransferTransaction(
            java.sql.PreparedStatement txStmt,
            String reference,
            long accountId,
            TransactionType type,
            BigDecimal amount,
            BigDecimal before,
            BigDecimal after,
            String description
    ) throws SQLException {
        txStmt.setString(1, reference);
        txStmt.setLong(2, accountId);
        txStmt.setString(3, type.name());
        txStmt.setBigDecimal(4, amount);
        txStmt.setBigDecimal(5, before);
        txStmt.setBigDecimal(6, after);
        txStmt.setString(7, description);
        txStmt.executeUpdate();
    }
}
