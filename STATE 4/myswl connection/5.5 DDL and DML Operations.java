import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * DDL and DML Operations in JDBC
 * 
 * DDL (Data Definition Language) - Defines database structure:
 *   - CREATE TABLE / DATABASE / INDEX / VIEW
 *   - ALTER TABLE (ADD, DROP, MODIFY columns)
 *   - DROP TABLE / DATABASE / INDEX
 *   - TRUNCATE TABLE
 *   - RENAME TABLE
 * 
 * DML (Data Manipulation Language) - Manipulates data:
 *   - INSERT INTO (add new rows)
 *   - UPDATE (modify existing rows)
 *   - DELETE (remove rows)
 *   - INSERT ... SELECT (copy data between tables)
 * 
 * DCL (Data Control Language) - Controls access:
 *   - GRANT / REVOKE permissions
 * 
 * TCL (Transaction Control Language):
 *   - COMMIT / ROLLBACK / SAVEPOINT
 * 
 * Statement Methods:
 *   - executeQuery(String sql)  -> Returns ResultSet (for SELECT)
 *   - executeUpdate(String sql) -> Returns int (rows affected) (for INSERT, UPDATE, DELETE, DDL)
 *   - execute(String sql)       -> Returns boolean (true if ResultSet, false otherwise)
 *   - executeBatch()            -> Returns int[] (for batch operations)
 *   - addBatch(String sql)      -> Adds SQL to batch
 */

// 5.5 DDL and DML Operations - CREATE/ALTER/DROP, INSERT/UPDATE/DELETE, batch processing
public class DDLandDMLOperations {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // =============================================
            // DDL OPERATIONS
            // =============================================
            System.out.println("=== DDL Operations ===\n");

            // 1. CREATE TABLE
            System.out.println("--- 1. CREATE TABLE ---");
            String createTable = "CREATE TABLE IF NOT EXISTS employees ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "first_name VARCHAR(50) NOT NULL, "
                    + "last_name VARCHAR(50) NOT NULL, "
                    + "email VARCHAR(100) UNIQUE, "
                    + "salary DOUBLE DEFAULT 0.0, "
                    + "department VARCHAR(50), "
                    + "hire_date DATE, "
                    + "is_active BOOLEAN DEFAULT TRUE"
                    + ")";
            int result = stmt.executeUpdate(createTable);
            System.out.println("Table 'employees' created. (executeUpdate returned: " + result + ")");

            // 2. ALTER TABLE
            System.out.println("\n--- 2. ALTER TABLE ---");
            String addColumn = "ALTER TABLE employees ADD COLUMN phone VARCHAR(20)";
            stmt.executeUpdate(addColumn);
            System.out.println("Column 'phone' added to employees table.");

            String modifyColumn = "ALTER TABLE employees MODIFY COLUMN email VARCHAR(150)";
            stmt.executeUpdate(modifyColumn);
            System.out.println("Column 'email' modified to VARCHAR(150).");

            // 3. CREATE INDEX
            System.out.println("\n--- 3. CREATE INDEX ---");
            String createIndex = "CREATE INDEX IF NOT EXISTS idx_emp_dept ON employees(department)";
            stmt.executeUpdate(createIndex);
            System.out.println("Index 'idx_emp_dept' created on department column.");

            // 4. DROP TABLE (create first then drop for demo)
            System.out.println("\n--- 4. DROP TABLE ---");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS temp_table (id INT)");
            stmt.executeUpdate("DROP TABLE IF EXISTS temp_table");
            System.out.println("Table 'temp_table' dropped.");

            // 5. TRUNCATE TABLE
            System.out.println("\n--- 5. TRUNCATE TABLE ---");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS truncate_test (id INT)");
            stmt.executeUpdate("TRUNCATE TABLE truncate_test");
            stmt.executeUpdate("DROP TABLE IF EXISTS truncate_test");
            System.out.println("Table 'truncate_test' truncated and dropped.");

            // =============================================
            // DML OPERATIONS
            // =============================================
            System.out.println("\n=== DML Operations ===\n");

            // 1. INSERT - Single Row
            System.out.println("--- 1. INSERT Single Row ---");
            String insertSingle = "INSERT INTO employees (first_name, last_name, email, salary, department, hire_date) "
                    + "VALUES ('John', 'Doe', 'john.doe@company.com', 75000.00, 'Engineering', '2024-01-15')";
            int rowsInserted = stmt.executeUpdate(insertSingle);
            System.out.println("Rows inserted: " + rowsInserted);

            // 2. INSERT - Multiple Rows (using batch)
            System.out.println("\n--- 2. INSERT Multiple Rows (Batch) ---");
            stmt.addBatch("INSERT INTO employees (first_name, last_name, email, salary, department, hire_date) "
                    + "VALUES ('Jane', 'Smith', 'jane.smith@company.com', 82000.00, 'Marketing', '2024-02-20')");
            stmt.addBatch("INSERT INTO employees (first_name, last_name, email, salary, department, hire_date) "
                    + "VALUES ('Bob', 'Johnson', 'bob.j@company.com', 68000.00, 'Engineering', '2024-03-10')");
            stmt.addBatch("INSERT INTO employees (first_name, last_name, email, salary, department, hire_date) "
                    + "VALUES ('Alice', 'Williams', 'alice.w@company.com', 91000.00, 'HR', '2024-04-05')");
            int[] batchResults = stmt.executeBatch();
            System.out.println("Batch insert completed. Rows affected: " + java.util.Arrays.toString(batchResults));

            // 3. UPDATE
            System.out.println("\n--- 3. UPDATE ---");
            String update = "UPDATE employees SET salary = salary * 1.10 WHERE department = 'Engineering'";
            int rowsUpdated = stmt.executeUpdate(update);
            System.out.println("Rows updated (10% raise for Engineering): " + rowsUpdated);

            // 4. DELETE
            System.out.println("\n--- 4. DELETE ---");
            String delete = "DELETE FROM employees WHERE email = 'bob.j@company.com'";
            int rowsDeleted = stmt.executeUpdate(delete);
            System.out.println("Rows deleted: " + rowsDeleted);

            // 5. SELECT to verify
            System.out.println("\n--- 5. Verify with SELECT ---");
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            System.out.println("Current employees:");
            while (rs.next()) {
                System.out.println("  " + rs.getString("first_name") + " "
                        + rs.getString("last_name") + " - $"
                        + rs.getDouble("salary") + " ("
                        + rs.getString("department") + ")");
            }
            rs.close();

            // =============================================
            // METADATA OPERATIONS
            // =============================================
            System.out.println("\n=== Table Metadata ===");
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("\nColumns in 'employees':");
            ResultSet columns = meta.getColumns(null, null, "employees", null);
            while (columns.next()) {
                System.out.println("  " + columns.getString("COLUMN_NAME")
                        + " - " + columns.getString("TYPE_NAME")
                        + " (" + columns.getInt("COLUMN_SIZE") + ")");
            }
            columns.close();

            System.out.println("\nIndexes on 'employees':");
            ResultSet indexes = meta.getIndexInfo(null, null, "employees", false, false);
            while (indexes.next()) {
                System.out.println("  " + indexes.getString("INDEX_NAME")
                        + " on " + indexes.getString("COLUMN_NAME")
                        + (indexes.getBoolean("NON_UNIQUE") ? "" : " (unique)"));
            }
            indexes.close();

            // Cleanup
            stmt.executeUpdate("DROP TABLE IF EXISTS employees");
            System.out.println("\nCleanup: 'employees' table dropped.");

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
