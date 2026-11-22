package com.skillora.database;

import com.skillora.utility.FileUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connectivity using the Singleton pattern.
 * Keeps database credentials externalized in db.properties.
 * Demonstrates: Singleton Pattern, JDBC DriverManager, Connection management.
 */
public class DBConnection {

    private static Connection connection = null;

    // Private constructor prevents direct instantiation
    private DBConnection() {}

    /**
     * Returns an active java.sql.Connection instance.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                Properties props = FileUtil.loadDatabaseProperties();
                String url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/skillora_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
                String user = props.getProperty("db.user", "root");
                String pass = props.getProperty("db.password", "");

                connection = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found in classpath. Ensure mysql-connector-j jar is present.", e);
            }
        }
        return connection;
    }

    /**
     * Closes the active database connection when application exits.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
