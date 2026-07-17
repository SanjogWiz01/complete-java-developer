import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/*
 * Row Sets and Transactions in JDBC
 * 
 * RowSet (javax.sql.rowset):
 *   - Extends ResultSet with additional features
 *   - Can be disconnected from the database (CachedRowSet)
 *   - Supports event listeners for data changes
 *   - Lightweight, serializable, can be passed over network
 * 
 * RowSet Types:
 *   1. CachedRowSet      - Disconnected, in-memory data, scrollable, updatable
 *   2. WebRowSet          - CachedRowSet + XML format support
 *   3. FilteredRowSet     - CachedRowSet + filtering capabilities
 *   4. JoinRowSet         - CachedRowSet + join capabilities
 *   5. JdbcRowSet         - Connected, auto-closes connection when done
 * 
 * Transactions:
 *   - A sequence of operations treated as a single unit of work
 *   - All operations succeed (COMMIT) or all fail (ROLLBACK)
 *   - ACID Properties:
 *     A - Atomicity: All operations complete or none do
 *     C - Consistency: Database moves from one valid state to another
 *     I - Isolation: Concurrent transactions don't interfere
 *     D - Durability: Committed data survives system failures
 * 
 * Transaction Isolation Levels:
 *   - TRANSACTION_NONE             - No isolation
 *   - TRANSACTION_READ_UNCOMMITTED - Dirty reads possible
 *   - TRANSACTION_READ_COMMITTED   - No dirty reads (most common)
 *   - TRANSACTION_REPEATABLE_READ  - No dirty/non-repeatable reads
 *   - TRANSACTION_SERIALIZABLE    - Full isolation (slowest)
 * 
 * Savepoints:
 *   - Allow partial rollback within a transaction
 *   - setSavepoint() creates a named or unnamed savepoint
 *   - rollback(savepoint) rolls back to that point
 *   - releaseSavepoint() removes the savepoint
 */

// 5.7 Row Sets and Transactions - CachedRowSet, commit/rollback, savepoints, isolation levels
public class RowSetsAndTransactions {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // Setup test table
        setupTestTable();

        // =============================================
        // ROW SETS
        // =============================================
        System.out.println("=== Row Sets ===\n");

        // 1. CachedRowSet (Disconnected)
        System.out.println("--- 1. CachedRowSet (Disconnected) ---");
        try {
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.setUrl(URL);
            cachedRowSet.setUsername(USER);
            cachedRowSet.setPassword(PASS);
            cachedRowSet.setCommand("SELECT * FROM bank_accounts");
            cachedRowSet.execute();

            System.out.println("Data loaded into CachedRowSet (connection closed).");
            System.out.println("Working with disconnected data:");
            while (cachedRowSet.next()) {
                System.out.println("  " + cachedRowSet.getString("owner")
                        + " - Balance: $" + cachedRowSet.getDouble("balance"));
            }

            // Modify data in CachedRowSet
            cachedRowSet.beforeFirst();
            if (cachedRowSet.next()) {
                double newBalance = cachedRowSet.getDouble("balance") + 500;
                cachedRowSet.updateDouble("balance", newBalance);
                cachedRowSet.updateRow();
                System.out.println("\nUpdated first account balance in CachedRowSet.");
            }

            // Reconnect and accept changes
            cachedRowSet.acceptChanges();
            System.out.println("Changes propagated to database.");

            cachedRowSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. CachedRowSet with Filter
        System.out.println("\n--- 2. CachedRowSet with Predicate (Filtering) ---");
        try {
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.setUrl(URL);
            cachedRowSet.setUsername(USER);
            cachedRowSet.setPassword(PASS);
            cachedRowSet.setCommand("SELECT * FROM bank_accounts");
            cachedRowSet.execute();

            // Filter using RowSet filter (programmatic filtering)
            System.out.println("Accounts with balance > $1000:");
            while (cachedRowSet.next()) {
                if (cachedRowSet.getDouble("balance") > 1000) {
                    System.out.println("  " + cachedRowSet.getString("owner")
                            + " - Balance: $" + cachedRowSet.getDouble("balance"));
                }
            }
            cachedRowSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // =============================================
        // TRANSACTIONS
        // =============================================
        System.out.println("\n=== Transactions ===\n");

        // 1. Basic Transaction - Commit
        System.out.println("--- 1. Basic Transaction (COMMIT) ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false); // Start transaction

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance - 200 WHERE owner = 'Alice'");
            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance + 200 WHERE owner = 'Bob'");
            stmt.close();

            conn.commit(); // Commit transaction
            System.out.println("Transaction committed: Alice sent $200 to Bob.");
            printBalances(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Basic Transaction - Rollback
        System.out.println("\n--- 2. Basic Transaction (ROLLBACK) ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance - 500 WHERE owner = 'Alice'");
            System.out.println("Alice's balance reduced by $500 (pending).");

            // Oops! Something went wrong, rollback
            conn.rollback();
            System.out.println("Transaction rolled back!");
            stmt.close();
            printBalances(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. Savepoints
        System.out.println("\n--- 3. Savepoints (Partial Rollback) ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance - 100 WHERE owner = 'Alice'");
            System.out.println("Step 1: Alice - $100");

            java.sql.Savepoint sp1 = conn.setSavepoint("after_alice");
            System.out.println("Savepoint 'after_alice' created.");

            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance - 100 WHERE owner = 'Bob'");
            System.out.println("Step 2: Bob - $100");

            java.sql.Savepoint sp2 = conn.setSavepoint("after_bob");
            System.out.println("Savepoint 'after_bob' created.");

            // Rollback to savepoint1 (undo Bob's change, keep Alice's)
            conn.rollback(sp1);
            System.out.println("\nRolled back to 'after_alice' (Bob's change undone).");
            stmt.close();

            conn.commit();
            System.out.println("Transaction committed with only Alice's change.");
            printBalances(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 4. Transaction Isolation Levels
        System.out.println("\n--- 4. Transaction Isolation Levels ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Default Isolation: " + conn.getTransactionIsolation());
            System.out.println("  TRANSACTION_NONE = " + Connection.TRANSACTION_NONE);
            System.out.println("  TRANSACTION_READ_UNCOMMITTED = " + Connection.TRANSACTION_READ_UNCOMMITTED);
            System.out.println("  TRANSACTION_READ_COMMITTED = " + Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("  TRANSACTION_REPEATABLE_READ = " + Connection.TRANSACTION_REPEATABLE_READ);
            System.out.println("  TRANSACTION_SERIALIZABLE = " + Connection.TRANSACTION_SERIALIZABLE);

            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("Set to SERIALIZABLE: " + conn.getTransactionIsolation());

            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("Reset to READ_COMMITTED: " + conn.getTransactionIsolation());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 5. Auto-commit behavior
        System.out.println("\n--- 5. Auto-Commit Behavior ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Default auto-commit: " + conn.getAutoCommit());

            conn.setAutoCommit(false);
            System.out.println("Auto-commit OFF: " + conn.getAutoCommit());

            // Must explicitly commit
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE bank_accounts SET balance = balance + 0 WHERE owner = 'Alice'");
            conn.commit();
            System.out.println("Manual commit done.");
            stmt.close();

            conn.setAutoCommit(true);
            System.out.println("Auto-commit back ON: " + conn.getAutoCommit());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cleanup
        cleanupTestTable();
    }

    private static void setupTestTable() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS bank_accounts");
            stmt.executeUpdate("CREATE TABLE bank_accounts ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "owner VARCHAR(50) NOT NULL, "
                    + "balance DOUBLE DEFAULT 0.0"
                    + ")");
            stmt.executeUpdate("INSERT INTO bank_accounts (owner, balance) VALUES "
                    + "('Alice', 1500.00), ('Bob', 2000.00), ('Charlie', 500.00)");
            System.out.println("Test table 'bank_accounts' created with initial balances.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printBalances(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM bank_accounts");
        System.out.println("Current Balances:");
        while (rs.next()) {
            System.out.println("  " + rs.getString("owner") + ": $" + rs.getDouble("balance"));
        }
        rs.close();
        stmt.close();
        System.out.println();
    }

    private static void cleanupTestTable() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS bank_accounts");
            System.out.println("Cleanup: 'bank_accounts' table dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
