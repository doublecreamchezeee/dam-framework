package com.example.damfw.core.management;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.ConnectionAbstractFactory;
import com.example.damfw.core.database.DatabaseAction;
import com.example.damfw.core.database.DatabaseConnectorStrategy;
import com.example.damfw.exception.OutOfConnectionException;

import java.util.*;
import java.util.stream.IntStream;

public class Persistence {
    public final static Integer MAX_EXISTS_CONNECTIONS = 4;

    public static List<DatabaseAction> connectionPool = new ArrayList<>();

    public final static Map<Integer, DatabaseManagerAbstractFactory> used = new HashMap<>();

    private Persistence() {

    }

    public static void configureDatasource(ConnectionConfig configuration, ConnectionAbstractFactory connection, DatabaseConnectorStrategy connector) throws Exception {
        createConnection(configuration, connection, connector);
    }

    public static void createConnection(ConnectionConfig configuration, ConnectionAbstractFactory connection, DatabaseConnectorStrategy connector) throws Exception {
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