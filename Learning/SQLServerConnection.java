import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLServerConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 1433;
    private static final String DATABASE = "learning_db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "YourPassword123";

    private static final String URL = "jdbc:sqlserver://" + HOST + ":" + PORT
            + ";databaseName=" + DATABASE
            + ";encrypt=false;trustServerCertificate=true";

    private Connection connection;

    public SQLServerConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("SQL Server JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("encrypt", "false");
            props.setProperty("trustServerCertificate", "true");
            props.setProperty("loginTimeout", "10");
            props.setProperty("socketTimeout", "30");

            connection = DriverManager.getConnection(URL, props);
            System.out.println("Connected to SQL Server database: " + DATABASE);
        }
        return connection;
    }

    public Connection connectWithInstance(String instanceName) throws SQLException {
        String instanceUrl = "jdbc:sqlserver://" + HOST + "\\" + instanceName
                + ":" + PORT + ";databaseName=" + DATABASE
                + ";encrypt=false;trustServerCertificate=true";

        Connection instanceConn = DriverManager.getConnection(instanceUrl, USERNAME, PASSWORD);
        System.out.println("Connected to SQL Server instance: " + instanceName);
        return instanceConn;
    }

    public Connection connectToMaster() throws SQLException {
        String masterUrl = "jdbc:sqlserver://" + HOST + ":" + PORT
                + ";databaseName=master;encrypt=false;trustServerCertificate=true";

        Connection masterConn = DriverManager.getConnection(masterUrl, USERNAME, PASSWORD);
        System.out.println("Connected to SQL Server master database.");
        return masterConn;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from SQL Server database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean testConnection() {
        try (Connection testConn = connect()) {
            return testConn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        SQLServerConnection sqlserver = new SQLServerConnection();
        try {
            Connection conn = sqlserver.connect();
            System.out.println("Connection valid: " + conn.isValid(5));
            System.out.println("Auto-commit: " + conn.getAutoCommit());
            System.out.println("Catalog: " + conn.getCatalog());
            sqlserver.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
