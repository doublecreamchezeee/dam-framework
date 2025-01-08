package com.example.damfw.core.database;

import com.example.damfw.core.connection.ConnectionAbstractFactory;
import com.example.damfw.core.connection.ConnectionConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class Neo4jDatabaseConnector implements DatabaseConnectorStrategy{
    @Override
    public DatabaseAction connect(ConnectionConfig configuration, ConnectionAbstractFactory noSqlConnection) throws Exception {

        Connection connection = DriverManager.getConnection(
                noSqlConnection.createConnectionString(configuration),
                configuration.getUsername(),
                configuration.getPassword()
        );

        return new DatabaseAction(connection);
    }
}
