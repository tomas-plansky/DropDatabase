# Drop Database

> Java Library for connecting to and working with various SQL databases.

## Table of Contents

- [About](#about)
- [Features](#features)
- [License](#license)
- [Contributing](#contributing)
  - [Building](#building)
- [Usage](#usage)
  - [Adding the library to your project](#adding-the-library-to-your-project)
  - [Creating a DatabaseManager](#creating-a-databasemanager)
  - [Building and Executing a Query](#building-and-executing-a-query)
  - [ORM](#orm)

## About

This library is a Java library for connecting to and working with various SQL databases. It is designed to be simple and easy to use.

- Built on: *Java 8*

## Features

- A simple, easy to use API
- Multiple database drivers
- Query builder
- Schema builder
- ORM (Object Relational Mapping)
- Migrations
- Seeding

## License

This library is licensed under the [MIT License](LICENSE).

## Contributing

If you would like to contribute to this project, please read the [contributing guidelines](CONTRIBUTING.md).

### Building

To build this project, you will need to have Java 8 and Maven installed. Then, simply run the following command:

```
mvn clean install
```

## Usage

First of all, you need to add the library to your project. Below are instructions for adding the library to your project using Maven or Gradle.

### Adding the library to your project

<details>
<summary>Maven (Expand)</summary>

To use this library in your project, add the following to your `pom.xml`:

> Replace `VERSION` with the latest version of the library.

```xml

<dependency>
    <groupId>com.github.d0by</groupId>
    <artifactId>drop-database</artifactId>
    <version>VERSION</version>
    <scope>compile</scope>
</dependency>
```
</details>

<details>
<summary>Gradle (Expand)</summary>

To use this library in your project, add the following to your `build.gradle`:

> Replace `VERSION` with the latest version of the library.

```groovy
repositories {
    mavenCentral()
    jitpack()
}

dependencies {
    // Core functionality of the library (required)
    compile 'com.github.d0by:drop-database:core:VERSION'
    
    // Connectors: (Used for creating connections to the database)
    // MySQL connector (optional)
    compile 'com.github.d0by:drop-database:mysql-connector:VERSION'
    // PostgreSQL connector (optional)
    compile 'com.github.d0by:drop-database:postgresql-connector:VERSION'
    // SQLite connector (optional)
    compile 'com.github.d0by:drop-database:sqlite-connector:VERSION'
    // MariaDB connector (optional)
    compile 'com.github.d0by:drop-database:mariadb-connector:VERSION'
}
```
</details>

### Creating a DatabaseManager

In order to use this library, you need to create a `DatabaseManager` instance. Each `DatabaseManager`
requires its own `Connector` instance. The `Connector` is used to create a connection to the database.
You don't need to worry about the connection itself, just create a `Connector` to your respective
database and pass it to the `DatabaseManager` constructor.

**Examples:**

```java
// -- Create a new MySQL connector (requires the MySQL connector dependency)
// First, we need Credentials to connect to the database
Credentials credentials = new Credentials("localhost", "3306", "database", "username", "password");
// Then, we can create a new MySQLConnector
Connector connector = new MySQLConnector(credentials);

// -- Create a new DatabaseManager
DatabaseManager databaseManager = new DatabaseManager(connector);

// ... Do stuff with the database manager
```

```java
// Optionally, you can also specify custom Properties for the connection,
// this is done by creating a new Properties object and passing it to the connector.
Properties properties = new Properties();
properties.setProperty("useSSL", "false");
// Then, we can create a new MySQLConnector with the properties
Credentials credentials = new Credentials("localhost", "3306", "database", "username", "password");
Connector connector = new MySQLConnector(credentials, properties);

// ...
```

### Building and Executing a Query

There are multiple Query builders for different types of queries. The query builders are used to build
a query without having to write the query manually. This makes it easier to write queries and reduces
the chance of making a mistake.

**Examples:**

```java
// -- Create a new SelectQuery with a builder
SelectQuery selectQuery = SelectQuery.builder()
        .setTable("users")
        .setColumns("name", "age")
        .setWhere("name = ?")
        .build();

// -- Then, we can execute the query using the DatabaseManager
// A SELECT query returns a ResultSet, so we need to pass a callback to the executeQuery method
// to handle the result set as we want.
databaseManager.executeQuery(selectQuery, resultSet -> {
    // Do stuff with the result set
});
```

```java
// -- Create a new InsertQuery with a builder
InsertQuery insertQuery = InsertQuery.builder()
        .setTable("users")
        .setColumns(
                new ColumnValuePair("name", "John"),
                new ColumnValuePair("age", 20)
        )
        .build();

// -- Then, we can execute the query using the DatabaseManager
// An INSERT doesn't return a ResultSet, so we can just use the executeUpdate method.
// 
// The INSERT query will require some parameters to be set, because it contains placeholders
// instead of the actual values. We can add parameters directly to the executeUpdate method.
databaseManager.executeUpdate(insertQuery, "John Doe", 25);
```

### ORM

The ORM (Object Relational Mapping) is used to map Java objects to database tables. This makes it
easier to work with the database, because you can use Java objects instead of writing queries.

In this library, ORM is done using the `@DatabaseTable` and `@DatabaseColumn` annotations. The
`@DatabaseTable` annotation is used to mark a class as a database table. The `@DatabaseColumn`
annotation is used to mark a field as a database column.

**Example class with ORM annotations:**

```java

@DatabaseTable(name = "users")
public class User {

    @DatabaseColumn(name = "id", type = "INT", primaryKey = true, autoIncrement = true, notNull = true, unique = true)
    private final int id;

    @DatabaseColumn(name = "name", type = "VARCHAR(16)", notNull = true, unique = true)
    private String name;

    @DatabaseColumn(name = "age", type = "INT", unsigned = true)
    private int age;

    // ...
}
```

**Saving/Loading objects to/from database:**

For this, we will need the `DatabaseManager` that we created earlier.

```java
// -- Create a new User object
User user = new User(0, "John", 20);

// -- Save the user to the database
databaseManager.saveObject(user, true);

// -- Load the user from the database
List<User> loadedUsers = databaseManager.loadORMObjects(User.class, (builder) -> builder.setWhere("name = ?").setValues("John"));
```
