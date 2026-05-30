import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionDemo {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();

        try (Connection connection = DriverManager.getConnection(
                config.url(),
                config.user(),
                config.password())) {

            DatabaseMetaData metadata = connection.getMetaData();
            System.out.println("Connected successfully.");
            System.out.println("Database: " + metadata.getDatabaseProductName());
            System.out.println("Version: " + metadata.getDatabaseProductVersion());
            System.out.println("Driver: " + metadata.getDriverName());
        } catch (SQLException ex) {
            System.err.println("Could not connect to the database.");
            System.err.println("Check DB_URL, DB_USER, DB_PASSWORD, and the JDBC driver jar.");
            ex.printStackTrace();
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

