package com.example.damfw.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {

    public static Map<String, Object> getFieldsWithAnnotations(Object object, Class annotationClass) throws IllegalAccessException {
        Map<String, Object> fieldsMap = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                field.setAccessible(true);
                fieldsMap.put(field.getName(), field.get(object));
            }
        }

        return fieldsMap;
    }
}
