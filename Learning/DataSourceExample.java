import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static DataSourceExample instance;
    private static Connection sharedConnection;

    private DataSourceExample() {}

    public static synchronized DataSourceExample getInstance() {
        if (instance == null) {
            instance = new DataSourceExample();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        if (sharedConnection == null || sharedConnection.isClosed()) {
            sharedConnection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return sharedConnection;
    }

    public void initialize() {
        try {
            Connection conn = getConnection();
            System.out.println("DataSource initialized. Connection valid: " + conn.isValid(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(String name, String email, int age) {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            System.out.println("User inserted: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listUsers() {
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("name") + " - " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (sharedConnection != null && !sharedConnection.isClosed()) {
                sharedConnection.close();
                System.out.println("DataSource connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataSourceExample ds = DataSourceExample.getInstance();
        ds.initialize();
        ds.insertUser("DataSourceUser1", "ds1@example.com", 29);
        ds.listUsers();
        ds.close();
    }
}
