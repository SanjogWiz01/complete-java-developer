import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparedStatementCrudDemo {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();

        try (Connection connection = DriverManager.getConnection(
                config.url(),
                config.user(),
                config.password())) {

            createStudentsTable(connection);
            insertStudent(connection, 1, "Anita Sharma", "anita@example.com");
            insertStudent(connection, 2, "Ravi Thapa", "ravi@example.com");

            System.out.println("After insert:");
            printStudents(connection);

            updateStudentEmail(connection, 2, "ravi.thapa@example.com");

            System.out.println("\nAfter update:");
            printStudents(connection);

            deleteStudent(connection, 1);

            System.out.println("\nAfter delete:");
            printStudents(connection);
        } catch (SQLException ex) {
            System.err.println("CRUD demo failed.");
            ex.printStackTrace();
        }
    }

    private static void createStudentsTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static void insertStudent(
            Connection connection,
            int id,
            String name,
            String email) throws SQLException {

        String sql = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, email);
            int rows = statement.executeUpdate();
            System.out.println("Inserted rows: " + rows);
        }
    }

    private static void updateStudentEmail(
            Connection connection,
            int id,
            String email) throws SQLException {

        String sql = "UPDATE students SET email = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setInt(2, id);
            int rows = statement.executeUpdate();
            System.out.println("Updated rows: " + rows);
        }
    }

    private static void deleteStudent(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            System.out.println("Deleted rows: " + rows);
        }
    }

    private static void printStudents(Connection connection) throws SQLException {
        String sql = "SELECT id, name, email FROM students ORDER BY id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.printf("%d | %s | %s%n", id, name, email);
            }
        }
    }

    private record DatabaseConfig(String url, String user, String password) {
        static DatabaseConfig fromEnvironment() {
            String url = valueOrDefault(
                    System.getenv("DB_URL"),
                    "jdbc:h2:mem:jdbc_summary;DB_CLOSE_DELAY=-1");
            String user = valueOrDefault(System.getenv("DB_USER"), "sa");
            String password = valueOrDefault(System.getenv("DB_PASSWORD"), "");
            return new DatabaseConfig(url, user, password);
        }

        private static String valueOrDefault(String value, String defaultValue) {
            return value == null || value.isBlank() ? defaultValue : value;
        }
    }
}

