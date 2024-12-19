package com.example.damfw.core.connection;

public abstract class ConnectionManager {
    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    // Depending on the use case, instantiate SQL or NoSQL connection managers here.
                    // For example, we can default to PostgreSQL connection manager.
                    instance = new PostgreSQLConnectionManager();
                }
            }
        }
        return instance;
    }

    // Abstract methods for initializing, getting, and closing connections
    public abstract void initialize(String url, String username, String password);

    public abstract Object getConnection();  // Object because it could be SQL or NoSQL

    public abstract void close();
}