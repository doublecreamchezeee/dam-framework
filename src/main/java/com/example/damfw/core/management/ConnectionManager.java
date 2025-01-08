package com.example.damfw.core.management;

import com.example.damfw.core.connection.DatabaseConnectionConfig;
import com.example.damfw.core.connection.DatabaseConnectionAbstractFactory;
import com.example.damfw.core.database.DatabaseAction;
import com.example.damfw.core.database.DatabaseConnectorStrategy;
import com.example.damfw.core.query.builder.wrapIdentifer.DefaultIdentifierWrapper;
import com.example.damfw.exception.OutOfConnectionException;
import com.example.damfw.core.query.builder.QueryBuilder;
import com.example.damfw.core.query.builder.wrapIdentifer.MySQLIdentifierWrapper;
import com.example.damfw.core.query.builder.wrapIdentifer.PostgreSQLIdentifierWrapper;

import java.util.*;
import java.util.stream.IntStream;

public class ConnectionManager {
    public final static Integer MAX_EXISTS_CONNECTIONS = 4;

    public static List<DatabaseAction> connectionPool = new ArrayList<>();

    public final static Map<Integer, DatabaseManagerAbstractFactory> used = new HashMap<>();

    private ConnectionManager() {

    }

    public static void configureDatasource(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory connection, DatabaseConnectorStrategy connector) throws Exception {
        setIdentifierWrapper(configuration.getType());
        createConnection(configuration, connection, connector);
    }

    private static void setIdentifierWrapper(String dbType) {
        System.out.println("Setting identifier wrapper for " + dbType);
        switch (dbType.toLowerCase()) {
            case "mysql" -> QueryBuilder.setIdentifierWrapperStrategy(new MySQLIdentifierWrapper());
            case "postgresql" -> QueryBuilder.setIdentifierWrapperStrategy(new PostgreSQLIdentifierWrapper());
            case "neo4j" -> QueryBuilder.setIdentifierWrapperStrategy(new DefaultIdentifierWrapper());
            default -> throw new UnsupportedOperationException("Unsupported database type: " + dbType);
        }
    }

    public static void createConnection(DatabaseConnectionConfig configuration, DatabaseConnectionAbstractFactory connection, DatabaseConnectorStrategy connector) throws Exception {
        for (int i = 0; i < MAX_EXISTS_CONNECTIONS; ++i) {
            connectionPool.add(connector.connect(configuration, connection));
        }
    }

    public static DatabaseManagerAbstractFactory createRecordManagerFactory(String factoryName) {
        int connectionId = used.entrySet().stream()
                .filter(entry -> factoryName.equals(entry.getValue().getName()))
                .mapToInt(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
        if (connectionId != -1) {
            return used.get(connectionId);
        }
        int freeConnectionId = IntStream.range(0, MAX_EXISTS_CONNECTIONS)
                .filter(id -> !used.containsKey(id))
                .findFirst()
                .orElse(-1);
        if (freeConnectionId == -1) {
            try {
                throw new OutOfConnectionException("Out of database connections.");
            } catch (OutOfConnectionException e) {
                throw new RuntimeException(e);
            }
        }
        DatabaseManagerAbstractFactory factory = new DatabaseManagerAbstractFactory(factoryName,
                connectionPool.get(freeConnectionId));
        used.put(freeConnectionId, factory);
        return factory;
    }

    public static void release(String factoryName) {
        int connectionId = used.entrySet().stream()
                .filter(entry -> factoryName.equals(entry.getValue().getName()))
                .mapToInt(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
        if (connectionId == -1) {
            return;
        }
        used.remove(connectionId);
    }
}