package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.TransactionDao;
import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.TransactionStatus;
import com.jyotibank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoJdbc implements TransactionDao {

    @Override
    public long create(Transaction transaction) {
        String sql = """
                INSERT INTO transactions (
                  reference_number, account_id, transaction_type, amount,
                  balance_before, balance_after, description, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, transaction.getReferenceNumber());
            ps.setLong(2, transaction.getAccountId());
            ps.setString(3, transaction.getTransactionType().name());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBigDecimal(5, transaction.getBalanceBefore());
            ps.setBigDecimal(6, transaction.getBalanceAfter());
            ps.setString(7, transaction.getDescription());
            ps.setString(8, transaction.getStatus().name());
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create transaction; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaction> findByAccountId(long accountId, int limit) {
        String sql = """
                SELECT * FROM transactions
                 WHERE account_id = ?
                 ORDER BY created_at DESC
                 LIMIT ?
                """;
        List<Transaction> history = new ArrayList<>();
        int boundedLimit = Math.max(1, Math.min(limit, 500));
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            ps.setInt(2, boundedLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) history.add(map(rs));
            }
            return history;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch transaction history: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaction> findByAccountIdAndDateRange(long accountId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT * FROM transactions
                 WHERE account_id = ? AND created_at >= ? AND created_at < ?
                 ORDER BY created_at DESC
                """;
        List<Transaction> history = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            ps.setTimestamp(2, Timestamp.valueOf(from.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(to.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) history.add(map(rs));
            }
            return history;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch statement: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal sumAmountByTypeOnDate(long accountId, TransactionType type, LocalDate date) {
        String sql = """
                SELECT COALESCE(SUM(amount), 0) AS total
                  FROM transactions
                 WHERE account_id = ?
                   AND transaction_type = ?
                   AND status = 'SUCCESS'
                   AND created_at >= ? AND created_at < ?
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            ps.setString(2, type.name());
            ps.setTimestamp(3, Timestamp.valueOf(date.atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to sum daily transactions: " + e.getMessage(), e);
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setReferenceNumber(rs.getString("reference_number"));
        transaction.setAccountId(rs.getLong("account_id"));
        transaction.setTransactionType(TransactionType.fromString(rs.getString("transaction_type")));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setBalanceBefore(rs.getBigDecimal("balance_before"));
        transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
        transaction.setDescription(rs.getString("description"));
        transaction.setStatus(TransactionStatus.fromString(rs.getString("status")));
        Timestamp createdAt = rs.getTimestamp("created_at");
        transaction.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        return transaction;
    }
}
