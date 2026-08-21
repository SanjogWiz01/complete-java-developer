package com.jyotibank.model.enums;

public enum TransactionStatus {
    SUCCESS("Success"),
    FAILED("Failed"),
    PENDING("Pending");

    private final String displayName;

    TransactionStatus(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static TransactionStatus fromString(String value) {
        for (TransactionStatus s : values()) {
            if (s.name().equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown transaction status: " + value);
    }
}
