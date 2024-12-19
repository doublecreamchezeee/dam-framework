package com.example.damfw.core.connection;

import com.example.damfw.core.connection.NoSQLConnectionManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionManager extends NoSQLConnectionManager {

    private static MongoClient mongoClient;
    private static MongoDBConnectionManager instance;

    public static MongoDBConnectionManager getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnectionManager.class) {
                if (instance == null) {
                    instance = new MongoDBConnectionManager();
                }
            }
        }
        return instance;
    }
    @Override
    public void initialize(String url, String username, String password) {
        mongoClient = MongoClients.create(url);  // MongoDB connection string
        connection = mongoClient.getDatabase("test");  // MongoDatabase object
        System.out.println("MongoDB connection established.");
    }

    @Override
    public MongoDatabase getConnection() {
        return (MongoDatabase) connection;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}
