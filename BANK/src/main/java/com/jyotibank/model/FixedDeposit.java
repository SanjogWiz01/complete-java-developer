package com.jyotibank.model;

import com.jyotibank.model.enums.FDStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FixedDeposit — stores FD-specific details (principal, tenure, maturity).
 *
 * Interest formula used: Simple Interest
 *   A = P × (1 + r × t)
 * where:
 *   P = principal
 *   r = annual rate (e.g., 0.08 for 8%)
 *   t = tenure in years (tenure_months / 12)
 *
 * Simple interest is used here for clarity and is common in Nepali banking for short tenures.
 * Compound interest variant is demonstrated in calculateCompoundMaturity().
 */
public class FixedDeposit {

    private long fdId;
    private long linkedAccountId;   // Source savings/current account (for debit on creation)
    private long fdAccountId;       // The actual FD account in accounts table
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private int tenureMonths;
    private BigDecimal maturityAmount;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private FDStatus status;
    private LocalDateTime createdAt;

    public FixedDeposit() {}

    public FixedDeposit(long linkedAccountId, BigDecimal principalAmount,
                        BigDecimal interestRate, int tenureMonths) {
        this.linkedAccountId  = linkedAccountId;
        this.principalAmount  = principalAmount;
        this.interestRate     = interestRate;
        this.tenureMonths     = tenureMonths;
        this.startDate        = LocalDate.now();
        this.maturityDate     = startDate.plusMonths(tenureMonths);
        this.maturityAmount   = calculateSimpleMaturity();
        this.status           = FDStatus.ACTIVE;
    }

    /** Simple interest maturity: A = P(1 + r*t) */
    public BigDecimal calculateSimpleMaturity() {
        if (principalAmount == null || interestRate == null) return BigDecimal.ZERO;
        BigDecimal t = BigDecimal.valueOf(tenureMonths)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        return principalAmount
                .multiply(BigDecimal.ONE.add(interestRate.multiply(t)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Compound interest maturity: A = P(1 + r/n)^(n*t)
     * n=1 (compounded annually), t = tenureMonths / 12.
     * Kept here for educational comparison.
     */
    public BigDecimal calculateCompoundMaturity() {
        if (principalAmount == null || interestRate == null) return BigDecimal.ZERO;
        double p = principalAmount.doubleValue();
        double r = interestRate.doubleValue();
        double t = tenureMonths / 12.0;
        double a = p * Math.pow(1 + r, t);
        return BigDecimal.valueOf(a).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalInterestEarned() {
        if (maturityAmount == null || principalAmount == null) return BigDecimal.ZERO;
        return maturityAmount.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
    }

    /** On early withdrawal: return principal minus penalty. No interest paid. */
    public BigDecimal calculateEarlyWithdrawalAmount(BigDecimal penaltyRate) {
        BigDecimal penalty = principalAmount.multiply(penaltyRate).setScale(2, RoundingMode.HALF_UP);
        return principalAmount.subtract(penalty);
    }

    public boolean isMatured() {
        return !LocalDate.now().isBefore(maturityDate);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public long getFdId() { return fdId; }
    public void setFdId(long fdId) { this.fdId = fdId; }

    public long getLinkedAccountId() { return linkedAccountId; }
    public void setLinkedAccountId(long linkedAccountId) { this.linkedAccountId = linkedAccountId; }

    public long getFdAccountId() { return fdAccountId; }
    public void setFdAccountId(long fdAccountId) { this.fdAccountId = fdAccountId; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

    public FDStatus getStatus() { return status; }
    public void setStatus(FDStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format(
                "FixedDeposit{id=%d, principal=%s, rate=%s, tenure=%d months, maturity=%s, status=%s}",
                fdId, principalAmount, interestRate, tenureMonths, maturityDate, status);
    }
}
