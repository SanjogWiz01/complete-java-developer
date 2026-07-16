import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransientException;
import java.sql.SQLRecoverableException;

public class ExceptionHandlingExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        ExceptionHandlingExample example = new ExceptionHandlingExample();
        example.handleSQLException();
        example.handleBatchException();
        example.reconnectOnFailure();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void handleSQLException() {
        String sql = "INSERT INTO nonexistent_table (col1) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "test");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());

            SQLException next = e.getNextException();
            while (next != null) {
                System.out.println("Next exception: " + next.getMessage());
                next = next.getNextException();
            }
        }
    }

    public void handleBatchException() {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            pstmt.setString(1, "BatchUser1");
            pstmt.setString(2, "batch1@example.com");
            pstmt.setInt(3, 25);
            pstmt.addBatch();

            pstmt.setString(1, null);
            pstmt.setString(2, "batch2@example.com");
            pstmt.setInt(3, 30);
            pstmt.addBatch();

            pstmt.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            System.out.println("Batch failed: " + e.getMessage());
        }
    }

    public void reconnectOnFailure() {
        int maxRetries = 3;
        int retryCount = 0;
        boolean connected = false;

        while (!connected && retryCount < maxRetries) {
            try (Connection conn = getConnection()) {
                System.out.println("Connected successfully on attempt " + (retryCount + 1));
                connected = true;
            } catch (SQLRecoverableException e) {
                retryCount++;
                System.out.println("Recoverable error, retrying... Attempt " + retryCount);
                try { Thread.sleep(1000); } catch (InterruptedException ie) { break; }
            } catch (SQLTransientException e) {
                retryCount++;
                System.out.println("Transient error, retrying... Attempt " + retryCount);
                try { Thread.sleep(2000); } catch (InterruptedException ie) { break; }
            } catch (SQLException e) {
                System.out.println("Non-recoverable error: " + e.getMessage());
                break;
            }
        }

        if (!connected) {
            System.out.println("Failed to connect after " + maxRetries + " attempts.");
        }
    }
}
