import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Enumeration;

/*
 * JDBC Driver Types and Configuration
 * 
 * There are 4 types of JDBC Drivers:
 * 
 * Type 1 - JDBC-ODBC Bridge Driver:
 *   - Uses ODBC driver to connect to the database
 *   - Translates JDBC calls to ODBC calls
 *   - Requires ODBC driver to be installed on client machine
 *   - Removed from Java 8 (deprecated since Java 8)
 *   - Performance: Slow (extra translation layer)
 * 
 * Type 2 - Native-API Driver (Partial Java Driver):
 *   - Uses database-specific native client library (e.g., Oracle OCI)
 *   - Converts JDBC calls to native database calls
 *   - Requires native library installed on client machine
 *   - Performance: Better than Type 1
 * 
 * Type 3 - Network Protocol Driver (Middleware Driver):
 *   - Uses middleware/application server to communicate with database
 *   - JDBC calls are sent to middleware, which translates to DB-specific calls
 *   - No native library needed on client
 *   - Performance: Moderate
 * 
 * Type 4 - Thin Driver (Pure Java Driver):
 *   - Directly converts JDBC calls to database-specific protocol
 *   - 100% Java, no native code or middleware needed
 *   - Most commonly used driver type
 *   - Examples: MySQL Connector/J, Oracle Thin Driver, PostgreSQL JDBC Driver
 *   - Performance: Best
 * 
 * Driver Configuration:
 *   - Driver registration: Class.forName() or auto-registration via SPI (Service Provider Interface)
 *   - Connection URL format: jdbc:<subprotocol>:<subname>
 *   - Properties: user, password, and driver-specific properties
 */

// 5.2 JDBC Driver Types and Configuration - Four driver types, connection properties, and driver capabilities
public class JDBCDriverTypes {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {

        // Listing all registered JDBC drivers
        System.out.println("=== Registered JDBC Drivers ===");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println("Driver: " + driver.getClass().getName());
            System.out.println("  Version: " + driver.getMajorVersion() + "." + driver.getMinorVersion());
        }

        // Type 4 Driver - MySQL Connector/J (Pure Java Driver)
        System.out.println("\n=== Type 4: MySQL Thin Driver (Pure Java) ===");
        try {
            // Modern JDBC (JDBC 4.0+) auto-loads drivers via SPI, Class.forName is optional
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded: com.mysql.cj.jdbc.Driver");
            System.out.println("Type: Pure Java (Type 4) - No native libraries needed");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL driver not found. Add mysql-connector-j to classpath.");
            e.printStackTrace();
        }

        // Establishing connection with Type 4 driver
        System.out.println("\n=== Connecting with Type 4 Driver ===");
        Connection connection = null;
        try {
            // Connection URL parameters for MySQL configuration
            String urlWithParams = MYSQL_URL
                    + "?useSSL=false"
                    + "&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true"
                    + "&characterEncoding=UTF-8"
                    + "&connectTimeout=5000"
                    + "&socketTimeout=5000";

            connection = DriverManager.getConnection(urlWithParams, USER, PASS);

            System.out.println("Connection successful!");
            System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Driver Version: " + connection.getMetaData().getDriverVersion());

        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }

        // Exploring driver metadata and capabilities
        System.out.println("\n=== Driver Capabilities ===");
        try {
            if (connection != null) {
                java.sql.DatabaseMetaData meta = connection.getMetaData();
                System.out.println("Supports Batch Updates: " + meta.supportsBatchUpdates());
                System.out.println("Supports Transactions: " + meta.supportsTransactions());
                System.out.println("Supports ResultSet TYPE_FORWARD_ONLY: "
                        + meta.supportsResultSetType(java.sql.ResultSet.TYPE_FORWARD_ONLY));
                System.out.println("Supports ResultSet TYPE_SCROLL_INSENSITIVE: "
                        + meta.supportsResultSetType(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE));
                System.out.println("Supports GENERATED_KEYS: "
                        + meta.supportsGetGeneratedKeys());
                System.out.println("Max Table Name Length: " + meta.getMaxTableNameLength());
                System.out.println("Max Column Name Length: " + meta.getMaxColumnNameLength());
                System.out.println("Nulls Sort High: " + meta.nullsAreSortedHigh());
                System.out.println("Nulls Sort Low: " + meta.nullsAreSortedLow());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Closing connection
        System.out.println("\n=== Closing Connection ===");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Summary of all driver types
        System.out.println("\n=== JDBC Driver Types Summary ===");
        System.out.println("Type 1: JDBC-ODBC Bridge    -> Uses ODBC, removed in Java 8");
        System.out.println("Type 2: Native-API          -> Uses native DB client library");
        System.out.println("Type 3: Network Protocol    -> Uses middleware server");
        System.out.println("Type 4: Thin/Pure Java      -> Direct protocol, best performance");
    }
}
