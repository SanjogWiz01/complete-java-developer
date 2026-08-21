package com.jyotibank.util;

import com.jyotibank.model.enums.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountNumberGeneratorTest {

    @Test
    void generatedNumberContainsTypePrefix() {
        String accountNumber = AccountNumberGenerator.generate(AccountType.SAVINGS);
        assertTrue(accountNumber.startsWith("SAV"));
        assertTrue(accountNumber.length() >= 17);
    }
}
