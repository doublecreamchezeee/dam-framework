package com.example.damfw.core.database;

import com.example.damfw.core.connection.DatabaseConnectionConfig;
import com.example.damfw.core.connection.DatabaseConnectionAbstractFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresSQLDatabaseConnector implements DatabaseConnectorStrategy {
    @Override
    public DatabaseAction connect(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory sqlConnection) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                sqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword()
        );

        return new DatabaseAction(connection);
    }
}
