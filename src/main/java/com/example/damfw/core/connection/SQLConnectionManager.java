package com.example.damfw.core.connection;

import com.example.damfw.core.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class SQLConnectionManager extends ConnectionManager {

    protected Connection connection;

    @Override
    public void initialize(String url, String username, String password) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("SQL connection established.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while initializing SQL connection: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("SQL connection is not established. Call initialize() first.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("SQL connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
