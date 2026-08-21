package com.jyotibank.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsAccountTest {

    @Test
    void calculateMonthlyInterestUsesConfiguredRate() {
        SavingsAccount account = new SavingsAccount();
        account.setBalance(new BigDecimal("12000.00"));
        BigDecimal monthlyInterest = account.calculateMonthlyInterest();

        assertEquals(new BigDecimal("45.00"), monthlyInterest);
    }
}
