import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/*
 * Managing Connections and Statements in JDBC
 * 
 * Connection Management:
 *   -.DriverManager.getConnection() - creates a new connection each time (expensive)
 *   - Connection pooling - reuses existing connections (efficient)
 *   - Connection properties: auto-commit, catalog, transaction isolation, read-only, etc.
 * 
 * Statement Types:
 *   1. Statement        - For simple static SQL queries (no parameters)
 *   2. PreparedStatement - For parameterized/precompiled SQL (prevents SQL injection)
 *   3. CallableStatement - For calling stored procedures/functions
 * 
 * Statement Configuration:
 *   - ResultSet type: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE
 *   - ResultSet concurrency: CONCUR_READ_ONLY, CONCUR_UPDATABLE
 *   - ResultSet holdability: HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT
 *   - Fetch size and max rows limits
 * 
 * Best Practices:
 *   - Always close connections, statements, and result sets in finally block or use try-with-resources
 *   - Use connection pooling for production applications
 *   - Use PreparedStatement over Statement for repeated queries
 *   - Set appropriate fetch size for large result sets
 */

// 5.3 Managing Connections and Statements - Connection config, statement types, scrollable result sets
public class ManagingConnectionsAndStatements {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // =============================================
        // CONNECTION MANAGEMENT
        // =============================================

        System.out.println("=== Connection Management ===\n");

        // 1. Basic Connection
        System.out.println("--- 1. Basic Connection ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Connected successfully!");
            System.out.println("Auto-commit: " + conn.getAutoCommit());
            System.out.println("Catalog: " + conn.getCatalog());
            System.out.println("Transaction Isolation: " + conn.getTransactionIsolation());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Connection with Properties
        System.out.println("\n--- 2. Connection with Properties ---");
        java.util.Properties props = new java.util.Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASS);
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("allowPublicKeyRetrieval", "true");
        props.setProperty("connectTimeout", "5000");
        props.setProperty("socketTimeout", "10000");

        try (Connection conn = DriverManager.getConnection(URL, props)) {
            System.out.println("Connected with properties!");
            System.out.println("Network Timeout: " + conn.getNetworkTimeout());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. Connection Configuration
        System.out.println("\n--- 3. Connection Configuration ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // Set auto-commit mode
            conn.setAutoCommit(false);
            System.out.println("Auto-commit disabled: " + conn.getAutoCommit());

            // Set read-only mode
            conn.setReadOnly(true);
            System.out.println("Read-only mode: " + conn.isReadOnly());

            // Set transaction isolation
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("Isolation level set: " + conn.getTransactionIsolation());

            // Reset for proper close
            conn.setReadOnly(false);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // =============================================
        // STATEMENT MANAGEMENT
        // =============================================

        System.out.println("\n=== Statement Management ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            // 1. Basic Statement
            System.out.println("--- 1. Basic Statement ---");
            try (Statement stmt = conn.createStatement()) {
                System.out.println("Statement created.");
                System.out.println("Fetch Size: " + stmt.getFetchSize());
                System.out.println("Max Field Size: " + stmt.getMaxFieldSize());
                System.out.println("Max Rows: " + stmt.getMaxRows());
                System.out.println("Query Timeout: " + stmt.getQueryTimeout());

                // Set custom fetch size
                stmt.setFetchSize(100);
                System.out.println("Fetch size set to: " + stmt.getFetchSize());

                // Execute a simple query
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM students");
                if (rs.next()) {
                    System.out.println("Total students: " + rs.getInt(1));
                }
                rs.close();
            }

            // 2. Statement with Scrollable ResultSet
            System.out.println("\n--- 2. Scrollable ResultSet Statement ---");
            try (Statement stmt = conn.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_READ_ONLY)) {
                System.out.println("Scrollable statement created.");
                ResultSet rs = stmt.executeQuery("SELECT * FROM students");

                // Move to last row
                if (rs.last()) {
                    System.out.println("Last row number: " + rs.getRow());
                    System.out.println("Last student: " + rs.getString("name"));
                }

                // Move to first row
                if (rs.first()) {
                    System.out.println("First row number: " + rs.getRow());
                    System.out.println("First student: " + rs.getString("name"));
                }
                rs.close();
            }

            // 3. Statement with Updatable ResultSet
            System.out.println("\n--- 3. Updatable ResultSet Statement ---");
            try (Statement stmt = conn.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_UPDATABLE)) {
                System.out.println("Updatable statement created.");
                // Note: Not all queries/tables support updatable result sets
            }

            // 4. Statement Execution Limits
            System.out.println("\n--- 4. Statement Limits ---");
            try (Statement stmt = conn.createStatement()) {
                stmt.setMaxRows(5);
                System.out.println("Max rows set to: " + stmt.getMaxRows());

                stmt.setQueryTimeout(10);
                System.out.println("Query timeout set to: " + stmt.getQueryTimeout() + " seconds");

                ResultSet rs = stmt.executeQuery("SELECT * FROM students");
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                System.out.println("Rows returned (limited): " + count);
                rs.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // =============================================
        // TRY-WITH-RESOURCES (AutoCloseable)
        // =============================================
        System.out.println("\n=== Try-With-Resources (AutoCloseable) ===\n");
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM students LIMIT 3")
        ) {
            System.out.println("Resources managed by try-with-resources.");
            while (rs.next()) {
                System.out.println("  Student: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll connections and statements closed automatically.");
    }
}
