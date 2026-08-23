package com.jyotibank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * AppConfig — Singleton configuration loader.
 *
 * <p>Reads application.properties from the classpath once on first access.
 * All other classes retrieve config values through this single instance.
 *
 * <p><b>Java concepts demonstrated:</b>
 * <ul>
 *   <li>Singleton pattern with double-checked locking (thread-safe lazy init)</li>
 *   <li>final class — prevents subclassing, protecting the singleton contract</li>
 *   <li>try-with-resources — InputStream is auto-closed even if load() throws</li>
 *   <li>ClassLoader resource loading — works inside JAR files, not just file paths</li>
 * </ul>
 */
public final class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final String CONFIG_FILE = "application.properties";

    /*
     * volatile ensures that when thread A writes to 'instance', thread B
     * immediately sees the updated reference (no CPU cache lag).
     * Without volatile, double-checked locking is broken in Java.
     */
    private static volatile AppConfig instance;

    private final Properties properties;

    private AppConfig() {
        properties = new Properties();

        /*
         * try-with-resources: InputStream implements AutoCloseable.
         * Java guarantees close() is called even if properties.load() throws.
         * This prevents file descriptor leaks — important in long-running apps.
         */
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Fail fast — the app cannot run without its config file
                throw new RuntimeException(
                    "Configuration file not found on classpath: " + CONFIG_FILE +
                    ". Make sure it exists in src/main/resources/");
            }
            properties.load(input);
            logger.info("Application configuration loaded from '{}'", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Failed to load '{}': {}", CONFIG_FILE, e.getMessage());
            throw new RuntimeException("Fatal: cannot load application configuration.", e);
        }
    }

    /**
     * Double-checked locking singleton.
     *
     * <p>First check (outside synchronized) avoids locking on every call once
     * the instance is already created — only the very first call ever blocks.
     * Second check (inside synchronized) handles the race when two threads both
     * pass the first null check simultaneously.
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    /** Returns the raw string value for a key, or null if absent. */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /** Returns the value for a key, falling back to defaultValue if absent. */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Parses a property as int.
     * Returns defaultValue — and logs a warning — if the key is missing or unparseable.
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Property '{}' has invalid integer value '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parses a property as double.
     * Used mainly for interest rates and monetary limits.
     */
    public double getDoubleProperty(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Property '{}' has invalid double value '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parses a property as BigDecimal.
     * Money values must never pass through double (binary floating point);
     * this keeps paisa-exact arithmetic end to end.
     */
    public java.math.BigDecimal getDecimalProperty(String key, java.math.BigDecimal defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return new java.math.BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Property '{}' has invalid decimal value '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parses a property as long.
     * Used for timeouts and pool sizes that exceed int range on large servers.
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Property '{}' has invalid long value '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }
}
