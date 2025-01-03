package com.example.damfw.core.query.builder;

public class DeleteQueryBuilder extends QueryBuilder {

    public DeleteQueryBuilder() {
    }

    public DeleteQueryBuilder(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(this.table);
        if (!this.wheres.isEmpty()) {
            builder.append(" WHERE ");
            for (String where : this.wheres) {
                builder.append(where).append(" ");
            }
        }
        return builder.toString();
    }
}