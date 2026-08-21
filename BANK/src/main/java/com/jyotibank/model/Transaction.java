package com.jyotibank.model;

import com.jyotibank.model.enums.TransactionStatus;
import com.jyotibank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction — immutable record of a single ledger entry.
 *
 * Stores balance_before and balance_after so the audit trail is complete
 * even if the account balance is later corrected. A bank's books must
 * always show exactly what happened and when.
 */
public class Transaction {

    private long transactionId;
    private String referenceNumber;
    private long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(String referenceNumber, long accountId, TransactionType type,
                       BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter,
                       String description) {
        this.referenceNumber = referenceNumber;
        this.accountId       = accountId;
        this.transactionType = type;
        this.amount          = amount;
        this.balanceBefore   = balanceBefore;
        this.balanceAfter    = balanceAfter;
        this.description     = description;
        this.status          = TransactionStatus.SUCCESS;
        this.createdAt       = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(BigDecimal balanceBefore) { this.balanceBefore = balanceBefore; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Transaction{ref='%s', type=%s, amount=%s, status=%s, at=%s}",
                referenceNumber, transactionType, amount, status, createdAt);
    }
}
