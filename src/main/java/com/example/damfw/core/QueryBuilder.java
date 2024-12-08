package com.example.damfw.core;

import java.util.Map;

public class QueryBuilder {

    public static String buildInsertQuery(String tableName, Map<String, Object> columns) {
        StringBuilder columnsPart = new StringBuilder();
        StringBuilder valuesPart = new StringBuilder();

        columns.forEach((key, value) -> {
            columnsPart.append(key).append(", ");
            valuesPart.append("?, ");
        });

        columnsPart.setLength(columnsPart.length() - 2);
        valuesPart.setLength(valuesPart.length() - 2);

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnsPart, valuesPart);
    }

    public static String buildUpdateQuery(String tableName, Map<String, Object> columns, String whereClause) {
        StringBuilder setPart = new StringBuilder();

        columns.forEach((key, value) -> setPart.append(key).append(" = ?, "));

        setPart.setLength(setPart.length() - 2);

        return String.format("UPDATE %s SET %s WHERE %s", tableName, setPart, whereClause);
    }

    public static String buildDeleteQuery(String tableName, String whereClause) {
        return String.format("DELETE FROM %s WHERE %s", tableName, whereClause);
    }

    public static String buildSelectQuery(String tableName, String columns, String whereClause) {
        return String.format("SELECT %s FROM %s WHERE %s", columns, tableName, whereClause);
    }
}