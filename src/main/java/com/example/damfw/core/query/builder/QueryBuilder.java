package com.example.damfw.core.query.builder;

import com.example.damfw.core.query.QueryType;

import java.util.ArrayList;
import java.util.List;

public abstract class QueryBuilder {

    protected String table;

    protected List<String> wheres = new ArrayList<>();

    // Wrap table and column names to avoid reserved keyword conflicts
    protected static String wrapIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return "";
        }
        return "\"" + identifier + "\""; // Use double quotes for PostgreSQL
    }

    public QueryBuilder where(String clause) {
        wheres.add(clause);
        return this;
    }

    public QueryBuilder and(String clause) {
        wheres.add(" AND " + clause);
        return this;
    }

    public QueryBuilder or(String clause) {
        wheres.add(" OR " + clause);
        return this;
    }

    public QueryBuilder table(String table) {
        this.table = wrapIdentifier(table);
        return this;
    }

    public static SelectQueryBuilder select(String... columns) {
        return new SelectQueryBuilder(columns);
    }

    public static InsertQueryBuilder insert(String table) {
        return new InsertQueryBuilder(wrapIdentifier(table));
    }

    public static UpdateQueryBuilder update(String table) {
        return new UpdateQueryBuilder(wrapIdentifier(table));
    }

    public static DeleteQueryBuilder delete(String table) {
        return new DeleteQueryBuilder(wrapIdentifier(table));
    }

    public static QueryBuilder newQuery(QueryType type) {
        return switch (type) {
            case SELECT -> select();
            case INSERT -> new InsertQueryBuilder();
            case UPDATE -> new UpdateQueryBuilder();
            case DELETE -> new DeleteQueryBuilder();
        };
    }

    public abstract String build();
}
