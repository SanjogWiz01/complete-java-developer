import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 1521;
    private static final String SERVICE_NAME = "ORCL";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "password";

    private static final String URL = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SERVICE_NAME;

    private Connection connection;

    public OracleConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("oracle.jdbc.ReadTimeout", "30000");
            props.setProperty("oracle.net.CONNECT_TIMEOUT", "10000");

            connection = DriverManager.getConnection(URL, props);
            System.out.println("Connected to Oracle database: " + SERVICE_NAME);
        }
        return connection;
    }

    public Connection connectThin() throws SQLException {
        String thinUrl = "jdbc:oracle:thin:@//" + HOST + ":" + PORT + "/" + SERVICE_NAME;
        Connection thinConn = DriverManager.getConnection(thinUrl, USERNAME, PASSWORD);
        System.out.println("Connected to Oracle using thin driver.");
        return thinConn;
    }

    public Connection connectOCI() throws SQLException {
        String ociUrl = "jdbc:oracle:oci:@" + SERVICE_NAME;
        Connection ociConn = DriverManager.getConnection(ociUrl, USERNAME, PASSWORD);
        System.out.println("Connected to Oracle using OCI driver.");
        return ociConn;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from Oracle database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(autoCommit);
                System.out.println("Auto-commit set to: " + autoCommit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        OracleConnection oracle = new OracleConnection();
        try {
            Connection conn = oracle.connect();
            System.out.println("Connection valid: " + conn.isValid(5));
            System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
            oracle.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
