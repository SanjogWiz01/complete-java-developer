package com.jyotibank.model;

import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SavingsAccount — earns monthly interest, has a minimum balance requirement.
 *
 * Demonstrates: Inheritance + Method Overriding.
 * The @Override annotation makes the compiler verify we are truly overriding
 * a parent method — it prevents silent bugs from typos.
 */
public class SavingsAccount extends Account {

    private static final BigDecimal DEFAULT_MIN_BALANCE  = new BigDecimal("500.00");
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0450"); // 4.5% p.a.

    public SavingsAccount() {
        super();
        setAccountType(AccountType.SAVINGS);
        setMinimumBalance(DEFAULT_MIN_BALANCE);
        setInterestRate(DEFAULT_INTEREST_RATE);
    }

    /**
     * Monthly interest = (balance × annual_rate) / 12
     *
     * RoundingMode.HALF_UP ensures NPR 0.005 rounds to NPR 0.01,
     * which is the standard banking convention (favours the customer on credit).
     */
    @Override
    public BigDecimal calculateMonthlyInterest() {
        if (getBalance().compareTo(BigDecimal.ZERO) <= 0 || getInterestRate() == null) {
            return BigDecimal.ZERO;
        }
        return getBalance()
                .multiply(getInterestRate())
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String getAccountDescription() {
        BigDecimal pct = (getInterestRate() != null)
                ? getInterestRate().multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        return String.format("Savings Account | Interest: %s%% p.a. | Min Balance: NPR %s",
                pct, getMinimumBalance());
    }
}
