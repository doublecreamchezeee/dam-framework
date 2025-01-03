package com.example.damfw.core.database;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.ConnectionAbstractFactory;

public interface DatabaseConnectorStrategy {
    DatabaseAction connect(ConnectionConfig configuration, ConnectionAbstractFactory sqlConnection) throws Exception;
}
