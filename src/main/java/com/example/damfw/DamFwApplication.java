package com.example.damfw;

import com.example.damfw.core.connection.ConnectionConfig;
import com.example.damfw.core.connection.NoSqlConnectionFactory;
import com.example.damfw.core.connection.SqlConnectionFactory;
import com.example.damfw.core.database.MySQLDatabaseConnector;
import com.example.damfw.core.database.Neo4jDatabaseConnector;
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

    public static void main(String[] args) throws SQLException, UnsupportedActionException, InvocationTargetException,
            InstantiationException, IllegalAccessException, NoSuchMethodException {
//        Map<Object, Object> params = new HashMap<>();
//        params.put("createDatabaseIfNotExist", "true");
        // To test: change the hostname, username, password, and database name according
        // to your database
//        ConnectionConfig configuration = ConnectionConfig.builder()
//                .type("mysql")
//                .hostname("localhost:3306")
//                .username("root")
//                .password("123456")
//                .database("designPattern")
//                .params(params)
//                .build();

//         ConnectionConfig configuration = ConnectionConfig.builder()
//         .type("postgresql")
//         .hostname("localhost:5435")
//         .username("postgres")
//         .password("123456")
//         .database("database")
//         .params(params)
//         .build();

            Map<Object, Object> params = new HashMap<>();
            params.put("enableSQLTranslation", "true");
            ConnectionConfig configuration = ConnectionConfig.builder()
                    .type("neo4j")
                    .hostname("localhost:7687")
                    .username("neo4j")
                    .password("12345678")
                    .params(params)
                    .build();

        try {
            // MySQL
//            Persistence.configureDatasource(configuration, new SqlConnectionFactory(),
//                    new MySQLDatabaseConnector());

//            // PostgresSQL
//             Persistence.configureDatasource(configuration, new SqlConnectionFactory(),
//                     new PostgresSQLDatabaseConnector());
             // Neo4j
                Persistence.configureDatasource(configuration, new NoSqlConnectionFactory(),
                        new Neo4jDatabaseConnector());

            System.out.println("Connected to database");
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DatabaseManagerAbstractFactory factory = Persistence.createRecordManagerFactory("simple-dao");
        DatabaseManager databaseManager = factory.createRecordManager();

//         INSERT INTO user (id, name) VALUES (1, 'ATuan')
//         QueryBuilder insertQuery = QueryBuilder.insert("user")
//                 .value("id", 2)
//                 .value("name", "ATuan2");
//
//         databaseManager.executeUpdate(insertQuery);

        // INSERT FROM OBJECT
//         User user = new User(4, "Telu");
//         databaseManager.insert(user);

        // UPDATE user SET name = 'atuan' WHERE id = 1
        // If not exist then INSERT
        // QueryBuilder query = QueryBuilder.update("user")
        // .setter("name", "Trithanh")
        // .where("id = 2");
        // databaseManager.executeUpdate(query);

//         SELECT * FROM user
//         List<User> users =
//         databaseManager.executeQuery(QueryBuilder.select().from("user"), User.class);
//         users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

         var user = databaseManager.executeQuery(QueryBuilder.select().from("user"));
        while (user.next()) {
            System.out.println(user.getString("name"));
        }

        // SELECT * FROM user WHERE id = 1
        // QueryBuilder selectQuery = QueryBuilder.select()
        // .from("user")
        // .groupBy("id")
        // .having("id = 1");
        // List<User> users = databaseManager.executeQuery(selectQuery, User.class);
        // users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

        // SELECT * FROM user
        // QueryBuilder selectQuery2 = QueryBuilder.select()
        // .from("user");
        // List<User> users = databaseManager.executeQuery(selectQuery2, User.class);
        // users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

        // DELETE FROM user WHERE id = 1
        // QueryBuilder deleteQuery = QueryBuilder.delete("user")
        // .where("id = 1");
        // databaseManager.executeUpdate(deleteQuery);

//        Persistence.release("simple-dao");
    }
}
