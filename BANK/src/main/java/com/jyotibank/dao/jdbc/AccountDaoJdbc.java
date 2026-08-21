package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.AccountDao;
import com.jyotibank.model.Account;
import com.jyotibank.model.CurrentAccount;
import com.jyotibank.model.FixedDepositAccount;
import com.jyotibank.model.SavingsAccount;
import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.AccountType;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {

    @Override
    public long create(Account account) {
        String sql = """
                INSERT INTO accounts (
                  account_number, customer_id, account_type, balance, status, interest_rate, minimum_balance
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getAccountNumber());
            ps.setLong(2, account.getCustomerId());
            ps.setString(3, account.getAccountType().name());
            ps.setBigDecimal(4, account.getBalance());
            ps.setString(5, account.getStatus().name());
            ps.setBigDecimal(6, account.getInterestRate());
            ps.setBigDecimal(7, account.getMinimumBalance());
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create account; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create account: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Account> findById(long accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch account by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch account by number: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> findByCustomerId(long customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY opened_at DESC";
        List<Account> accounts = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) accounts.add(map(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch customer accounts: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateBalance(long accountId, BigDecimal balance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, balance);
            ps.setLong(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update account balance: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateStatus(long accountId, AccountStatus accountStatus) {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountStatus.name());
            ps.setLong(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update account status: " + e.getMessage(), e);
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        AccountType accountType = AccountType.fromString(rs.getString("account_type"));
        Account account = switch (accountType) {
            case SAVINGS -> new SavingsAccount();
            case CURRENT -> new CurrentAccount();
            case FIXED_DEPOSIT -> new FixedDepositAccount();
        };
        account.setAccountId(rs.getLong("account_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setCustomerId(rs.getLong("customer_id"));
        account.setAccountType(accountType);
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(AccountStatus.fromString(rs.getString("status")));
        account.setInterestRate(rs.getBigDecimal("interest_rate"));
        account.setMinimumBalance(rs.getBigDecimal("minimum_balance"));
        Timestamp openedAt = rs.getTimestamp("opened_at");
        account.setOpenedAt(openedAt == null ? null : openedAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        account.setUpdatedAt(updatedAt == null ? null : updatedAt.toLocalDateTime());
        return account;
    }
}
