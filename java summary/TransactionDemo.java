import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionDemo {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();

        try (Connection connection = DriverManager.getConnection(
                config.url(),
                config.user(),
                config.password())) {

            createAccountsTable(connection);
            resetAccounts(connection);
            printAccounts(connection, "Before transfer");
            transferMoney(connection, 1, 2, new BigDecimal("250.00"));
            printAccounts(connection, "After transfer");
        } catch (SQLException ex) {
            System.err.println("Transaction demo failed.");
            ex.printStackTrace();
        }
    }

    private static void createAccountsTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id INT PRIMARY KEY,
                    owner_name VARCHAR(100) NOT NULL,
                    balance DECIMAL(12, 2) NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static void resetAccounts(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM accounts");
        }

        insertAccount(connection, 1, "Sender", new BigDecimal("1000.00"));
        insertAccount(connection, 2, "Receiver", new BigDecimal("300.00"));
    }

    private static void insertAccount(
            Connection connection,
            int id,
            String ownerName,
            BigDecimal balance) throws SQLException {

        String sql = "INSERT INTO accounts (id, owner_name, balance) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, ownerName);
            statement.setBigDecimal(3, balance);
            statement.executeUpdate();
        }
    }

    private static void transferMoney(
            Connection connection,
            int fromAccountId,
            int toAccountId,
            BigDecimal amount) throws SQLException {

        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            updateBalance(connection, fromAccountId, amount.negate());
            updateBalance(connection, toAccountId, amount);
            connection.commit();
            System.out.println("Transfer committed.");
        } catch (SQLException ex) {
            connection.rollback();
            System.err.println("Transfer rolled back.");
            throw ex;
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private static void updateBalance(
            Connection connection,
            int accountId,
            BigDecimal changeAmount) throws SQLException {

        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, changeAmount);
            statement.setInt(2, accountId);
            int rows = statement.executeUpdate();

            if (rows != 1) {
                throw new SQLException("Account not found: " + accountId);
            }
        }
    }

    private static void printAccounts(Connection connection, String title) throws SQLException {
        String sql = "SELECT id, owner_name, balance FROM accounts ORDER BY id";
        System.out.println("\n" + title);

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                System.out.printf(
                        "%d | %s | %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("owner_name"),
                        resultSet.getBigDecimal("balance"));
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

