package com.inventoryapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Db {

    private static final String CONFIG_FILE = "/db.properties";
    private static final Properties PROPERTIES = loadProperties();

    static {
        try {
            Class.forName(PROPERTIES.getProperty("db.driver"));
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private Db() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"),
                PROPERTIES.getProperty("db.password"));
    }

    public static void rollbackQuietly(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // Ignore rollback failures so the original error can surface.
        }
    }

    private static Properties loadProperties() {
        try (InputStream inputStream = Db.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing db.properties on classpath");
            }
            Properties props = new Properties();
            props.load(inputStream);
            return props;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load db.properties", ex);
        }
    }
}
