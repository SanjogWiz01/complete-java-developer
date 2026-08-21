package com.jyotibank.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseConfig — Singleton JDBC connection pool manager.
 *
 * <p>Wraps HikariCP so that the rest of the application only needs to call
 * {@code DatabaseConfig.getInstance().getConnection()} to obtain a pooled
 * {@link Connection}.  Callers must close the connection (ideally via
 * try-with-resources) to return it to the pool — closing does NOT close the
 * underlying TCP socket, just hands the slot back to HikariCP.
 *
 * <p><b>Why a connection pool?</b><br>
 * Opening a raw JDBC connection involves TCP handshake + MySQL authentication
 * (~100–300 ms).  For a banking app processing many requests, creating a new
 * connection per operation would be catastrophically slow.  HikariCP maintains
 * a warm pool of connections that are reused instantly.
 *
 * <p><b>Java concepts demonstrated:</b>
 * <ul>
 *   <li>Singleton (double-checked locking, volatile)</li>
 *   <li>final class — singleton must not be subclassed</li>
 *   <li>Dependency on AppConfig — demonstrates composition over inheritance</li>
 *   <li>Resource management — pool is closed via {@link #close()} at shutdown</li>
 * </ul>
 */
public final class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private static volatile DatabaseConfig instance;
    private final HikariDataSource dataSource;

    private DatabaseConfig() {
        AppConfig cfg = AppConfig.getInstance();

        HikariConfig hikari = new HikariConfig();

        // ── Core connection settings ──────────────────────────────────────
        hikari.setJdbcUrl(cfg.getProperty("db.url"));
        hikari.setUsername(cfg.getProperty("db.username"));
        hikari.setPassword(cfg.getProperty("db.password"));

        // ── Pool sizing ───────────────────────────────────────────────────
        /*
         * Maximum pool size: how many DB connections can exist simultaneously.
         * Rule of thumb (HikariCP docs): pool_size = (cpu_cores * 2) + spindle_count
         * For dev/portfolio use, 10 is fine.
         */
        hikari.setMaximumPoolSize(cfg.getIntProperty("db.pool.size", 10));
        hikari.setMinimumIdle(cfg.getIntProperty("db.pool.min.idle", 2));

        // ── Timeouts ──────────────────────────────────────────────────────
        // How long a thread waits for a free connection before throwing
        hikari.setConnectionTimeout(cfg.getLongProperty("db.pool.connection.timeout", 30_000L));
        // Idle connections older than this are removed from the pool
        hikari.setIdleTimeout(cfg.getLongProperty("db.pool.idle.timeout", 600_000L));
        // Hard maximum age of any connection (prevents stale connections)
        hikari.setMaxLifetime(cfg.getLongProperty("db.pool.max.lifetime", 1_800_000L));

        // ── Diagnostics ───────────────────────────────────────────────────
        hikari.setPoolName("JyotiBank-Pool");

        /*
         * connectionTestQuery: sent to MySQL to verify the connection is still alive
         * before handing it to a caller.  Required for older MySQL drivers;
         * modern drivers use isValid() internally, but being explicit is safer.
         */
        hikari.setConnectionTestQuery("SELECT 1");

        logger.info("Initializing HikariCP connection pool (max={}, url={})",
                cfg.getIntProperty("db.pool.size", 10),
                cfg.getProperty("db.url"));

        this.dataSource = new HikariDataSource(hikari);

        logger.info("Database connection pool '{}' ready.", hikari.getPoolName());
    }

    /** Returns the singleton instance, creating it on first call (thread-safe). */
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Borrows a {@link Connection} from the pool.
     *
     * <p><b>Contract for callers:</b> always use try-with-resources or call
     * {@code close()} in a finally block.  Failing to close leaks pool slots
     * and will eventually deadlock the application.
     *
     * <pre>{@code
     *   try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
     *       // use conn
     *   }  // conn.close() automatically returns it to the pool here
     * }</pre>
     *
     * @throws SQLException if no connection is available within connectionTimeout
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Shuts down the entire connection pool.
     * Call once at application exit (e.g., in a JVM shutdown hook).
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool shut down gracefully.");
        }
    }

    /** Exposed for health-check purposes — returns true if the pool is running. */
    public boolean isHealthy() {
        return dataSource != null && !dataSource.isClosed();
    }
}
