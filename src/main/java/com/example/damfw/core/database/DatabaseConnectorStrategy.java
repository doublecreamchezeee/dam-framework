package com.example.damfw.core.database;

import com.example.damfw.core.connection.DatabaseConnectionConfig;
import com.example.damfw.core.connection.DatabaseConnectionAbstractFactory;

public interface DatabaseConnectorStrategy {
    DatabaseAction connect(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory sqlConnection) throws Exception;
}
