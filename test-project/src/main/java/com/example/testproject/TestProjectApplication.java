package com.example.testproject;

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
import com.example.damfw.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TestProjectApplication {
    public static void main(String[] args) {
        // Call the desired test function
//        testPostgres();
        // testMySQL();
         testNeo4j();
    }

    private static void testPostgres() {
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

        try {
            Persistence.configureDatasource(configuration, new SqlConnectionFactory(), new PostgresSQLDatabaseConnector());
            System.out.println("Connected to PostgreSQL database");

            DatabaseManagerAbstractFactory factory = Persistence.createRecordManagerFactory("simple-dao");
            DatabaseManager databaseManager = factory.createRecordManager();

            // Perform database operations
            QueryBuilder insertQuery = QueryBuilder.insert("user")
                    .value("id", 1)
                    .value("name", "PostgresUser");
            databaseManager.executeUpdate(insertQuery);

            List<User> users = databaseManager.executeQuery(QueryBuilder.select().from("user"), User.class);
            users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

            Persistence.release("simple-dao");
        } catch (Exception e) {
            System.err.println("Error with PostgreSQL: " + e.getMessage());
        }
    }

    private static void testMySQL() {
        Map<Object, Object> params = new HashMap<>();
        params.put("createDatabaseIfNotExist", "true");

        ConnectionConfig configuration = ConnectionConfig.builder()
                .type("mysql")
                .hostname("localhost:3306")
                .username("root")
                .password("123456")
                .database("designPattern")
                .params(params)
                .build();

        try {
            Persistence.configureDatasource(configuration, new SqlConnectionFactory(), new MySQLDatabaseConnector());
            System.out.println("Connected to MySQL database");

            DatabaseManagerAbstractFactory factory = Persistence.createRecordManagerFactory("simple-dao");
            DatabaseManager databaseManager = factory.createRecordManager();

            // Perform database operations
            QueryBuilder insertQuery = QueryBuilder.insert("user")
                    .value("id", 1)
                    .value("name", "MySQLUser");
            databaseManager.executeUpdate(insertQuery);

            List<User> users = databaseManager.executeQuery(QueryBuilder.select().from("user"), User.class);
            users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));

            Persistence.release("simple-dao");
        } catch (Exception e) {
            System.err.println("Error with MySQL: " + e.getMessage());
        }
    }

    private static void testNeo4j() {
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
            Persistence.configureDatasource(configuration, new NoSqlConnectionFactory(), new Neo4jDatabaseConnector());
            System.out.println("Connected to Neo4j database");

            DatabaseManagerAbstractFactory factory = Persistence.createRecordManagerFactory("simple-dao");
            DatabaseManager databaseManager = factory.createRecordManager();

            // Perform database operations
            User user = new User(1, "Neo4jUser");
            databaseManager.insert(user);

            ResultSet users = databaseManager.executeQuery(QueryBuilder.select().from("user"));
            while (users.next()){
                System.out.println(users.getInt("id"));
                System.out.println(users.getString("name"));
            }
            Persistence.release("simple-dao");
        } catch (Exception e) {
            System.err.println("Error with Neo4j: " + e.getMessage());
        }
    }
}
