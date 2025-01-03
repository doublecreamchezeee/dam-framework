package com.example.damfw.core.database;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.ConnectionAbstractFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresSQLDatabaseConnector implements DatabaseConnectorStrategy {
    @Override
    public DatabaseAction connect(ConnectionConfig configuration, ConnectionAbstractFactory sqlConnection) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                sqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword()
        );

        return new DatabaseAction(connection);
    }
}
