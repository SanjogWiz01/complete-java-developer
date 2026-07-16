import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CallableStatementExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        CallableStatementExample example = new CallableStatementExample();
        example.callSimpleProcedure();
        example.callProcedureWithParameters();
        example.callFunction();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void callSimpleProcedure() {
        String sql = "{CALL GetAllUsers()}";
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            System.out.println("--- All Users from Procedure ---");
            while (rs.next()) {
                System.out.println(rs.getString("name")
                        + " - " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void callProcedureWithParameters() {
        String sql = "{CALL GetUserById(?, ?)}";
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, 1);
            cstmt.registerOutParameter(2, Types.VARCHAR);

            cstmt.execute();
            String userName = cstmt.getString(2);
            System.out.println("User from procedure: " + userName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void callFunction() {
        String sql = "{? = CALL GetUserCount()}";
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();

            int count = cstmt.getInt(1);
            System.out.println("Total users: " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
