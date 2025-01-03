package com.example.damfw.core.database;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.ConnectionAbstractFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDatabaseConnector implements DatabaseConnectorStrategy {
    @Override
    public DatabaseAction connect(ConnectionConfig configuration, ConnectionAbstractFactory sqlConnection) throws Exception {
        // Load the MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a connection using the ConnectionAbstractFactory
        Connection connection = DriverManager.getConnection(
                sqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword());

        // Return a new DatabaseAction object encapsulating the connection
        return new DatabaseAction(connection);
    }
}
