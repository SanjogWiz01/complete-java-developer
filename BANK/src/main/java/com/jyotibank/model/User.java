package com.jyotibank.model;

import com.jyotibank.model.enums.UserRole;

import java.time.LocalDateTime;

/**
 * User — authentication/authorization entity.
 *
 * Deliberately separated from Customer: a customer record holds personal info;
 * a user record holds login credentials. This separation lets admins exist
 * without a corresponding customer entry (customerId == null for admins).
 */
public class User {

    private long userId;
    private String username;
    private String passwordHash;    // BCrypt hash — NEVER the plain-text password
    private UserRole role;
    private Long customerId;        // null for ADMIN users
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User() {}

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = true;
    }

    public boolean isAdmin()    { return UserRole.ADMIN.equals(role); }
    public boolean isCustomer() { return UserRole.CUSTOMER.equals(role); }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', role=%s, active=%b}",
                userId, username, role, active);
    }
}
