package com.example.damfw.core.connection;

import com.example.damfw.core.connection.ConnectionManager;

public abstract class NoSQLConnectionManager extends ConnectionManager {

    protected Object connection;  // Could be MongoClient, FirebaseDatabase, etc.

    @Override
    public abstract void initialize(String url, String username, String password);

    @Override
    public abstract Object getConnection();

    @Override
    public abstract void close();
}
