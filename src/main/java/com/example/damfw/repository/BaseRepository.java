package com.example.damfw.repository;


import com.example.damfw.annotations.Column;
import com.example.damfw.annotations.Table;
import com.example.damfw.core.connection.ConnectionManager;
import com.example.damfw.core.ORMMapper;
import com.example.damfw.core.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseRepository<T> {
    private final Class<T> entityClass;
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    public BaseRepository(Class<T> entityClass, Connection connection) {
        this.entityClass = entityClass;
        this.connection = connection;
    }

    public void save(T entity) {
        logger.info("Connection: {}", connection);
        try (connection) {
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

//    public void update(T entity, String whereClause) {
//        try (Connection connection = (Connection) ConnectionManager.getInstance().getConnection()) {
//            String tableName = getTableName();
//            Map<String, Object> columns = getColumns(entity);
//
//            String query = QueryBuilder.buildUpdateQuery(tableName, columns, whereClause);
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                int index = 1;
//                for (Object value : columns.values()) {
//                    statement.setObject(index++, value);
//                }
//                statement.executeUpdate();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error updating entity", e);
//        }
//    }
//
//    public void delete(String whereClause) {
//        try (Connection connection = (Connection) ConnectionManager.getInstance().getConnection()) {
//            String tableName = getTableName();
//            String query = QueryBuilder.buildDeleteQuery(tableName, whereClause);
//
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.executeUpdate();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error deleting entity", e);
//        }
//    }
//
//    public T findOne(String whereClause) {
//        try (Connection connection = (Connection) ConnectionManager.getInstance().getConnection()) {
//            String tableName = getTableName();
//            String query = QueryBuilder.buildSelectQuery(tableName, "*", whereClause);
//
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                ResultSet resultSet = statement.executeQuery();
//                if (resultSet.next()) {
//                    return ORMMapper.mapResultSetToObject(resultSet, entityClass);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error finding entity", e);
//        }
//        return null;
//    }

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
