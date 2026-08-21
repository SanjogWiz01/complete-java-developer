package com.jyotibank.util;

import com.jyotibank.config.AppConfig;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {}

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
        int rounds = AppConfig.getInstance().getIntProperty("app.bcrypt.rounds", 12);
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(rounds));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isBlank()) return false;
        if (hashedPassword == null || hashedPassword.isBlank()) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
