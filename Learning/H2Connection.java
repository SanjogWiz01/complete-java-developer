import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {

    private static final String IN_MEMORY_URL = "jdbc:h2:mem:learning_db;DB_CLOSE_DELAY=-1";
    private static final String FILE_URL = "jdbc:h2:./data/learning_db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private Connection connection;

    public H2Connection() {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("H2 JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public Connection connectInMemory() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(IN_MEMORY_URL, USERNAME, PASSWORD);
            System.out.println("Connected to H2 in-memory database.");
        }
        return connection;
    }

    public Connection connectToFile() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(FILE_URL, USERNAME, PASSWORD);
            System.out.println("Connected to H2 file-based database.");
        }
        return connection;
    }

    public Connection connectToServer(String serverUrl, int port) throws SQLException {
        String serverDbUrl = "jdbc:h2:tcp://" + serverUrl + ":" + port + "/learning_db";
        Connection serverConn = DriverManager.getConnection(serverDbUrl, USERNAME, PASSWORD);
        System.out.println("Connected to H2 server at " + serverUrl + ":" + port);
        return serverConn;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from H2 database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void runInitScript(String scriptPath) {
        try (Connection conn = connectInMemory();
             var stmt = conn.createStatement()) {
            stmt.execute("RUNSCRIPT FROM '" + scriptPath + "'");
            System.out.println("Init script executed: " + scriptPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTestTable() throws SQLException {
        Connection conn = connectInMemory();
        var stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS test_table ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100), "
                + "value INT"
                + ")");
        System.out.println("Test table created in H2 database.");
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        H2Connection h2 = new H2Connection();
        try {
            Connection conn = h2.connectInMemory();
            System.out.println("Connection valid: " + conn.isValid(5));

            h2.createTestTable();

            var pstmt = conn.prepareStatement("INSERT INTO test_table (name, value) VALUES (?, ?)");
            pstmt.setString(1, "H2Test");
            pstmt.setInt(2, 100);
            pstmt.executeUpdate();

            var rs = conn.createStatement().executeQuery("SELECT * FROM test_table");
            while (rs.next()) {
                System.out.println("Record: " + rs.getString("name")
                        + " - Value: " + rs.getInt("value"));
            }

            h2.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
