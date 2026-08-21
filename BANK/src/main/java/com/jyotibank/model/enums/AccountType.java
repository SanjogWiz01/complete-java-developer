package com.jyotibank.model.enums;

public enum AccountType {
    SAVINGS("Savings Account"),
    CURRENT("Current Account"),
    FIXED_DEPOSIT("Fixed Deposit Account");

    private final String displayName;

    AccountType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static AccountType fromString(String value) {
        for (AccountType t : values()) {
            if (t.name().equalsIgnoreCase(value)) return t;
        }
        throw new IllegalArgumentException("Unknown account type: " + value);
    }
}
