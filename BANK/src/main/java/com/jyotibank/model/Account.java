package com.jyotibank.model;

import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Account — abstract base class for all account types.
 *
 * OOP concepts demonstrated:
 * - Abstraction: abstract class with abstract methods that force subclasses
 *   to provide their own interest calculation and description.
 * - Inheritance: SavingsAccount, CurrentAccount, FixedDepositAccount extend this.
 * - Polymorphism: AccountService works with Account references, calling
 *   calculateMonthlyInterest() without knowing the concrete type at compile time.
 * - Template Method pattern: hasSufficientBalance() uses the subclass's minimumBalance
 *   without knowing which subclass is active.
 *
 * Why BigDecimal instead of double?
 * Binary floating-point (double) cannot represent 0.1 exactly.
 * 0.1 + 0.2 in double == 0.30000000000000004.
 * For banking, every paisa must be exact. DECIMAL(15,2) in MySQL maps to BigDecimal in Java.
 */
public abstract class Account {

    private long accountId;
    private String accountNumber;
    private long customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    private LocalDateTime openedAt;
    private LocalDateTime updatedAt;

    protected Account() {
        this.balance       = BigDecimal.ZERO;
        this.status        = AccountStatus.ACTIVE;
        this.minimumBalance = BigDecimal.ZERO;
    }

    // ── Abstract contract ─────────────────────────────────────────────────

    /** Calculates one month's interest on the current balance. */
    public abstract BigDecimal calculateMonthlyInterest();

    /** Returns a human-readable description of this account type. */
    public abstract String getAccountDescription();

    // ── Template method ───────────────────────────────────────────────────

    /**
     * Returns true if withdrawing the given amount leaves the balance
     * at or above the minimum required balance for this account type.
     */
    public boolean hasSufficientBalance(BigDecimal amount) {
        BigDecimal afterWithdrawal = balance.subtract(amount);
        BigDecimal minBal = (minimumBalance != null) ? minimumBalance : BigDecimal.ZERO;
        return afterWithdrawal.compareTo(minBal) >= 0;
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(status);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(BigDecimal minimumBalance) { this.minimumBalance = minimumBalance; }

    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Account{number='%s', type=%s, balance=%s, status=%s}",
                accountNumber, accountType, balance, status);
    }
}
