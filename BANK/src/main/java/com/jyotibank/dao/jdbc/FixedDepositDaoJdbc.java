package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.FixedDepositDao;
import com.jyotibank.model.FixedDeposit;
import com.jyotibank.model.enums.FDStatus;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FixedDepositDaoJdbc implements FixedDepositDao {

    @Override
    public long create(FixedDeposit fixedDeposit) {
        String sql = """
                INSERT INTO fixed_deposits (
                  linked_account_id, fd_account_id, principal_amount, interest_rate, tenure_months,
                  maturity_amount, start_date, maturity_date, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, fixedDeposit.getLinkedAccountId());
            ps.setLong(2, fixedDeposit.getFdAccountId());
            ps.setBigDecimal(3, fixedDeposit.getPrincipalAmount());
            ps.setBigDecimal(4, fixedDeposit.getInterestRate());
            ps.setInt(5, fixedDeposit.getTenureMonths());
            ps.setBigDecimal(6, fixedDeposit.getMaturityAmount());
            ps.setDate(7, Date.valueOf(fixedDeposit.getStartDate()));
            ps.setDate(8, Date.valueOf(fixedDeposit.getMaturityDate()));
            ps.setString(9, fixedDeposit.getStatus().name());
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create fixed deposit; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create fixed deposit: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<FixedDeposit> findById(long fixedDepositId) {
        String sql = "SELECT * FROM fixed_deposits WHERE fd_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fixedDepositId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch fixed deposit by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FixedDeposit> findByFdAccountId(long fdAccountId) {
        String sql = "SELECT * FROM fixed_deposits WHERE fd_account_id = ? ORDER BY created_at DESC";
        List<FixedDeposit> deposits = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fdAccountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) deposits.add(map(rs));
            }
            return deposits;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch fixed deposit list: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FixedDeposit> findByLinkedAccountId(long linkedAccountId) {
        String sql = "SELECT * FROM fixed_deposits WHERE linked_account_id = ? ORDER BY created_at DESC";
        List<FixedDeposit> deposits = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, linkedAccountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) deposits.add(map(rs));
            }
            return deposits;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch linked fixed deposits: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(FixedDeposit fixedDeposit) {
        String sql = """
                UPDATE fixed_deposits
                   SET maturity_amount = ?, status = ?, maturity_date = ?
                 WHERE fd_id = ?
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, fixedDeposit.getMaturityAmount());
            ps.setString(2, fixedDeposit.getStatus().name());
            ps.setDate(3, Date.valueOf(fixedDeposit.getMaturityDate()));
            ps.setLong(4, fixedDeposit.getFdId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update fixed deposit: " + e.getMessage(), e);
        }
    }

    private FixedDeposit map(ResultSet rs) throws SQLException {
        FixedDeposit fd = new FixedDeposit();
        fd.setFdId(rs.getLong("fd_id"));
        fd.setLinkedAccountId(rs.getLong("linked_account_id"));
        fd.setFdAccountId(rs.getLong("fd_account_id"));
        fd.setPrincipalAmount(rs.getBigDecimal("principal_amount"));
        fd.setInterestRate(rs.getBigDecimal("interest_rate"));
        fd.setTenureMonths(rs.getInt("tenure_months"));
        fd.setMaturityAmount(rs.getBigDecimal("maturity_amount"));
        Date startDate = rs.getDate("start_date");
        fd.setStartDate(startDate == null ? null : startDate.toLocalDate());
        Date maturityDate = rs.getDate("maturity_date");
        fd.setMaturityDate(maturityDate == null ? null : maturityDate.toLocalDate());
        fd.setStatus(FDStatus.fromString(rs.getString("status")));
        Timestamp createdAt = rs.getTimestamp("created_at");
        fd.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        return fd;
    }
}
