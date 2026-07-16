import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        ResultSetExample example = new ResultSetExample();
        example.scrollableResultSet();
        example.updatableResultSet();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void scrollableResultSet() {
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(
                 ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Scrollable ResultSet ---");

            if (rs.last()) {
                System.out.println("Last record: " + rs.getString("name"));
                System.out.println("Total rows: " + rs.getRow());
            }

            if (rs.first()) {
                System.out.println("First record: " + rs.getString("name"));
            }

            rs.afterLast();
            while (rs.previous()) {
                System.out.println("Record: " + rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatableResultSet() {
        String sql = "SELECT * FROM users WHERE name = 'Alice'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(
                 ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.first()) {
                System.out.println("Before update: " + rs.getString("name")
                        + " - " + rs.getString("email"));
                rs.updateString("email", "alice_updated@example.com");
                rs.updateRow();
                System.out.println("After update: " + rs.getString("name")
                        + " - " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void caseInsensitiveColumnAccess() {
        String sql = "SELECT id, name, email FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            var metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("--- Column Metadata ---");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": "
                        + metaData.getColumnName(i)
                        + " (" + metaData.getColumnTypeName(i) + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
