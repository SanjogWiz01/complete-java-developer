package com.jyotibank.service;

import com.jyotibank.dao.AuditLogDao;
import com.jyotibank.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuditService — central point for recording who did what.
 *
 * <p>Audit writes must never break the business operation they are observing:
 * a failed audit insert is logged and swallowed, while the caller's result
 * stands. This mirrors real banking systems where the trail is important but
 * must not become a single point of failure.
 */
public class AuditService {

    public static final long SYSTEM_ACTOR = 0L;

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogDao auditLogDao;
    private final String ipAddress;

    public AuditService(AuditLogDao auditLogDao) {
        this(auditLogDao, "127.0.0.1"); // console app — loopback
    }

    public AuditService(AuditLogDao auditLogDao, String ipAddress) {
        this.auditLogDao = auditLogDao;
        this.ipAddress = ipAddress;
    }

    /** Fire-and-forget audit write. Never throws. */
    public void record(long userId, String action, String entityType, Long entityId, String details) {
        try {
            AuditLog log = new AuditLog(userId, action, entityType, entityId, details);
            log.setIpAddress(ipAddress);
            auditLogDao.create(log);
        } catch (RuntimeException e) {
            logger.warn("Audit write failed for action={} entity={}/{}: {}",
                    action, entityType, entityId, e.getMessage());
        }
    }
}
