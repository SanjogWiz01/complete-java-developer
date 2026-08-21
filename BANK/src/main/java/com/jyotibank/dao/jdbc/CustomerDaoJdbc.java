package com.jyotibank.dao.jdbc;

import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.dao.CustomerDao;
import com.jyotibank.model.Customer;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoJdbc implements CustomerDao {

    @Override
    public long create(Customer customer) {
        String sql = """
                INSERT INTO customers (
                  first_name, last_name, date_of_birth, national_id, email, phone,
                  address_line1, address_line2, city, state, postal_code, is_active
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            if (customer.getDateOfBirth() == null) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(customer.getDateOfBirth()));
            }
            ps.setString(4, customer.getNationalId());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getPhone());
            ps.setString(7, customer.getAddressLine1());
            ps.setString(8, customer.getAddressLine2());
            ps.setString(9, customer.getCity());
            ps.setString(10, customer.getState());
            ps.setString(11, customer.getPostalCode());
            ps.setBoolean(12, customer.isActive());
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("Failed to create customer; no generated ID returned.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findById(long customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch customer by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findAllActive() {
        String sql = "SELECT * FROM customers WHERE is_active = true ORDER BY created_at DESC";
        List<Customer> customers = new ArrayList<>();
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) customers.add(map(rs));
            return customers;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch active customers: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = """
                UPDATE customers
                   SET first_name=?, last_name=?, date_of_birth=?, national_id=?, email=?, phone=?,
                       address_line1=?, address_line2=?, city=?, state=?, postal_code=?, is_active=?
                 WHERE customer_id=?
                """;
        try (var conn = DatabaseConfig.getInstance().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            if (customer.getDateOfBirth() == null) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(customer.getDateOfBirth()));
            }
            ps.setString(4, customer.getNationalId());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getPhone());
            ps.setString(7, customer.getAddressLine1());
            ps.setString(8, customer.getAddressLine2());
            ps.setString(9, customer.getCity());
            ps.setString(10, customer.getState());
            ps.setString(11, customer.getPostalCode());
            ps.setBoolean(12, customer.isActive());
            ps.setLong(13, customer.getCustomerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update customer: " + e.getMessage(), e);
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        Date dob = rs.getDate("date_of_birth");
        customer.setDateOfBirth(dob == null ? null : dob.toLocalDate());
        customer.setNationalId(rs.getString("national_id"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddressLine1(rs.getString("address_line1"));
        customer.setAddressLine2(rs.getString("address_line2"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setPostalCode(rs.getString("postal_code"));
        customer.setActive(rs.getBoolean("is_active"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        customer.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        customer.setUpdatedAt(updatedAt == null ? null : updatedAt.toLocalDateTime());
        return customer;
    }
}
