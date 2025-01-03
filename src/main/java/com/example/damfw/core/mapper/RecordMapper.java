package com.example.damfw.core.mapper;

import com.example.damfw.annotations.Column;
import com.example.damfw.annotations.Id;
import com.example.damfw.annotations.Table;
import com.example.damfw.core.query.builder.QueryBuilder;
import com.example.damfw.exception.UnsupportedActionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordMapper {

    public static <T> List<T> map(ResultSet rs, Class<T> clazz)
            throws SQLException, InstantiationException, IllegalAccessException,
            UnsupportedActionException, NoSuchMethodException, InvocationTargetException {
        List<T> data = new ArrayList<>();
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new UnsupportedActionException("Must be Table object");
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        Field[] fields = clazz.getDeclaredFields();
        while (rs.next()) {
            T bean = clazz.getDeclaredConstructor().newInstance();
            for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                String columnName = rsmd.getColumnName(_iterator + 1);
                Object columnValue = rs.getObject(_iterator + 1);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
                            field.set(bean, columnValue);
                        }
                    }
                    if (field.isAnnotationPresent(Id.class)) {
                        Id column = field.getAnnotation(Id.class);
                        if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
                            field.set(bean, columnValue);
                        }
                    }
                }
            }
            data.add(bean);
        }
        return data;
    }

    public static <T> QueryBuilder toInsertQuery(T object) {
        try {
            Class<T> clazz = (Class<T>) object.getClass();
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new RuntimeException("No valid record.");
            }
            String tableName = clazz.getAnnotation(Table.class).table();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            Map<Object, Object> values = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    Id column = field.getAnnotation(Id.class);
                    Object idValue = field.get(object);
                    if (idValue != null && field.getType().isAssignableFrom(String.class)) {
                        idValue = "'" + idValue + "'";
                    }
                    values.put(column.name(), idValue);
                }
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Object columnValue = field.get(object);
                    if (columnValue != null && field.getType().isAssignableFrom(String.class)) {
                        columnValue = "'" + columnValue + "'";
                    }
                    values.put(column.name(), columnValue);
                }
            }
            return QueryBuilder.insert(tableName).values(values);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't process query on record.");
        }
    }

    public static <T> QueryBuilder toUpdateQuery(T object) {
        try {
            Class<?> clazz = object.getClass();
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new RuntimeException("No valid record.");
            }
            Field[] fields = clazz.getDeclaredFields();
            Field idField = null;
            Map<Object, Object> setters = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    idField = field;
                    Id column = field.getAnnotation(Id.class);
                    Object idValue = field.get(object);
                    if (idValue != null && field.getType().isAssignableFrom(String.class)) {
                        idValue = "'" + idValue + "'";
                    }
                    setters.put(column.name(), idValue);
                }
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Object columnValue = field.get(object);
                    if (columnValue != null && field.getType().isAssignableFrom(String.class)) {
                        columnValue = "'" + columnValue + "'";
                    }
                    setters.put(column.name(), columnValue);
                }
            }
            if (idField == null) {
                throw new RuntimeException("haha");
            }
            String tableName = clazz.getAnnotation(Table.class).table();
            return QueryBuilder.update(tableName)
                    .setters(setters)
                    .where(idField.getAnnotation(Id.class).name()
                            + " = " + idField.get(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> QueryBuilder toDeleteQuery(T object) {
        try {
            Class<?> clazz = object.getClass();
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new RuntimeException("No valid record.");
            }
            Field[] fields = clazz.getDeclaredFields();
            Field idField = null;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    idField = field;
                }
            }
            if (idField == null) {
                throw new RuntimeException("No ID column. Please assign an id corresponding column!");
            }
            String tableName = clazz.getAnnotation(Table.class).table();
            Object idValue = idField.get(object);
            if (idField.getType().isAssignableFrom(String.class)) {
                idValue = "'" + idValue + "'";
            }
            return QueryBuilder.delete(tableName)
                    .where(idField.getAnnotation(Id.class).name() + " = " + idValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}