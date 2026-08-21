package com.jyotibank;

import com.jyotibank.config.AppConfig;
import com.jyotibank.config.DatabaseConfig;
import com.jyotibank.presentation.MainMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Main — application entry point.
 *
 * <p>Phase 1 only verifies that:
 * <ol>
 *   <li>AppConfig loads application.properties correctly</li>
 *   <li>DatabaseConfig builds the HikariCP pool without error</li>
 *   <li>A real JDBC connection to MySQL is obtained and returned</li>
 * </ol>
 *
 * <p>This class will be expanded in later phases to launch the console UI.
 *
 * <p><b>Java concept — shutdown hooks:</b><br>
 * {@code Runtime.getRuntime().addShutdownHook()} registers a thread that the
 * JVM executes when the application exits (Ctrl+C, System.exit, normal return).
 * We use it to guarantee the HikariCP pool is closed cleanly even if the user
 * kills the program, preventing connection leaks in MySQL.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        printBanner();

        AppConfig config = AppConfig.getInstance();
        logger.info("Starting {} v{}",
                config.getProperty("app.name", "Jyoti Bank"),
                config.getProperty("app.version", "1.0.0"));

        // Register shutdown hook so the pool is always closed on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook triggered — closing DB pool...");
            DatabaseConfig.getInstance().close();
        }, "shutdown-hook"));

        // Phase 1 connection smoke-test
        verifyDatabaseConnection();

        if (args.length > 0 && "--menu".equalsIgnoreCase(args[0])) {
            new MainMenu().start();
        }
        logger.info("Startup checks complete.");
    }

    private static void verifyDatabaseConnection() {
        System.out.println("\n[DB] Attempting database connection...");

        /*
         * try-with-resources: Connection implements AutoCloseable.
         * The connection is returned to the HikariCP pool when the try block exits,
         * regardless of whether it exits normally or via an exception.
         */
        try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.printf("[DB] Connected to: %s %s%n",
                    meta.getDatabaseProductName(),
                    meta.getDatabaseProductVersion());
            System.out.printf("[DB] JDBC Driver:  %s%n", meta.getDriverName());
            System.out.println("[DB] Connection pool: HEALTHY");
            logger.info("Database connection verified successfully.");
        } catch (SQLException e) {
            logger.error("Database connection FAILED: {}", e.getMessage());
            System.err.println("\n[ERROR] Cannot connect to database.");
            System.err.println("  Check: db.url / db.username / db.password in application.properties");
            System.err.println("  Ensure MySQL is running and the 'jyoti_bank' database exists.");
            System.err.println("  SQL State: " + e.getSQLState());
            System.exit(1); // Fatal — app cannot run without DB
        }
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║       JYOTI BANK MANAGEMENT SYSTEM v1.0.0       ║");
        System.out.println("║          Powered by Core & Advanced Java         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
    }
}
