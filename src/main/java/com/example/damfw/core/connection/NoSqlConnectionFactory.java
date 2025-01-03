package com.example.damfw.core.connection;

import java.util.stream.Collectors;

public class NoSqlConnectionFactory extends ConnectionAbstractFactory {

    @Override
    public String createConnectionString(ConnectionConfig config) {
        StringBuilder connectionUrl = new StringBuilder("mongodb://");

        if (config.getUsername() != null && config.getPassword() != null) {
            connectionUrl.append(config.getUsername()).append(":").append(config.getPassword()).append("@");
        }

        connectionUrl.append(config.getHostname())
                .append("/")
                .append(config.getDatabase());

        if (config.getParams() != null && !config.getParams().isEmpty()) {
            String queryParams = config.getParams().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
            connectionUrl.append("?").append(queryParams);
        }

        return connectionUrl.toString();
    }
}
