import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/*
 * JDBC Architecture
 * 
 * JDBC (Java Database Connectivity) follows a two-tier and three-tier architecture model.
 * 
 * Two-Tier Architecture:
 *   - Java Application <---> JDBC Driver <---> Database
 *   - The application directly communicates with the database through the JDBC driver.
 * 
 * Three-Tier Architecture:
 *   - Java Application <---> Middleware (Business Logic) <---> JDBC Driver <---> Database
 *   - A middleware server sits between the application and the database.
 * 
 * Key Components of JDBC Architecture:
 *   1. JDBC API        - Provides interfaces (Connection, Statement, ResultSet, etc.)
 *   2. JDBC Driver Manager - Manages a list of database drivers and dispatches connections
 *   3. JDBC Drivers    - Translates JDBC calls into database-specific protocol calls
 *   4. Database        - The actual data store (MySQL, Oracle, PostgreSQL, etc.)
 * 
 * JDBC Workflow:
 *   Step 1: Load the JDBC driver (Class.forName)
 *   Step 2: Establish a connection (DriverManager.getConnection)
 *   Step 3: Create a statement (conn.createStatement)
 *   Step 4: Execute a query (stmt.executeQuery)
 *   Step 5: Process the ResultSet
 *   Step 6: Close the connection
 */

// 5.1 JDBC Architecture - Understanding the two-tier and three-tier models, JDBC workflow, and metadata
public class JDBCArchitecture {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // Step 1: Loading the JDBC Driver
        // This registers the driver with the DriverManager
        System.out.println("=== Step 1: Loading JDBC Driver ===");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: JDBC Driver not found!");
            e.printStackTrace();
            return;
        }

        // Step 2: Establishing a Connection
        // DriverManager looks up the appropriate driver and creates a Connection object
        System.out.println("\n=== Step 2: Establishing Connection ===");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection established successfully!");
            System.out.println("Connected to: " + connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to database!");
            e.printStackTrace();
            return;
        }

        // Step 3 & 4: Creating Statement and Executing Query
        System.out.println("\n=== Step 3 & 4: Statement and Query Execution ===");
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM students";
            resultSet = statement.executeQuery(query);
            System.out.println("Query executed successfully.");

            // Step 5: Processing ResultSet
            System.out.println("\n=== Step 5: Processing ResultSet ===");
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("Row " + rowCount + ": Name = " + name + ", Age = " + age);
            }
            System.out.println("Total rows fetched: " + rowCount);

        } catch (SQLException e) {
            System.err.println("ERROR during query execution!");
            e.printStackTrace();
        }

        // Exploring Database Metadata (part of JDBC Architecture)
        System.out.println("\n=== Database Metadata (Architecture Info) ===");
        try {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("Driver Name: " + meta.getDriverName());
            System.out.println("Driver Version: " + meta.getDriverVersion());
            System.out.println("Database Product: " + meta.getDatabaseProductName());
            System.out.println("Database Version: " + meta.getDatabaseProductVersion());
            System.out.println("JDBC Version: " + meta.getJDBCMajorVersion() + "." + meta.getJDBCMinorVersion());
            System.out.println("Supports Transactions: " + meta.supportsTransactions());
            System.out.println("Supports Batch Updates: " + meta.supportsBatchUpdates());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Step 6: Closing Resources
        System.out.println("\n=== Step 6: Closing Resources ===");
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            System.out.println("All resources closed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
