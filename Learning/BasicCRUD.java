import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasicCRUD {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        BasicCRUD crud = new BasicCRUD();

        crud.createTable();
        crud.insertRecord("Alice", "alice@example.com", 28);
        crud.insertRecord("Bob", "bob@example.com", 35);
        crud.readAllRecords();
        crud.updateRecord(1, "alice_new@example.com");
        crud.deleteRecord(2);
        crud.readAllRecords();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "email VARCHAR(150) NOT NULL, "
                + "age INT"
                + ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRecord(String name, String email, int age) {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            System.out.println("Record inserted: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readAllRecords() {
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("--- All Users ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id")
                        + ", Name: " + rs.getString("name")
                        + ", Email: " + rs.getString("email")
                        + ", Age: " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(int id, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
