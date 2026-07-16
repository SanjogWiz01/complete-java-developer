import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdvancedQueryExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        AdvancedQueryExample example = new AdvancedQueryExample();
        example.joinQuery();
        example.aggregateQuery();
        example.subQuery();
        example.paginationQuery(0, 10);
        example.dynamicQuery("name", "Alice");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void joinQuery() {
        String sql = "SELECT u.name, u.email, o.order_id "
                + "FROM users u INNER JOIN orders o ON u.id = o.user_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Join Query Results ---");
            while (rs.next()) {
                System.out.println(rs.getString("name")
                        + " | " + rs.getString("email")
                        + " | Order: " + rs.getInt("order_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aggregateQuery() {
        String sql = "SELECT COUNT(*) as total, "
                + "AVG(age) as avg_age, "
                + "MIN(age) as min_age, "
                + "MAX(age) as max_age "
                + "FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("--- Aggregate Results ---");
                System.out.println("Total Users: " + rs.getInt("total"));
                System.out.println("Average Age: " + rs.getDouble("avg_age"));
                System.out.println("Min Age: " + rs.getInt("min_age"));
                System.out.println("Max Age: " + rs.getInt("max_age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void subQuery() {
        String sql = "SELECT name, email FROM users "
                + "WHERE age > (SELECT AVG(age) FROM users)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Users Above Average Age ---");
            while (rs.next()) {
                System.out.println(rs.getString("name")
                        + " - " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void paginationQuery(int offset, int limit) {
        String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("--- Page Results (offset=" + offset + ", limit=" + limit + ") ---");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dynamicQuery(String column, String value) {
        String sql = "SELECT * FROM users WHERE " + column + " = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("--- Dynamic Query Results (" + column + "=" + value + ") ---");
            while (rs.next()) {
                System.out.println(rs.getString("name")
                        + " - " + rs.getString("email")
                        + " - Age: " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
