import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.sql.BatchUpdateException;
import java.sql.SQLTransientException;
import java.sql.SQLNonTransientException;

/*
 * Result Sets and Exception Handling in JDBC
 * 
 * ResultSet:
 *   - Maintains a cursor pointing to the current row
 *   - Initially positioned before the first row
 *   - next() moves cursor to the next row, returns false when no more rows
 *   - Can retrieve data by column index (1-based) or column name
 *   - Types: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE
 *   - Concurrency: CONCUR_READ_ONLY, CONCUR_UPDATABLE
 * 
 * ResultSet Data Retrieval Methods:
 *   - getString(), getInt(), getLong(), getDouble(), getFloat()
 *   - getBoolean(), getDate(), getTime(), getTimestamp()
 *   - getObject(), getBigDecimal(), getBlob(), getClob()
 * 
 * JDBC Exception Hierarchy:
 *   SQLException (base)
 *     |-- SQLWarning (warnings, non-fatal)
 *     |-- BatchUpdateException (batch update errors)
 *     |-- SQLTransientException (temporary, may succeed on retry)
 *     |     |-- SQLTransientConnectionException
 *     |     |-- SQLTimeoutException
 *     |-- SQLNonTransientException (permanent failures)
 *     |     |-- SQLIntegrityConstraintViolationException
 *     |     |-- SQLDataException
 *     |     |-- SQLSyntaxErrorException
 *     |-- SQLRecoverableException (connection can be recovered)
 * 
 * Exception Handling Best Practices:
 *   - Always catch SQLExceptions
 *   - Use try-with-resources for automatic cleanup
 *   - Check SQLState for portable error handling
 *   - Log exceptions properly, don't just print stack traces
 *   - Implement retry logic for transient exceptions
 */

// 5.4 Result Sets and Exception Handling - Scrollable result sets, exception hierarchy, retry logic
public class ResultSetsAndExceptionHandling {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // =============================================
        // RESULTSET BASICS
        // =============================================
        System.out.println("=== ResultSet Basics ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            // ResultSetMetaData
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("Number of columns: " + columnCount);
            System.out.println("Column names and types:");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("  " + metaData.getColumnName(i)
                        + " (" + metaData.getColumnTypeName(i) + ")");
            }

            System.out.println("\n--- Fetching Data ---");
            int rowNum = 0;
            while (rs.next()) {
                rowNum++;
                // Retrieving by column name
                String name = rs.getString("name");

                // Retrieving by column index (1-based)
                int age = rs.getInt(2);

                // Check for null values
                if (rs.wasNull()) {
                    System.out.println("Row " + rowNum + ": age is NULL");
                } else {
                    System.out.println("Row " + rowNum + ": " + name + ", Age: " + age);
                }
            }
            System.out.println("Total rows: " + rowNum);

        } catch (SQLException e) {
            handleSQLException(e);
        }

        // =============================================
        // SCROLLABLE RESULTSET
        // =============================================
        System.out.println("\n=== Scrollable ResultSet ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            // Move to last row
            if (rs.last()) {
                System.out.println("Last row: " + rs.getString("name"));
                System.out.println("Row number: " + rs.getRow());
            }

            // Move to first row
            if (rs.first()) {
                System.out.println("First row: " + rs.getString("name"));
            }

            // Move to absolute position
            if (rs.absolute(3)) {
                System.out.println("Row at position 3: " + rs.getString("name"));
            }

            // Move relative
            if (rs.relative(-1)) {
                System.out.println("One row before current: " + rs.getString("name"));
            }

            // Check if before/after first/last
            System.out.println("Is before first? " + rs.isBeforeFirst());
            System.out.println("Is after last? " + rs.isAfterLast());

        } catch (SQLException e) {
            handleSQLException(e);
        }

        // =============================================
        // JDBC EXCEPTION HANDLING
        // =============================================
        System.out.println("\n=== JDBC Exception Handling ===\n");

        // 1. Basic SQLException Handling
        System.out.println("--- 1. Basic SQLException ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT * FROM nonexistent_table");
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Next Exception: " + e.getNextException());
        }

        // 2. SQLException Chaining
        System.out.println("\n--- 2. SQLException with Chained Exceptions ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO invalid_column VALUES (1)");
        } catch (SQLException e) {
            SQLException chained = e;
            int count = 0;
            while (chained != null) {
                count++;
                System.err.println("Exception #" + count + ":");
                System.err.println("  SQLState: " + chained.getSQLState());
                System.err.println("  ErrorCode: " + chained.getErrorCode());
                System.err.println("  Message: " + chained.getMessage());
                chained = chained.getNextException();
            }
        }

        // 3. SQLWarning (Non-fatal)
        System.out.println("\n--- 3. SQLWarning Handling ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT * FROM students");
            SQLWarning warning = stmt.getWarnings();
            if (warning != null) {
                System.err.println("Warning: " + warning.getMessage());
            } else {
                System.out.println("No warnings.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        // 4. Exception Type Checking
        System.out.println("\n--- 4. Exception Type Checking ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT * FROM students WHERE bad_column = 1");
        } catch (java.sql.SQLSyntaxErrorException e) {
            System.err.println("Syntax Error: " + e.getMessage());
        } catch (java.sql.SQLDataException e) {
            System.err.println("Data Error: " + e.getMessage());
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.err.println("Constraint Violation: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("General SQL Error: " + e.getMessage());
        }

        // 5. BatchUpdateException
        System.out.println("\n--- 5. BatchUpdateException ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.addBatch("INSERT INTO students (name, age) VALUES ('Test1', 20)");
            stmt.addBatch("INSERT INTO students (name, age) VALUES ('Test2', 21)");
            stmt.addBatch("INSERT INTO nonexistent_table VALUES (1)");
            stmt.executeBatch();
        } catch (BatchUpdateException e) {
            System.err.println("Batch Error: " + e.getMessage());
            System.err.println("Update counts: ");
            int[] counts = e.getUpdateCounts();
            for (int i = 0; i < counts.length; i++) {
                System.err.println("  Statement " + (i + 1) + ": " + counts[i]);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        // 6. Retry Logic for Transient Exceptions
        System.out.println("\n--- 6. Retry Logic Pattern ---");
        executeWithRetry(
                "SELECT * FROM students",
                3, // max retries
                1000 // delay ms
        );
    }

    // Helper method for consistent exception handling
    private static void handleSQLException(SQLException e) {
        System.err.println("=== SQL Exception Handler ===");
        System.err.println("SQLState:    " + e.getSQLState());
        System.err.println("Error Code:  " + e.getErrorCode());
        System.err.println("Message:     " + e.getMessage());
        System.err.println("Vendor Error: " + e.getLocalizedMessage());
    }

    // Retry pattern for transient exceptions
    private static void executeWithRetry(String query, int maxRetries, long delayMs) {
        int attempt = 0;
        while (attempt < maxRetries) {
            attempt++;
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("Query succeeded on attempt " + attempt);
                while (rs.next()) {
                    System.out.println("  " + rs.getString("name"));
                }
                return;
            } catch (SQLException e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (e instanceof SQLTransientException && attempt < maxRetries) {
                    System.out.println("Retrying in " + delayMs + "ms...");
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    System.err.println("Non-retryable or max retries reached.");
                    return;
                }
            }
        }
    }
}
