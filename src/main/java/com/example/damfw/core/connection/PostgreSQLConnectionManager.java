package com.example.damfw.core.connection;

import com.example.damfw.core.connection.SQLConnectionManager;

import java.sql.Connection;

public class PostgreSQLConnectionManager extends SQLConnectionManager {
    private static PostgreSQLConnectionManager instance;
    // Singleton access method
    public static PostgreSQLConnectionManager getInstance() {
        if (instance == null) {
            synchronized (PostgreSQLConnectionManager.class) {
                if (instance == null) {
                    instance = new PostgreSQLConnectionManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void initialize(String url, String username, String password) {
        super.initialize(url, username, password); // Call parent class's initialize method
        // You can add PostgreSQL-specific setup here if needed
    }

    @Override
    public Connection getConnection() {
        return super.getConnection(); // Return the connection from the parent class
    }

    @Override
    public void close() {
        super.close(); // Close the connection using the parent class's close method
    }
}
