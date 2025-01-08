package com.example.damfw.core.database;

import com.example.damfw.core.connection.DatabaseConnectionConfig;
import com.example.damfw.core.connection.DatabaseConnectionAbstractFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDatabaseConnector implements DatabaseConnectorStrategy {
    @Override
    public DatabaseAction connect(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory sqlConnection) throws Exception {
        // Load the MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a connection using the DatabaseConnectionAbstractFactory
        Connection connection = DriverManager.getConnection(
                sqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword());

        // Return a new DatabaseAction object encapsulating the connection
        return new DatabaseAction(connection);
    }
}
