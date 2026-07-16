import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        PreparedStatementExample example = new PreparedStatementExample();
        example.batchInsertWithPreparedStatement();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void batchInsertWithPreparedStatement() {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            String[][] users = {
                {"Charlie", "charlie@example.com", "22"},
                {"Diana", "diana@example.com", "31"},
                {"Edward", "edward@example.com", "45"}
            };

            for (String[] user : users) {
                pstmt.setString(1, user[0]);
                pstmt.setString(2, user[1]);
                pstmt.setInt(3, Integer.parseInt(user[2]));
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit();

            System.out.println("Batch insert completed. Rows affected: " + results.length);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchByName(String namePattern) {
        String sql = "SELECT * FROM users WHERE name LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + namePattern + "%");
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name") + " - " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
