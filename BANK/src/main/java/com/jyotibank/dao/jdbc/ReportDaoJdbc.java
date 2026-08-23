package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.ReportDao;
import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReportDaoJdbc implements ReportDao {

    @Override
    public BigDecimal totalActiveDeposits() {
        String sql = "SELECT COALESCE(SUM(balance), 0) AS total FROM accounts WHERE status = 'ACTIVE'";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal("total") : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to compute deposit liabilities: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DormancyCandidate> findDormancyCandidates(int days) {
        String sql = """
                SELECT a.account_id, a.account_number, a.account_type, a.balance,
                       MAX(t.created_at) AS last_activity
                  FROM accounts a
                  LEFT JOIN transactions t ON t.account_id = a.account_id
                 WHERE a.status = 'ACTIVE'
                 GROUP BY a.account_id, a.account_number, a.account_type, a.balance
                HAVING last_activity IS NULL OR last_activity < NOW() - INTERVAL ? DAY
                 ORDER BY last_activity ASC
                """;
        List<DormancyCandidate> candidates = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Math.max(1, days));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp lastActivity = rs.getTimestamp("last_activity");
                    candidates.add(new DormancyCandidate(
                            rs.getLong("account_id"),
                            rs.getString("account_number"),
                            AccountType.fromString(rs.getString("account_type")).getDisplayName(),
                            rs.getBigDecimal("balance"),
                            lastActivity == null ? null : lastActivity.toLocalDateTime()));
                }
            }
            return candidates;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to compute dormancy report: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TypeCount> countAccountsByType() {
        String sql = """
                SELECT account_type, COUNT(*) AS cnt
                  FROM accounts
                 GROUP BY account_type
                 ORDER BY cnt DESC
                """;
        List<TypeCount> counts = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.add(new TypeCount(
                        AccountType.fromString(rs.getString("account_type")).getDisplayName(),
                        rs.getLong("cnt")));
            }
            return counts;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to count accounts by type: " + e.getMessage(), e);
        }
    }
}
