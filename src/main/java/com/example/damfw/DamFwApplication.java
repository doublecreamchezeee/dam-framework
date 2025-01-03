package com.example.damfw;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.SqlConnectionFactory;
import com.example.damfw.core.database.PostgresSQLDatabaseConnector;
import com.example.damfw.core.management.DatabaseManager;
import com.example.damfw.core.management.DatabaseManagerAbstractFactory;
import com.example.damfw.core.management.Persistence;
import com.example.damfw.core.query.builder.QueryBuilder;
import com.example.damfw.exception.ConnectionException;
import com.example.damfw.exception.UnsupportedActionException;
import com.example.damfw.model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamFwApplication {

    public static void main(String[] args) throws SQLException, UnsupportedActionException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Map<Object, Object> params = new HashMap<>();
        params.put("createDatabaseIfNotExist", "true");
        ConnectionConfig configuration = ConnectionConfig.builder()
                .type("postgresql")
                .hostname("localhost:5435")
                .username("postgres")
                .password("123456")
                .database("database")
                .params(params)
                .build();
//        PostgresSQLDatabaseConnector connector = new PostgresSQLDatabaseConnector();
        try {
            Persistence.configureDatasource(configuration, new SqlConnectionFactory(), new PostgresSQLDatabaseConnector());
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DatabaseManagerAbstractFactory factory = Persistence.createRecordManagerFactory("simple-dao");
        DatabaseManager databaseManager = factory.createRecordManager();
//        QueryBuilder query = QueryBuilder.update("user")
//                .setter("name", "hehe")
//                .where("id = 3");
//        databaseManager.executeUpdate(query);
//        QueryBuilder insertQuery = QueryBuilder.insert("user")
//                .value("id", 5)
//                .value("name", "test");
//        databaseManager.executeUpdate(insertQuery);

//        User user = new User();
//        databaseManager.insert(user);
        List<User> users = databaseManager.executeQuery(QueryBuilder.select().from("user"), User.class);
        users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

//        QueryBuilder selectQuery = QueryBuilder.select()
//                .from("user")
//                .groupBy("id")
//                .having("id = 1").or("name = '2'");
//        QueryBuilder selectQuery2 = QueryBuilder.select()
//                .from("user");
//        System.out.println(selectQuery.build());
//        System.out.println(selectQuery2.build());


        Persistence.release("simple-dao");
    }
}
