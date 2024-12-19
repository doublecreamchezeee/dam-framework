package com.example.damfw.core;

public abstract class ConnectionManager {

    // Abstract methods for initializing, getting, and closing connections
    public abstract void initialize(String url, String username, String password);

    public abstract Object getConnection();  // Object because it could be SQL or NoSQL

    public abstract void close();
}