package com.jyotibank.model;

import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;

/**
 * FixedDepositAccount — the account entry in the accounts table for an FD.
 * FD-specific details (tenure, maturity date, etc.) are in {@link FixedDeposit}.
 */
public class FixedDepositAccount extends Account {

    public FixedDepositAccount() {
        super();
        setAccountType(AccountType.FIXED_DEPOSIT);
        setMinimumBalance(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal calculateMonthlyInterest() {
        // FD interest is computed at maturity, not monthly — see FixedDeposit.calculateMaturityAmount()
        return BigDecimal.ZERO;
    }

    @Override
    public String getAccountDescription() {
        return "Fixed Deposit Account — interest accrued at maturity.";
    }
}
