package com.example.damfw.core.query.builder.wrapIdentifer;

public class MySQLIdentifierWrapper implements IdentifierWrapperStrategy {
    @Override
    public String wrap(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return "";
        }
        return "`" + identifier + "`"; // Use backticks for MySQL
    }
}