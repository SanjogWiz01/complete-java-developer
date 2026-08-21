package com.jyotibank.model.enums;

public enum FDStatus {
    ACTIVE("Active"),
    MATURED("Matured"),
    CLOSED("Closed"),
    BROKEN("Broken - Early Withdrawal");

    private final String displayName;

    FDStatus(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static FDStatus fromString(String value) {
        for (FDStatus s : values()) {
            if (s.name().equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown FD status: " + value);
    }
}
