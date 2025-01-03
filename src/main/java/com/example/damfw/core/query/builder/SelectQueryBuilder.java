package com.example.damfw.core.query.builder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelectQueryBuilder extends QueryBuilder {

    final List<String> columns;

    List<String> joins;

    GroupByBuilder groupBy;

    public SelectQueryBuilder(String... columns) {
        this.columns = Arrays.asList(columns);
        this.joins = new ArrayList<>();
    }

    public SelectQueryBuilder join(String table) {
        if (joins == null) {
            joins = new ArrayList<>();
        }
        joins.add(table);
        return this;
    }

    public SelectQueryBuilder from(String table) {
        this.table = wrapIdentifier(table);
        return this;
    }

    public GroupByBuilder groupBy(String...columns) {
        this.groupBy = new GroupByBuilder(this, columns);
        return this.groupBy;
    }

    public static class GroupByBuilder extends QueryBuilder {

        private final SelectQueryBuilder selectQueryBuilder;

        private List<String> columns;

        private final List<String> having;

        public GroupByBuilder(SelectQueryBuilder selectQueryBuilder) {
            this.selectQueryBuilder = selectQueryBuilder;
            this.having = new ArrayList<>();
        }

        public GroupByBuilder(SelectQueryBuilder selectQueryBuilder, String...columns) {
            this(selectQueryBuilder);
            this.columns = Arrays.asList(columns);
        }

        public QueryBuilder having(String clause) {
            having.add(clause);
            return this;
        }

        public QueryBuilder and(String clause) {
            having.add(" AND " + clause);
            return this;
        }

        public QueryBuilder or(String clause) {
            having.add(" OR " + clause);
            return this;
        }

        @Override
        public String build() {
            if (columns.isEmpty()) {
                return this.selectQueryBuilder.build();
            }
            StringBuilder builder = new StringBuilder();
            builder.append(this.selectQueryBuilder.build())
                    .append(" GROUP BY ");
            for (int i = 0; i < columns.size(); ++i) {
                String column = columns.get(i);
                builder.append(column);
                if (i < columns.size() - 1) {
                    builder.append(",");
                };
            }
            if (!this.having.isEmpty()) {
                builder.append(" HAVING ");
                for (String having : this.having) {
                    builder.append(having);
                }
            };
            return builder.toString();
        }
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT");
        if (!columns.isEmpty()) {
            builder.append(columns.get(0));
            for (String column : columns) {
                builder.append(",").append(column);
            }
        } else {
            builder.append(" * ");
        }
        builder.append(" FROM ");
        builder.append(this.table);
        if (!joins.isEmpty()) {
            for (int i = 1; i < joins.size(); ++i) {
                builder.append(" JOIN ").append(joins.get(i));
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