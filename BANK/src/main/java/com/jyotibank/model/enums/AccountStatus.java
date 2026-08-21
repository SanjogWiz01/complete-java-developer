package com.jyotibank.model.enums;

public enum AccountStatus {
    ACTIVE("Active"),
    BLOCKED("Blocked"),
    CLOSED("Closed"),
    DORMANT("Dormant");

    private final String displayName;

    AccountStatus(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static AccountStatus fromString(String value) {
        for (AccountStatus s : values()) {
            if (s.name().equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown account status: " + value);
    }
}
