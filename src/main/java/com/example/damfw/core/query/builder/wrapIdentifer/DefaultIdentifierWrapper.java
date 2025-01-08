package com.example.damfw.core.query.builder.wrapIdentifer;

public class DefaultIdentifierWrapper implements IdentifierWrapperStrategy{
    @Override
    public String wrap(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return "";
        }
        return "\"" + identifier + "\""; // Use double quotes for PostgreSQL
    }
}
