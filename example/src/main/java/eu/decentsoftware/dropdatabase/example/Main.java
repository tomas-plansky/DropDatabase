package eu.decentsoftware.dropdatabase.example;

import eu.decentsoftware.dropdatabase.Credentials;
import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.annotations.DatabaseColumn;
import eu.decentsoftware.dropdatabase.annotations.DatabaseTable;
import eu.decentsoftware.dropdatabase.connector.Connector;
import eu.decentsoftware.dropdatabase.connector.MySQLConnector;
import eu.decentsoftware.dropdatabase.query.CreateTableQuery;
import eu.decentsoftware.dropdatabase.query.InsertQuery;
import eu.decentsoftware.dropdatabase.query.SelectQuery;
import eu.decentsoftware.dropdatabase.query.intent.ColumnValuePair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class Main {

    public static void main(String[] args) {
        Credentials credentials = new Credentials("localhost", "3306", "root", "root", "example");
        Connector connector = new MySQLConnector(credentials);
        DatabaseManager databaseManager = new DatabaseManager(connector);

        // ========== CREATE TABLE ========== //

        // -- Creating a table with ORM
        databaseManager.createORMTable(User.class, true);

        // -- Creating a table with Query
        CreateTableQuery createTableQuery = CreateTableQuery.builder()
                .setDatabase("example")
                .setIfNotExists(true)
                .setTable("users")
                .addColumns(
                        new CreateTableQuery.Column("name", "VARCHAR(16)", true, false, true, false, false),
                        new CreateTableQuery.Column("age", "INT", false, false, false, false, false)
                )
                .build();
        databaseManager.executeUpdate(createTableQuery);

        // ========== INSERT ========== //

        // -- Inserting data with ORM
        databaseManager.saveORMObject(new User(0, "John", 20), true);

        // -- Inserting data with Query
        InsertQuery insertQuery = InsertQuery.builder()
                .setTable("users")
                .setColumns(
                        new ColumnValuePair("name", "John"),
                        new ColumnValuePair("age", 20)
                )
                .build();
        databaseManager.executeUpdate(insertQuery);

        // ========== SELECT ========== //

        // -- Selecting data with ORM
        List<User> loadedUsers = databaseManager.loadORMObjects(User.class, (builder) -> builder.setWhere("name = ?").setValues("John"));
        for (User user : loadedUsers) {
            System.out.println(user.getName() + " is " + user.getAge() + " years old.");
        }

        // -- Selecting data with Query
        SelectQuery selectQuery = SelectQuery.builder()
                .setTable("users")
                .setColumns("name", "age")
                .setWhere("name = ?")
                .build();
        databaseManager.executeQuery(selectQuery, (resultSet) -> {
            try {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    System.out.println(name + " is " + age + " years old.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @DatabaseTable(name = "users", database = "example")
    public static class User {

        @DatabaseColumn(name = "id", type = "INT", primaryKey = true, autoIncrement = true, notNull = true, unique = true)
        private final int id;

        @DatabaseColumn(name = "name", type = "VARCHAR(16)", notNull = true, unique = true)
        private String name;

        @DatabaseColumn(name = "age", type = "INT", unsigned = true)
        private int age;

    }

}
