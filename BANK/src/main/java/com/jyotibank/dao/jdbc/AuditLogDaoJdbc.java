package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.AuditLogDao;
import com.jyotibank.model.AuditLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDaoJdbc implements AuditLogDao {

    @Override
    public long create(AuditLog auditLog) {
        String sql = """
                INSERT INTO audit_logs (user_id, action, entity_type, entity_id, details, ip_address)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, auditLog.getUserId());
            ps.setString(2, auditLog.getAction());
            ps.setString(3, auditLog.getEntityType());
            if (auditLog.getEntityId() == null) {
                ps.setNull(4, java.sql.Types.BIGINT);
            } else {
                ps.setLong(4, auditLog.getEntityId());
            }
            ps.setString(5, auditLog.getDetails());
            ps.setString(6, auditLog.getIpAddress());
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create audit log; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create audit log: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AuditLog> findRecent(int limit) {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT ?";
        int boundedLimit = Math.max(1, Math.min(limit, 500));
        return query(sql, ps -> ps.setInt(1, boundedLimit));
    }

    @Override
    public List<AuditLog> findByUserId(long userId, int limit) {
        String sql = "SELECT * FROM audit_logs WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        int boundedLimit = Math.max(1, Math.min(limit, 500));
        return query(sql, ps -> {
            ps.setLong(1, userId);
            ps.setInt(2, boundedLimit);
        });
    }

    private List<AuditLog> query(String sql, SqlBinder binder) {
        List<AuditLog> logs = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) logs.add(map(rs));
            }
            return logs;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch audit logs: " + e.getMessage(), e);
        }
    }

    @FunctionalInterface
    private interface SqlBinder {
        void bind(java.sql.PreparedStatement ps) throws SQLException;
    }

    private AuditLog map(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getLong("log_id"));
        log.setUserId(rs.getLong("user_id"));
        log.setAction(rs.getString("action"));
        log.setEntityType(rs.getString("entity_type"));
        long entityId = rs.getLong("entity_id");
        log.setEntityId(rs.wasNull() ? null : entityId);
        log.setDetails(rs.getString("details"));
        log.setIpAddress(rs.getString("ip_address"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        log.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        return log;
    }
}
