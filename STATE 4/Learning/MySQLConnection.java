import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "learning_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private Connection connection;

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to MySQL database: " + DATABASE);
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from MySQL database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        MySQLConnection mysql = new MySQLConnection();
        try {
            Connection conn = mysql.connect();
            System.out.println("Connection valid: " + conn.isValid(5));
            System.out.println("Auto-commit: " + conn.getAutoCommit());
            System.out.println("Catalog: " + conn.getCatalog());
            mysql.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
