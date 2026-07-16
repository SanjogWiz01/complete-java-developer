import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgreSQLConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 5432;
    private static final String DATABASE = "learning_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;

    private Connection connection;

    public PostgreSQLConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("ssl", "false");
            props.setProperty("connectTimeout", "10");
            props.setProperty("socketTimeout", "30");

            connection = DriverManager.getConnection(URL, props);
            System.out.println("Connected to PostgreSQL database: " + DATABASE);
        }
        return connection;
    }

    public Connection connectWithSchema(String schema) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
        props.setProperty("currentSchema", schema);

        Connection schemaConn = DriverManager.getConnection(URL, props);
        System.out.println("Connected to PostgreSQL schema: " + schema);
        return schemaConn;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from PostgreSQL database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            return testConn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        PostgreSQLConnection postgres = new PostgreSQLConnection();
        try {
            Connection conn = postgres.connect();
            System.out.println("Connection valid: " + conn.isValid(5));
            System.out.println("Transaction isolation: " + conn.getTransactionIsolation());
            System.out.println("Catalog: " + conn.getCatalog());
            postgres.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
