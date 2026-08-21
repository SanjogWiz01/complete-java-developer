package com.jyotibank.model.enums;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    INTEREST("Interest Credit"),
    FEE("Bank Fee");

    private final String displayName;

    TransactionType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static TransactionType fromString(String value) {
        for (TransactionType t : values()) {
            if (t.name().equalsIgnoreCase(value)) return t;
        }
        throw new IllegalArgumentException("Unknown transaction type: " + value);
    }
}
