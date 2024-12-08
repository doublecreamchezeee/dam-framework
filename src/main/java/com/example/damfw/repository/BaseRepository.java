package com.example.damfw.repository;


import com.example.damfw.annotations.Column;
import com.example.damfw.annotations.Table;
import com.example.damfw.core.ConnectionManager;
import com.example.damfw.core.ORMMapper;
import com.example.damfw.core.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class BaseRepository<T> {
    private final Class<T> entityClass;

    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String tableName = getTableName();
            Map<String, Object> columns = getColumns(entity);

            String query = QueryBuilder.buildInsertQuery(tableName, columns);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                int index = 1;
                for (Object value : columns.values()) {
                    statement.setObject(index++, value);
                }
                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }

    public void update(T entity, String whereClause) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String tableName = getTableName();
            Map<String, Object> columns = getColumns(entity);

            String query = QueryBuilder.buildUpdateQuery(tableName, columns, whereClause);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                int index = 1;
                for (Object value : columns.values()) {
                    statement.setObject(index++, value);
                }
                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity", e);
        }
    }

    public void delete(String whereClause) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String tableName = getTableName();
            String query = QueryBuilder.buildDeleteQuery(tableName, whereClause);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity", e);
        }
    }

    public T findOne(String whereClause) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String tableName = getTableName();
            String query = QueryBuilder.buildSelectQuery(tableName, "*", whereClause);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return ORMMapper.mapResultSetToObject(resultSet, entityClass);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity", e);
        }
        return null;
    }

    private String getTableName() {
        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Class " + entityClass.getName() + " is not annotated with @Table");
        }
        return entityClass.getAnnotation(Table.class).name();
    }

    private Map<String, Object> getColumns(T entity) throws IllegalAccessException {
        Map<String, Object> columns = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                String columnName = field.getAnnotation(Column.class).name();
                Object value = field.get(entity);
                columns.put(columnName, value);
            }
        }
        return columns;
    }
}
