package com.example.damfw.core.query.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateQueryBuilder extends QueryBuilder {

    private String table;

    private Map<Object, Object> setters;

    public UpdateQueryBuilder() {
        this.setters = new HashMap<>();
    }

    public UpdateQueryBuilder(String table) {
        this();
        this.table = table;
    }

    public UpdateQueryBuilder setter(Object key, Object value) {
        setters.put(key, value);
        return this;
    }

    public UpdateQueryBuilder setters(Map<Object, Object> setters) {
        this.setters = setters;
        return this;
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ")
                .append(this.table);
        builder.append(" SET ");
        List<Map.Entry<Object, Object>> setters = new ArrayList<>(this.setters.entrySet());
        for (int i = 0; i < setters.size(); ++i) {
            Map.Entry<Object, Object> setter = setters.get(i);
            Object value = setter.getValue();
            if (value instanceof String) {
                value = "'" + value + "'";
            }
            builder.append(setter.getKey())
                    .append("=")
                    .append(value);
            if (i < setters.size() - 1) {
                builder.append(",");
            }
        }
        if (!this.wheres.isEmpty()) {
            builder.append(" WHERE ");
            for (String where : this.wheres) {
                builder.append(where);
            }
        }
        return builder.toString();
    }
}