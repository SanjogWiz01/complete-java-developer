import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchProcessing {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        BatchProcessing batch = new BatchProcessing();
        batch.executeBatchInsert();
        batch.executeBatchUpdate();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void executeBatchInsert() {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            long startTime = System.currentTimeMillis();

            for (int i = 1; i <= 1000; i++) {
                pstmt.setString(1, "User_" + i);
                pstmt.setString(2, "user" + i + "@example.com");
                pstmt.setInt(3, 20 + (i % 50));
                pstmt.addBatch();

                if (i % 100 == 0) {
                    pstmt.executeBatch();
                }
            }

            pstmt.executeBatch();
            conn.commit();

            long endTime = System.currentTimeMillis();
            System.out.println("Batch insert of 1000 records completed in "
                    + (endTime - startTime) + "ms");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeBatchUpdate() {
        String sql = "UPDATE users SET age = age + 1 WHERE name LIKE 'User_%'";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            int[] results = stmt.executeBatch(sql);
            conn.commit();

            System.out.println("Batch update completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
