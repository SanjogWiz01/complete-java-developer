package com.jyotibank.service;

import com.jyotibank.config.AppConfig;
import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.TransactionDao;
import com.jyotibank.exception.AccountNotFoundException;
import com.jyotibank.exception.InsufficientBalanceException;
import com.jyotibank.exception.InvalidAmountException;
import com.jyotibank.model.Account;
import com.jyotibank.model.CurrentAccount;
import com.jyotibank.model.SavingsAccount;
import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.AccountType;
import com.jyotibank.model.enums.TransactionType;
import com.jyotibank.util.AccountNumberGenerator;
import com.jyotibank.util.InputValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AccountService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    private final AuditService auditService;

    public AccountService(AccountDao accountDao, TransactionDao transactionDao, AuditService auditService) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
        this.auditService = auditService;
    }

    // ── Opening ──────────────────────────────────────────────────────────

    public Account openSavingsAccount(long actorUserId, long customerId, BigDecimal openingDeposit) {
        SavingsAccount account = new SavingsAccount();
        return openAccount(actorUserId, account, customerId, openingDeposit);
    }

    public Account openCurrentAccount(long actorUserId, long customerId, BigDecimal openingDeposit) {
        CurrentAccount account = new CurrentAccount();
        return openAccount(actorUserId, account, customerId, openingDeposit);
    }

    // ── Queries ──────────────────────────────────────────────────────────

    public Account getAccountByNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public List<Account> getAccountsByCustomer(long customerId) {
        return accountDao.findByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    // ── Money movement ───────────────────────────────────────────────────

    public Transaction deposit(long actorUserId, String accountNumber, BigDecimal amount, String description) {
        validateAmount(amount);
        Account account = getAccountByNumber(accountNumber);
        requireActive(account);

        BigDecimal before = account.getBalance();
        BigDecimal after = before.add(amount).setScale(2, java.math.RoundingMode.HALF_UP);
        accountDao.updateBalance(account.getAccountId(), after);
        account.setBalance(after);

        Transaction tx = new Transaction(
                generateReference("DEP"),
                account.getAccountId(),
                TransactionType.DEPOSIT,
                amount,
                before,
                after,
                description == null ? "Cash deposit" : description
        );
        transactionDao.create(tx);
        auditService.record(actorUserId, "DEPOSIT", "ACCOUNT", account.getAccountId(),
                amount + " credited to " + accountNumber);
        return tx;
    }

    public Transaction withdraw(long actorUserId, String accountNumber, BigDecimal amount, String description) {
        validateAmount(amount);
        Account account = getAccountByNumber(accountNumber);
        requireActive(account);

        enforceDailyWithdrawalLimit(account, amount);

        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }

        BigDecimal before = account.getBalance();
        BigDecimal after = before.subtract(amount).setScale(2, java.math.RoundingMode.HALF_UP);
        accountDao.updateBalance(account.getAccountId(), after);
        account.setBalance(after);

        Transaction tx = new Transaction(
                generateReference("WDR"),
                account.getAccountId(),
                TransactionType.WITHDRAWAL,
                amount,
                before,
                after,
                description == null ? "Cash withdrawal" : description
        );
        transactionDao.create(tx);
        auditService.record(actorUserId, "WITHDRAWAL", "ACCOUNT", account.getAccountId(),
                amount + " debited from " + accountNumber);
        return tx;
    }

    // ── Lifecycle ────────────────────────────────────────────────────────

    public void blockAccount(long actorUserId, String accountNumber) {
        changeStatus(actorUserId, accountNumber, AccountStatus.BLOCKED, "ACCOUNT_BLOCK");
    }

    public void unblockAccount(long actorUserId, String accountNumber) {
        changeStatus(actorUserId, accountNumber, AccountStatus.ACTIVE, "ACCOUNT_UNBLOCK");
    }

    /** Closing keeps history intact but freezes the account forever. */
    public void closeAccount(long actorUserId, String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAmountException("Account balance must be zero before closing (current: "
                    + account.getBalance() + ").");
        }
        changeStatus(actorUserId, accountNumber, AccountStatus.CLOSED, "ACCOUNT_CLOSE");
    }

    // ── Internals ────────────────────────────────────────────────────────

    private void enforceDailyWithdrawalLimit(Account account, BigDecimal amount) {
        BigDecimal dailyLimit = AppConfig.getInstance()
                .getDecimalProperty("transaction.daily.withdrawal.limit", new BigDecimal("100000.00"));
        BigDecimal withdrawnToday = transactionDao.sumAmountByTypeOnDate(
                account.getAccountId(), TransactionType.WITHDRAWAL, LocalDate.now());
        if (withdrawnToday == null) withdrawnToday = BigDecimal.ZERO;
        if (withdrawnToday.add(amount).compareTo(dailyLimit) > 0) {
            throw new InvalidAmountException("Daily withdrawal limit of " + dailyLimit
                    + " exceeded (already withdrawn today: " + withdrawnToday + ").");
        }
    }

    private void requireActive(Account account) {
        if (!account.isActive()) throw new IllegalStateException("Account is not active.");
    }

    private void changeStatus(long actorUserId, String accountNumber, AccountStatus status, String auditAction) {
        Account account = getAccountByNumber(accountNumber);
        accountDao.updateStatus(account.getAccountId(), status);
        auditService.record(actorUserId, auditAction, "ACCOUNT", account.getAccountId(),
                accountNumber + " → " + status);
    }

    private Account openAccount(long actorUserId, Account account, long customerId, BigDecimal openingDeposit) {
        validateAmount(openingDeposit);
        if (openingDeposit.compareTo(account.getMinimumBalance()) < 0) {
            throw new InvalidAmountException("Opening deposit is below minimum balance requirement.");
        }
        account.setCustomerId(customerId);
        account.setAccountNumber(AccountNumberGenerator.generate(account.getAccountType()));
        account.setBalance(openingDeposit);
        long accountId = accountDao.create(account);
        account.setAccountId(accountId);

        Transaction openingTransaction = new Transaction(
                generateReference("OPN"),
                accountId,
                TransactionType.DEPOSIT,
                openingDeposit,
                BigDecimal.ZERO,
                openingDeposit,
                "Opening deposit for " + readableAccountType(account.getAccountType())
        );
        transactionDao.create(openingTransaction);
        auditService.record(actorUserId, "ACCOUNT_OPEN", "ACCOUNT", accountId,
                account.getAccountType() + " " + account.getAccountNumber() + " opened with " + openingDeposit);
        return account;
    }

    private void validateAmount(BigDecimal amount) {
        if (!InputValidator.isValidAmount(amount)) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }

    private String readableAccountType(AccountType type) {
        return switch (type) {
            case SAVINGS -> "Savings Account";
            case CURRENT -> "Current Account";
            case FIXED_DEPOSIT -> "Fixed Deposit Account";
        };
    }

    private String generateReference(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
