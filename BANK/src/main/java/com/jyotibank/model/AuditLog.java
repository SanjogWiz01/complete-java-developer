package com.jyotibank.model;

import java.time.LocalDateTime;

/**
 * AuditLog — every significant action is recorded here.
 *
 * A banking system must maintain a tamper-evident trail of who did what and when.
 * Audit logs are INSERT-only — never UPDATE or DELETE an audit record.
 */
public class AuditLog {

    private long logId;
    private long userId;
    private String action;          // e.g., "CUSTOMER_CREATE", "ACCOUNT_BLOCK", "LOGIN"
    private String entityType;      // e.g., "CUSTOMER", "ACCOUNT", "TRANSACTION"
    private Long entityId;          // FK to the relevant row (nullable)
    private String details;         // JSON or plain text description
    private String ipAddress;
    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(long userId, String action, String entityType, Long entityId, String details) {
        this.userId     = userId;
        this.action     = action;
        this.entityType = entityType;
        this.entityId   = entityId;
        this.details    = details;
        this.createdAt  = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public long getLogId() { return logId; }
    public void setLogId(long logId) { this.logId = logId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("AuditLog{id=%d, user=%d, action='%s', entity=%s/%d, at=%s}",
                logId, userId, action, entityType, entityId, createdAt);
    }
}
