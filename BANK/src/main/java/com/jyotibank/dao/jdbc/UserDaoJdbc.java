package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.UserDao;
import com.jyotibank.model.User;
import com.jyotibank.model.enums.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {

    @Override
    public long create(User user) {
        String sql = """
                INSERT INTO users (username, password_hash, role, customer_id, is_active)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole().name());
            if (user.getCustomerId() == null) {
                ps.setNull(4, java.sql.Types.BIGINT);
            } else {
                ps.setLong(4, user.getCustomerId());
            }
            ps.setBoolean(5, user.isActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create user; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch user by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch user by username: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check username existence: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateLastLogin(long userId, LocalDateTime lastLogin) {
        String sql = "UPDATE users SET last_login = ? WHERE user_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(lastLogin));
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update last login: " + e.getMessage(), e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(UserRole.fromString(rs.getString("role")));
        long customerId = rs.getLong("customer_id");
        user.setCustomerId(rs.wasNull() ? null : customerId);
        user.setActive(rs.getBoolean("is_active"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        user.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        Timestamp lastLogin = rs.getTimestamp("last_login");
        user.setLastLogin(lastLogin == null ? null : lastLogin.toLocalDateTime());
        return user;
    }
}
