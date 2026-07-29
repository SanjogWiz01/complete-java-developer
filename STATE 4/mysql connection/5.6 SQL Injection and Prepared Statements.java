import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * SQL Injection and Prepared Statements
 * 
 * SQL Injection:
 *   - A security vulnerability where malicious SQL code is inserted into input fields
 *   - Occurs when user input is concatenated directly into SQL queries
 *   - Can lead to unauthorized data access, data modification, or data deletion
 * 
 * Example of Vulnerable Code (NEVER DO THIS):
 *   String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
 *   If userInput = "admin' OR '1'='1", the query becomes:
 *   SELECT * FROM users WHERE username = 'admin' OR '1'='1'
 *   This returns ALL rows because '1'='1' is always true!
 * 
 * Prevention:
 *   1. Use PreparedStatement with parameterized queries (? placeholders)
 *   2. Use stored procedures
 *   3. Input validation and sanitization
 *   4. Use ORM frameworks (Hibernate, JPA)
 *   5. Apply least privilege principle for database accounts
 * 
 * PreparedStatement:
 *   - Precompiles SQL with placeholders (?)
 *   - Parameters are set using setXXX() methods
 *   - Prevents SQL injection by properly escaping input
 *   - Better performance for repeated queries (precompiled)
 *   - Supports setInt(), setString(), setDouble(), setDate(), etc.
 */

// 5.6 SQL Injection and Prepared Statements - Preventing injection with parameterized queries
public class SQLInjectionAndPreparedStatements {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // =============================================
        // SQL INJECTION DEMONSTRATION
        // =============================================
        System.out.println("=== SQL Injection Vulnerability Demo ===\n");

        // Setup test table
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            stmt.executeUpdate("CREATE TABLE users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL, "
                    + "password VARCHAR(50) NOT NULL, "
                    + "role VARCHAR(20) DEFAULT 'user'"
                    + ")");
            stmt.executeUpdate("INSERT INTO users (username, password, role) VALUES "
                    + "('admin', 'secret123', 'admin'), "
                    + "('john', 'pass456', 'user'), "
                    + "('jane', 'mypass', 'user')");
            System.out.println("Test table 'users' created with 3 records.\n");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // VULNERABLE: String concatenation (NEVER DO THIS)
        System.out.println("--- VULNERABLE: String Concatenation (SQL Injection Risk) ---");
        String maliciousInput = "admin' OR '1'='1";
        String vulnerableQuery = "SELECT * FROM users WHERE username = '" + maliciousInput + "'";
        System.out.println("User input: " + maliciousInput);
        System.out.println("Constructed query: " + vulnerableQuery);
        System.out.println("WARNING: This query would return ALL users!\n");

        // =============================================
        // SAFE: PREPARED STATEMENTS
        // =============================================
        System.out.println("=== Safe: PreparedStatement (Parameterized Query) ===\n");

        String safeQuery = "SELECT * FROM users WHERE username = ? AND password = ?";

        // 1. Basic PreparedStatement
        System.out.println("--- 1. Basic PreparedStatement ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(safeQuery)) {

            pstmt.setString(1, "admin");
            pstmt.setString(2, "secret123");
            System.out.println("Query: " + safeQuery);
            System.out.println("Parameters: username='admin', password='secret123'");

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful! Role: " + rs.getString("role"));
            } else {
                System.out.println("Login failed!");
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. SQL Injection Attempt with PreparedStatement (SAFE)
        System.out.println("\n--- 2. SQL Injection Attempt with PreparedStatement (BLOCKED) ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(safeQuery)) {

            String maliciousUser = "admin' OR '1'='1";
            String maliciousPass = "anything";

            pstmt.setString(1, maliciousUser);
            pstmt.setString(2, maliciousPass);
            System.out.println("Malicious input: " + maliciousUser);
            System.out.println("The PreparedStatement treats the entire input as a string value.");
            System.out.println("It will search for a user literally named: admin' OR '1'='1");

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Found match (should NOT happen with PreparedStatement).");
            } else {
                System.out.println("No match found. SQL Injection BLOCKED!");
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. PreparedStatement with Different Data Types
        System.out.println("\n--- 3. PreparedStatement with Different Data Types ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS products");
            stmt.executeUpdate("CREATE TABLE products ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), "
                    + "price DOUBLE, "
                    + "quantity INT, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");
            stmt.executeUpdate("INSERT INTO products (name, price, quantity) VALUES "
                    + "('Laptop', 999.99, 50), ('Phone', 699.99, 100), ('Tablet', 499.99, 75)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertQuery = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            // Set different data types
            pstmt.setString(1, "Keyboard");
            pstmt.setDouble(2, 49.99);
            pstmt.setInt(3, 200);

            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row with setString, setDouble, setInt.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 4. PreparedStatement with LIKE Clause
        System.out.println("\n--- 4. PreparedStatement with LIKE ---");
        String likeQuery = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(likeQuery)) {

            pstmt.setString(1, "%Phone%");
            System.out.println("Searching for products containing 'Phone'...");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("  Found: " + rs.getString("name") + " - $" + rs.getDouble("price"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 5. PreparedStatement with IN Clause
        System.out.println("\n--- 5. PreparedStatement with IN Clause ---");
        String inQuery = "SELECT * FROM products WHERE id IN (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(inQuery)) {

            pstmt.setInt(1, 1);
            pstmt.setInt(2, 3);
            pstmt.setInt(3, 5);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("  Product: " + rs.getString("name"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6. Batch Insert with PreparedStatement
        System.out.println("\n--- 6. Batch Insert with PreparedStatement ---");
        String batchInsert = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(batchInsert)) {

            Object[][] products = {
                    {"Monitor", 299.99, 30},
                    {"Mouse", 29.99, 500},
                    {"Headset", 79.99, 150}
            };

            for (Object[] product : products) {
                pstmt.setString(1, (String) product[0]);
                pstmt.setDouble(2, (double) product[1]);
                pstmt.setInt(3, (int) product[2]);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            System.out.println("Batch insert completed. Rows: " + java.util.Arrays.toString(results));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Verify all products
        System.out.println("\n--- All Products After Inserts ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                System.out.println("  " + rs.getString("name") + " - $"
                        + rs.getDouble("price") + " (qty: " + rs.getInt("quantity") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cleanup
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            stmt.executeUpdate("DROP TABLE IF EXISTS products");
            System.out.println("\nCleanup: Test tables dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
