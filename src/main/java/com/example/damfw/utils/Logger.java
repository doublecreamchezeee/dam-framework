package com.example.damfw.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void info(String message) {
        System.out.println("[INFO] " + getCurrentTimestamp() + " - " + message);
    }

    public static void error(String message, Throwable throwable) {
        System.err.println("[ERROR] " + getCurrentTimestamp() + " - " + message);
        throwable.printStackTrace();
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
