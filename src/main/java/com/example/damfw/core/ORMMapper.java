package com.example.damfw.core;

import com.example.damfw.annotations.Column;
import com.example.damfw.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ORMMapper {

    public static <T> T mapResultSetToObject(ResultSet rs, Class<T> classTemplate)
            throws SQLException, InstantiationException,
            IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        if (!classTemplate.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Class " + classTemplate.getName() + " is not annotated with @Table");
        }

        T instance = classTemplate.getDeclaredConstructor().newInstance();

        for (Field field : classTemplate.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                field.setAccessible(true);
                field.set(instance, rs.getObject(columnName));
            }
        }

        return instance;
    }
}