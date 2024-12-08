package com.example.damfw.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream("application.yaml"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
