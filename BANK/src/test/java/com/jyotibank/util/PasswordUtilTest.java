package com.jyotibank.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void hashAndVerifyPasswordWorks() {
        String plain = "SuperSecure#123";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotEquals(plain, hash);
        assertTrue(PasswordUtil.verifyPassword(plain, hash));
        assertFalse(PasswordUtil.verifyPassword("wrong", hash));
    }
}
