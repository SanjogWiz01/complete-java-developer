package com.jyotibank.dao;

import com.jyotibank.model.AuditLog;

import java.util.List;

public interface AuditLogDao {
    long create(AuditLog auditLog);
    List<AuditLog> findRecent(int limit);
    List<AuditLog> findByUserId(long userId, int limit);
}
