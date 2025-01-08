package com.example.damfw.core.management;

import com.example.damfw.core.database.DatabaseAction;
import com.example.damfw.core.mapper.ResultSetMapper;
import com.example.damfw.core.query.QueryType;
import com.example.damfw.core.query.builder.QueryBuilder;
import com.example.damfw.exception.UnsupportedActionException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseOperationManager {
    DatabaseAction databaseAction;
    final List<Objects> records = new ArrayList<>();

    public QueryBuilder createQuery(QueryType type){
        return createQuery(type, null);
    }

    public QueryBuilder createQuery(QueryType type, Object record) {
        return switch (type) {
            case INSERT -> ResultSetMapper.toInsertQuery(record);
            case UPDATE -> ResultSetMapper.toUpdateQuery(record);
            case DELETE -> ResultSetMapper.toDeleteQuery(record);
            default -> QueryBuilder.newQuery(type);
        };
    }

    public <T> List<T> executeQuery(QueryBuilder query, Class<T> clazz)
            throws SQLException, InstantiationException, IllegalAccessException, UnsupportedActionException, InvocationTargetException, NoSuchMethodException {
        ResultSet resultSet = (ResultSet) databaseAction.executeQuery(query.build());
        return ResultSetMapper.map(resultSet, clazz);
    }

    public ResultSet executeQuery(QueryBuilder query)
            throws SQLException, InstantiationException, IllegalAccessException, UnsupportedActionException, InvocationTargetException, NoSuchMethodException {
        return (ResultSet) databaseAction.executeQuery(query.build());
    }

    public void executeUpdate(QueryBuilder query) {
        databaseAction.executeUpdate(query.build());
    }

    public void insert(Object record) {
        QueryBuilder query = createQuery(QueryType.INSERT, record);
        executeUpdate(query);
    }

    public void update(Object record) {
        QueryBuilder query = createQuery(QueryType.UPDATE, record);
        executeUpdate(query);
    }

    public void delete(Object record) {
        QueryBuilder query = createQuery(QueryType.DELETE, record);
        executeUpdate(query);
    }
}
