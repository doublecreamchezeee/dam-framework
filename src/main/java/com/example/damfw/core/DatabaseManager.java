package com.example.damfw.core;

import lombok.Getter;

public class DatabaseManager {
    @Getter
    private static String url;
    @Getter
    private static String username;
    @Getter
    private static String password;

    private DatabaseManager() {}

    public static void initialize(String dbUrl, String dbUsername, String dbPassword) {
        url = dbUrl;
        username = dbUsername;
        password = dbPassword;
    }
}
