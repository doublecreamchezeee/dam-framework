package com.example.damfw.core.database;

import com.example.damfw.core.connection.DatabaseConnectionAbstractFactory;
import com.example.damfw.core.connection.DatabaseConnectionConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class Neo4jDatabaseConnector implements DatabaseConnectorStrategy{
    @Override
    public DatabaseAction connect(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory noSqlConnection) throws Exception {

        Connection connection = DriverManager.getConnection(
                noSqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword()
        );

        return new DatabaseAction(connection);
    }
}
