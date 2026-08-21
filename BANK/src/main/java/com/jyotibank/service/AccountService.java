package com.jyotibank.service;

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
import java.util.List;
import java.util.UUID;

public class AccountService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public AccountService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public Account openSavingsAccount(long customerId, BigDecimal openingDeposit) {
        SavingsAccount account = new SavingsAccount();
        return openAccount(account, customerId, openingDeposit);
    }

    public Account openCurrentAccount(long customerId, BigDecimal openingDeposit) {
        CurrentAccount account = new CurrentAccount();
        return openAccount(account, customerId, openingDeposit);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public List<Account> getAccountsByCustomer(long customerId) {
        return accountDao.findByCustomerId(customerId);
    }

    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        validateAmount(amount);
        Account account = getAccountByNumber(accountNumber);
        if (!account.isActive()) throw new IllegalStateException("Account is not active.");

        BigDecimal before = account.getBalance();
        BigDecimal after = before.add(amount);
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
        return tx;
    }

    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        validateAmount(amount);
        Account account = getAccountByNumber(accountNumber);
        if (!account.isActive()) throw new IllegalStateException("Account is not active.");
        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }

        BigDecimal before = account.getBalance();
        BigDecimal after = before.subtract(amount);
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
        return tx;
    }

    public void blockAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        accountDao.updateStatus(account.getAccountId(), AccountStatus.BLOCKED);
    }

    private Account openAccount(Account account, long customerId, BigDecimal openingDeposit) {
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
