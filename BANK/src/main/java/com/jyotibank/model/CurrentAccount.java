package com.jyotibank.model;

import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CurrentAccount — designed for high transaction volume (businesses).
 * Lower interest rate; higher minimum balance.
 */
public class CurrentAccount extends Account {

    private static final BigDecimal DEFAULT_MIN_BALANCE  = new BigDecimal("1000.00");
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0200"); // 2% p.a.

    public CurrentAccount() {
        super();
        setAccountType(AccountType.CURRENT);
        setMinimumBalance(DEFAULT_MIN_BALANCE);
        setInterestRate(DEFAULT_INTEREST_RATE);
    }

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
        return String.format("Current Account | Interest: %s%% p.a. | Min Balance: NPR %s",
                pct, getMinimumBalance());
    }
}
