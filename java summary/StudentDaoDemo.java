import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoDemo {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();

        try (Connection connection = DriverManager.getConnection(
                config.url(),
                config.user(),
                config.password())) {

            StudentDao studentDao = new StudentDao(connection);
            studentDao.createTable();
            studentDao.deleteAll();

            studentDao.save(new Student(1, "Mina Karki", "mina@example.com"));
            studentDao.save(new Student(2, "Suman Rai", "suman@example.com"));

            System.out.println("All students:");
            studentDao.findAll().forEach(System.out::println);

            System.out.println("\nFind student with id 2:");
            studentDao.findById(2).ifPresent(System.out::println);

            studentDao.updateEmail(2, "suman.rai@example.com");

            System.out.println("\nAfter email update:");
            studentDao.findAll().forEach(System.out::println);
        } catch (SQLException ex) {
            System.err.println("DAO demo failed.");
            ex.printStackTrace();
        }
    }

    private record Student(int id, String name, String email) {
    }

    private static class StudentDao {
        private final Connection connection;

        StudentDao(Connection connection) {
            this.connection = connection;
        }

        void createTable() throws SQLException {
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

        void deleteAll() throws SQLException {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM students");
            }
        }

        void save(Student student) throws SQLException {
            String sql = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, student.id());
                statement.setString(2, student.name());
                statement.setString(3, student.email());
                statement.executeUpdate();
            }
        }

        Optional<Student> findById(int id) throws SQLException {
            String sql = "SELECT id, name, email FROM students WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(toStudent(resultSet));
                    }
                    return Optional.empty();
                }
            }
        }

        List<Student> findAll() throws SQLException {
            String sql = "SELECT id, name, email FROM students ORDER BY id";
            List<Student> students = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    students.add(toStudent(resultSet));
                }
            }

            return students;
        }

        void updateEmail(int id, String email) throws SQLException {
            String sql = "UPDATE students SET email = ? WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setInt(2, id);
                int rows = statement.executeUpdate();

                if (rows != 1) {
                    throw new SQLException("Student not found: " + id);
                }
            }
        }

        private Student toStudent(ResultSet resultSet) throws SQLException {
            return new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));
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

