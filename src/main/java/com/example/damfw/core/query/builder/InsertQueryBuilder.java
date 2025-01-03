package com.example.damfw.core.query.builder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class InsertQueryBuilder extends QueryBuilder {

    Map<Object, Object> values;

    public InsertQueryBuilder() {
        values = new HashMap<>();
    }

    public InsertQueryBuilder(String table) {
        this();
        this.table = table;
    }

    public InsertQueryBuilder value(Object key, Object value) {
        values.put(key, value);
        return this;
    }

    public InsertQueryBuilder values(Map<Object, Object> values) {
        this.values = values;
        return this;
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(this.table)
                .append(" (");
        List<Object> keys = new ArrayList<>(values.keySet());
        for (int i = 0; i < keys.size(); ++i) {
            Object key = keys.get(i);
            builder.append(key);
            if (i < keys.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(") VALUES (");
        List<Object> vals = new ArrayList<>(values.values());
        for (int i = 0; i < vals.size(); ++i) {
            Object value = vals.get(i);
            if (value instanceof String) {
                value = "'" + value + "'";
            }
            builder.append(value);
            if (i < keys.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(") ");
        if (!this.wheres.isEmpty()) {
            builder.append(" WHERE ");
            for (String where : this.wheres) {
                builder.append(where);
            }
        }
        return builder.toString();
    }
}