package com.jyotibank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfer — records a fund transfer between two accounts.
 *
 * A transfer always produces TWO Transaction rows (one debit, one credit)
 * sharing the same referenceNumber. This class ties them together and records
 * the overall transfer outcome.
 */
public class Transfer {

    private long transferId;
    private String referenceNumber;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private String status;               // SUCCESS | FAILED | ROLLED_BACK
    private LocalDateTime initiatedAt;

    // Populated by JOIN queries for display purposes — not stored in transfers table
    private String fromAccountNumber;
    private String toAccountNumber;

    public Transfer() {}

    public Transfer(String referenceNumber, long fromAccountId, long toAccountId, BigDecimal amount) {
        this.referenceNumber = referenceNumber;
        this.fromAccountId   = fromAccountId;
        this.toAccountId     = toAccountId;
        this.amount          = amount;
        this.status          = "SUCCESS";
        this.initiatedAt     = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public long getTransferId() { return transferId; }
    public void setTransferId(long transferId) { this.transferId = transferId; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(long fromAccountId) { this.fromAccountId = fromAccountId; }

    public long getToAccountId() { return toAccountId; }
    public void setToAccountId(long toAccountId) { this.toAccountId = toAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public void setInitiatedAt(LocalDateTime initiatedAt) { this.initiatedAt = initiatedAt; }

    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }

    @Override
    public String toString() {
        return String.format("Transfer{ref='%s', from=%s, to=%s, amount=%s, status=%s}",
                referenceNumber, fromAccountNumber != null ? fromAccountNumber : fromAccountId,
                toAccountNumber != null ? toAccountNumber : toAccountId, amount, status);
    }
}
