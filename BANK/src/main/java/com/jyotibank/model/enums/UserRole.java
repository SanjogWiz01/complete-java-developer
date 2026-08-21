package com.jyotibank.model.enums;

public enum UserRole {
    ADMIN("Administrator"),
    CUSTOMER("Customer");

    private final String displayName;

    UserRole(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static UserRole fromString(String value) {
        for (UserRole r : values()) {
            if (r.name().equalsIgnoreCase(value)) return r;
        }
        throw new IllegalArgumentException("Unknown user role: " + value);
    }
}
