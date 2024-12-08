package com.example.damfw.core;

import com.example.damfw.utils.ConfigUtil;
import org.springframework.data.config.ConfigurationUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection;

    private ConnectionManager() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = ConfigUtil.getProperty("db.url");
            String username = ConfigUtil.getProperty("db.username");
            String password = ConfigUtil.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
        }

        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}