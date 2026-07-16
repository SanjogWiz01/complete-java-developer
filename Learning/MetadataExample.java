import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetadataExample {

    private static final String URL = "jdbc:mysql://localhost:3306/learning_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        MetadataExample example = new MetadataExample();
        example.getDatabaseMetadata();
        example.getTableMetadata();
        example.getColumnMetadata();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void getDatabaseMetadata() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("--- Database Metadata ---");
            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + metaData.getDriverName());
            System.out.println("Driver Version: " + metaData.getDriverVersion());
            System.out.println("JDBC Major Version: " + metaData.getJDBCMajorVersion());
            System.out.println("JDBC Minor Version: " + metaData.getJDBCMinorVersion());
            System.out.println("Supports Transactions: " + metaData.supportsTransactions());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTableMetadata() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("\n--- Tables in Database ---");
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String tableType = tables.getString("TABLE_TYPE");
                    System.out.println("Table: " + tableName + " (Type: " + tableType + ")");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getColumnMetadata() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("\n--- Columns in 'users' Table ---");
            try (ResultSet columns = metaData.getColumns(null, null, "users", "%")) {
                while (columns.next()) {
                    String colName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    int size = columns.getInt("COLUMN_SIZE");
                    boolean nullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

                    System.out.println("Column: " + colName
                            + ", Type: " + typeName
                            + ", Size: " + size
                            + ", Nullable: " + nullable);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPrimaryKeys() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("\n--- Primary Keys ---");
            try (ResultSet keys = metaData.getPrimaryKeys(null, null, "users")) {
                while (keys.next()) {
                    String columnName = keys.getString("COLUMN_NAME");
                    String keyName = keys.getString("PK_NAME");
                    System.out.println("Primary Key: " + columnName + " (" + keyName + ")");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
