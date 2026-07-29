import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/*
 * SQL Escapes in JDBC
 * 
 * SQL Escape Syntax:
 *   - Provides a standard way to write database-independent SQL
 *   - Uses curly brace notation: {escape syntax}
 *   - JDBC driver translates escape syntax to native database syntax
 *   - Enables portable SQL across different database systems
 * 
 * Types of SQL Escapes:
 * 
 * 1. LIKE Escape:
 *    {escape 'escape-char'}
 *    Used when LIKE pattern characters need to be part of the literal
 *    Example: SELECT * FROM t WHERE col LIKE '%20%%' {escape '/'}
 * 
 * 2. Date/Time/Timestamp Escapes:
 *    {d 'yyyy-MM-dd'}      - Date literal
 *    {t 'HH:mm:ss'}        - Time literal
 *    {ts 'yyyy-MM-dd HH:mm:ss'} - Timestamp literal
 * 
 * 3. Outer Join Escape:
 *    {oj table LEFT|RIGHT|FULL OUTER JOIN table ON ...}
 *    Standardizes outer join syntax across databases
 * 
 * 4. Procedure Escape:
 *    {call procedure_name(args)}
 *    {call function_name(args)}
 *    Standardizes stored procedure/function calls
 * 
 * 5. Function Escape:
 *    {fn function_name(args)}
 *    Maps to database-specific functions
 *    Common functions: CONCAT, TRIM, UPPER, LOWER, LENGTH, etc.
 * 
 * 6. Scalar Escape:
 *    {fn CURRENT_DATE}       - Current date
 *    {fn CURRENT_TIME}       - Current time
 *    {fn CURRENT_TIMESTAMP}  - Current timestamp
 * 
 * 7. Limit Escape (Database-specific):
 *    {limit n}  - Some drivers support this
 * 
 * Note: Modern JDBC (JDBC 4.0+) also supports direct Java methods
 * for dates/timestamps, making date escapes less necessary.
 */

// 5.8 SQL Escapes - LIKE, date/time, outer join, procedure, and function escape syntax
public class SQLEscapes {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // Setup test table
        setupTestTable();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // =============================================
            // 1. LIKE ESCAPE
            // =============================================
            System.out.println("=== 1. LIKE Escape ===\n");

            // Insert data with special LIKE characters
            stmt.executeUpdate("INSERT INTO events (event_name, event_date, event_time, description) VALUES "
                    + "('100% Discount Sale', '2025-12-25', '10:00:00', 'Big holiday sale'), "
                    + "('Buy 1 Get 1 Free', '2025-11-15', '14:30:00', 'Special promotion'), "
                    + "('50% Off Everything', '2025-10-01', '09:00:00', 'October event'), "
                    + "('Regular Meeting', '2025-09-20', '11:00:00', 'Team meeting')");

            // Finding events with literal % in the name
            // Using JDBC escape syntax for LIKE
            String likeEscapeQuery = "SELECT * FROM events WHERE event_name LIKE '%100!%%' {escape '!'}";
            System.out.println("Query (escape syntax): " + likeEscapeQuery);
            ResultSet rs = stmt.executeQuery(likeEscapeQuery);
            while (rs.next()) {
                System.out.println("  Found: " + rs.getString("event_name"));
            }
            rs.close();

            // Same query without escape (driver handles it)
            String likeQuery = "SELECT * FROM events WHERE event_name LIKE ?";
            System.out.println("\nUsing PreparedStatement (safer approach):");
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(likeQuery)) {
                pstmt.setString(1, "%100!%%");
                ResultSet rs2 = pstmt.executeQuery();
                while (rs2.next()) {
                    System.out.println("  Found: " + rs2.getString("event_name"));
                }
                rs2.close();
            }

            // =============================================
            // 2. DATE/TIME/TIMESTAMP ESCAPES
            // =============================================
            System.out.println("\n=== 2. Date/Time/Timestamp Escapes ===\n");

            // Date escape
            System.out.println("--- Date Escape ---");
            String dateQuery = "{d '2025-12-25'}";
            String fullDateQuery = "SELECT * FROM events WHERE event_date = " + dateQuery;
            System.out.println("Query: " + fullDateQuery);
            ResultSet rsDate = stmt.executeQuery(fullDateQuery);
            while (rsDate.next()) {
                System.out.println("  Event on Christmas: " + rsDate.getString("event_name"));
            }
            rsDate.close();

            // Time escape
            System.out.println("\n--- Time Escape ---");
            String timeQuery = "{t '10:00:00'}";
            String fullTimeQuery = "SELECT * FROM events WHERE event_time = " + timeQuery;
            System.out.println("Query: " + fullTimeQuery);
            ResultSet rsTime = stmt.executeQuery(fullTimeQuery);
            while (rsTime.next()) {
                System.out.println("  Event at 10:00: " + rsTime.getString("event_name"));
            }
            rsTime.close();

            // Timestamp escape
            System.out.println("\n--- Timestamp Escape ---");
            String tsQuery = "{ts '2025-12-25 10:00:00'}";
            String fullTsQuery = "SELECT * FROM events WHERE event_date = {d '2025-12-25'} AND event_time = {t '10:00:00'}";
            System.out.println("Query: " + fullTsQuery);
            ResultSet rsTs = stmt.executeQuery(fullTsQuery);
            while (rsTs.next()) {
                System.out.println("  Event: " + rsTs.getString("event_name")
                        + " on " + rsTs.getDate("event_date")
                        + " at " + rsTs.getTime("event_time"));
            }
            rsTs.close();

            // Modern approach using PreparedStatement setters
            System.out.println("\n--- Modern Approach (PreparedStatement) ---");
            String modernQuery = "SELECT * FROM events WHERE event_date = ? AND event_time >= ?";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(modernQuery)) {
                pstmt.setDate(1, Date.valueOf("2025-12-25"));
                pstmt.setTime(2, Time.valueOf("09:00:00"));
                ResultSet rsMod = pstmt.executeQuery();
                while (rsMod.next()) {
                    System.out.println("  " + rsMod.getString("event_name")
                            + " - " + rsMod.getDate("event_date")
                            + " " + rsMod.getTime("event_time"));
                }
                rsMod.close();
            }

            // =============================================
            // 3. OUTER JOIN ESCAPE
            // =============================================
            System.out.println("\n=== 3. Outer Join Escape ===\n");

            // Create related tables for join demo
            stmt.executeUpdate("DROP TABLE IF EXISTS order_items");
            stmt.executeUpdate("DROP TABLE IF EXISTS orders");
            stmt.executeUpdate("CREATE TABLE orders ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, customer_name VARCHAR(50), order_date DATE)");
            stmt.executeUpdate("CREATE TABLE order_items ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, order_id INT, product VARCHAR(50), quantity INT)");
            stmt.executeUpdate("INSERT INTO orders (customer_name, order_date) VALUES "
                    + "('Alice', '2025-06-01'), ('Bob', '2025-06-02'), ('Charlie', '2025-06-03')");
            stmt.executeUpdate("INSERT INTO order_items (order_id, product, quantity) VALUES "
                    + "(1, 'Laptop', 1), (1, 'Mouse', 2), (2, 'Phone', 1)");

            // Outer Join Escape syntax
            String outerJoinQuery = "{oj orders LEFT OUTER JOIN order_items ON orders.id = order_items.order_id}";
            System.out.println("Outer Join Escape Query:");
            System.out.println("  " + outerJoinQuery);
            ResultSet rsJoin = stmt.executeQuery(
                    "SELECT * FROM " + outerJoinQuery);
            while (rsJoin.next()) {
                String customer = rsJoin.getString("customer_name");
                String product = rsJoin.getString("product");
                System.out.println("  " + customer + " - " + (product != null ? product : "No items"));
            }
            rsJoin.close();

            // Standard LEFT JOIN (equivalent, more common in practice)
            System.out.println("\nStandard LEFT JOIN (same result):");
            ResultSet rsJoin2 = stmt.executeQuery(
                    "SELECT o.customer_name, oi.product FROM orders o "
                    + "LEFT JOIN order_items oi ON o.id = oi.order_id");
            while (rsJoin2.next()) {
                System.out.println("  " + rsJoin2.getString("customer_name")
                        + " - " + (rsJoin2.getString("product") != null
                        ? rsJoin2.getString("product") : "No items"));
            }
            rsJoin2.close();

            // =============================================
            // 4. PROCEDURE ESCAPE
            // =============================================
            System.out.println("\n=== 4. Procedure Escape ===\n");

            // Create a stored procedure
            try {
                stmt.executeUpdate("DROP PROCEDURE IF EXISTS get_student_count");
                stmt.executeUpdate("CREATE PROCEDURE get_student_count(OUT count INT) "
                        + "BEGIN SELECT COUNT(*) INTO count FROM students; END");
                System.out.println("Stored procedure 'get_student_count' created.");

                // Call procedure using escape syntax
                // Note: MySQL uses {call proc_name(args)} syntax
                String procCall = "{call get_student_count(?)}";
                System.out.println("Procedure Call: " + procCall);
                try (java.sql.CallableStatement cstmt = conn.prepareCall(procCall)) {
                    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
                    cstmt.execute();
                    int count = cstmt.getInt(1);
                    System.out.println("Student count: " + count);
                }
            } catch (SQLException e) {
                System.out.println("Procedure not supported or error: " + e.getMessage());
            }

            // =============================================
            // 5. FUNCTION ESCAPE
            // =============================================
            System.out.println("\n=== 5. Function Escape ===\n");

            // String functions
            System.out.println("--- String Functions ---");
            String[] functionQueries = {
                    "SELECT {fn UPPER('hello world')} AS result",
                    "SELECT {fn LOWER('HELLO WORLD')} AS result",
                    "SELECT {fn TRIM('  spaces  ')} AS result",
                    "SELECT {fn CONCAT('Hello', ' ', 'World')} AS result",
                    "SELECT {fn LENGTH('Hello World')} AS result",
                    "SELECT {fn SUBSTRING('Hello World', 1, 5)} AS result"
            };

            for (String query : functionQueries) {
                try {
                    ResultSet rsFn = stmt.executeQuery(query);
                    if (rsFn.next()) {
                        System.out.println("  " + query + " => " + rsFn.getString(1));
                    }
                    rsFn.close();
                } catch (SQLException e) {
                    System.out.println("  " + query + " => Error: " + e.getMessage());
                }
            }

            // =============================================
            // 6. SCALAR ESCAPE (Built-in Functions)
            // =============================================
            System.out.println("\n=== 6. Scalar Escape ===\n");

            String[] scalarQueries = {
                    "SELECT {fn CURRENT_DATE} AS current_date",
                    "SELECT {fn CURRENT_TIME} AS current_time",
                    "SELECT {fn CURRENT_TIMESTAMP} AS current_timestamp"
            };

            for (String query : scalarQueries) {
                try {
                    ResultSet rsScalar = stmt.executeQuery(query);
                    if (rsScalar.next()) {
                        System.out.println("  " + query);
                        System.out.println("  => " + rsScalar.getObject(1));
                    }
                    rsScalar.close();
                } catch (SQLException e) {
                    System.out.println("  " + query + " => Error: " + e.getMessage());
                }
            }

            // =============================================
            // 7. ESCAPING SPECIAL CHARACTERS
            // =============================================
            System.out.println("\n=== 7. Escaping Special Characters in Strings ===\n");

            // Single quote escaping
            String insertWithQuotes = "INSERT INTO events (event_name, event_date, event_time, description) "
                    + "VALUES ('It''s a Test', '2025-01-01', '12:00:00', 'Testing single quotes')";
            stmt.executeUpdate(insertWithQuotes);
            System.out.println("Inserted event with single quote using double-quote escape.");

            // Backslash escaping
            String insertWithBackslash = "INSERT INTO events (event_name, event_date, event_time, description) "
                    + "VALUES ('Path\\\\File', '2025-01-01', '12:00:00', 'Testing backslash')";
            stmt.executeUpdate(insertWithBackslash);
            System.out.println("Inserted event with backslash.");

            // Using PreparedStatement (auto-escaping)
            System.out.println("\nUsing PreparedStatement (auto-escaping):");
            String safeInsert = "INSERT INTO events (event_name, event_date, event_time, description) VALUES (?, ?, ?, ?)";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(safeInsert)) {
                pstmt.setString(1, "It's a \"safe\" value with 'quotes' and \\backslashes");
                pstmt.setDate(2, Date.valueOf("2025-06-15"));
                pstmt.setTime(3, Time.valueOf("15:30:00"));
                pstmt.setString(4, "All special characters handled automatically");
                pstmt.executeUpdate();
                System.out.println("  PreparedStatement auto-escapes special characters.");
            }

            // =============================================
            // SUMMARY
            // =============================================
            System.out.println("\n=== SQL Escape Summary ===");
            System.out.println("{escape char}      - LIKE pattern escape character");
            System.out.println("{d 'yyyy-MM-dd'}   - Date literal");
            System.out.println("{t 'HH:mm:ss'}     - Time literal");
            System.out.println("{ts 'yyyy-MM-dd HH:mm:ss'} - Timestamp literal");
            System.out.println("{oj ...}           - Outer Join syntax");
            System.out.println("{call proc(...)}   - Stored Procedure call");
            System.out.println("{fn func(...)}     - Database function call");
            System.out.println("{fn CURRENT_DATE}  - Current date");
            System.out.println("{fn CURRENT_TIME}  - Current time");
            System.out.println("{fn CURRENT_TIMESTAMP} - Current timestamp");

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }

        // Cleanup
        cleanupTestTable();
    }

    private static void setupTestTable() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS events");
            stmt.executeUpdate("CREATE TABLE events ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "event_name VARCHAR(100), "
                    + "event_date DATE, "
                    + "event_time TIME, "
                    + "description VARCHAR(255)"
                    + ")");
            System.out.println("Test table 'events' created.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cleanupTestTable() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS events");
            stmt.executeUpdate("DROP TABLE IF EXISTS order_items");
            stmt.executeUpdate("DROP TABLE IF EXISTS orders");
            stmt.executeUpdate("DROP PROCEDURE IF EXISTS get_student_count");
            System.out.println("\nCleanup: All test tables and procedures dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
