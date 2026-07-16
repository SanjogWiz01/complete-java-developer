import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionManagement {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        TransactionManagement tm = new TransactionManagement();
        tm.transferFunds(1, 2, 500);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void transferFunds(int fromUserId, int toUserId, double amount) {
        String debitSQL = "UPDATE users SET age = age - ? WHERE id = ?";
        String creditSQL = "UPDATE users SET age = age + ? WHERE id = ?";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement debitPstmt = conn.prepareStatement(debitSQL);
                 PreparedStatement creditPstmt = conn.prepareStatement(creditSQL)) {

                debitPstmt.setDouble(1, amount);
                debitPstmt.setInt(2, fromUserId);
                debitPstmt.executeUpdate();

                creditPstmt.setDouble(1, amount);
                creditPstmt.setInt(2, toUserId);
                creditPstmt.executeUpdate();

                conn.commit();
                System.out.println("Transaction completed successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction rolled back due to error.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savepointExample() {
        String sql1 = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                 PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

                pstmt1.setString(1, "SavepointUser1");
                pstmt1.setString(2, "sp1@example.com");
                pstmt1.setInt(3, 30);
                pstmt1.executeUpdate();

                var savepoint = conn.setSavepoint("afterFirstInsert");

                pstmt2.setString(1, "SavepointUser2");
                pstmt2.setString(2, "sp2@example.com");
                pstmt2.setInt(3, 25);
                pstmt2.executeUpdate();

                conn.commit();
                System.out.println("Both inserts committed.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rolled back to savepoint.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
