package eu.decentsoftware.dropdatabase;

import eu.decentsoftware.dropdatabase.annotations.DatabaseColumn;
import eu.decentsoftware.dropdatabase.annotations.DatabaseTable;
import eu.decentsoftware.dropdatabase.connector.Connector;
import eu.decentsoftware.dropdatabase.exception.SQLConnectionException;
import eu.decentsoftware.dropdatabase.exception.SQLQueryException;
import eu.decentsoftware.dropdatabase.internal.ORMUtil;
import eu.decentsoftware.dropdatabase.query.CreateTableQuery;
import eu.decentsoftware.dropdatabase.query.InsertQuery;
import eu.decentsoftware.dropdatabase.query.Query;
import eu.decentsoftware.dropdatabase.query.SelectQuery;
import jdk.internal.misc.Unsafe;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class manages the connection to the database and provides methods to execute queries. It
 * basically serves as the main access point to the database.
 * <p>
 * You can execute queries using the {@link #executeQuery(Query, Consumer)} method, which
 * will handle the connection and result set for you. It takes a {@link Consumer} as a parameter, which
 * will be called with the result set as a parameter. This method is useful for queries that return a
 * result.
 * <p>
 * You can also execute queries using the {@link #executeUpdate(Query)} method, which will
 * handle the connection for you. This method is useful for queries that do not return a result.
 * <p>
 * To execute custom queries, without using the {@link Query} interface, you can use the
 * {@link #executeQuery(String, Consumer, Object...)} method or the {@link #executeUpdate(String, Object...)}
 * method, which will also handle the connection (and result set) for you.
 * <p>
 * You can also create multiple instances of this class, each with its own {@link Connector}. This
 * allows you to connect to multiple databases at the same time.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
public class DatabaseManager {

    private final @NonNull Connector connector;
    private Connection connection;

    /**
     * Create a new instance of {@link DatabaseManager}. This class manages the connection to the database
     * and provides methods to execute queries.
     *
     * @param connector The connector to the database.
     * @see Connector
     */
    @Contract(pure = true)
    public DatabaseManager(@NonNull Connector connector) {
        this.connector = connector;
    }

    /**
     * Returns the connection to the database. This method will attempt to reconnect if the connection is closed
     * or invalid.
     *
     * @return The connection to the database.
     * @throws SQLConnectionException If the connection failed.
     */
    @NonNull
    public Connection getConnection() throws SQLConnectionException {
        try {
            if (connection != null && connection.isValid(5)) {
                return connection;
            }

            connection = connector.connect();
            if (connection == null || connection.isClosed()) {
                throw new SQLConnectionException("Failed to connect to the database.");
            }

            return connection;
        } catch (SQLException e) {
            throw new SQLConnectionException("Failed to connect to the database.", e);
        }
    }

    /**
     * Closes the connection to the database if it is open.
     *
     * @throws SQLConnectionException If the connection failed to close.
     */
    public void close() throws SQLConnectionException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLConnectionException("Failed to close the connection.", e);
        }
    }

    /**
     * Executes a query to the database and handles the result. This method should be used
     * for queries that return a result.
     *
     * @param query          The query to execute.
     * @param resultCallback The callback to handle the result.
     * @param args           The arguments to replace in the query.
     * @throws SQLQueryException If the query failed.
     */
    protected void executeQuery(@NonNull String query, @NonNull Consumer<ResultSet> resultCallback, Object... args) throws SQLQueryException {
        final Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Replace custom arguments with the actual values. (? -> value)
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // Execute the query.
            try (ResultSet rs = ps.executeQuery()) {
                resultCallback.accept(rs);
            }
        } catch (SQLException e) {
            throw new SQLQueryException("Failed to execute query.", e);
        }
    }

    /**
     * Executes a query to the database and handles the result. This method should be used
     * for queries that return a result.
     *
     * @param query          The query to execute.
     * @param resultCallback The callback to handle the result.
     * @throws SQLQueryException If the query failed.
     */
    public void executeQuery(@NonNull Query query, @NonNull Consumer<ResultSet> resultCallback) throws SQLQueryException {
        executeQuery(query.toString(), resultCallback, query.getValues());
    }

    /**
     * Executes a query to the database. This method should be used for queries that do not
     * return a result.
     *
     * @param query The query to execute.
     * @param args  The arguments to replace in the query.
     * @throws SQLQueryException If the query failed.
     */
    protected void executeUpdate(@NonNull String query, Object... args) throws SQLQueryException {
        final Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Replace custom arguments with the actual values. (? -> value)
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLQueryException("Failed to execute query.", e);
        }
    }

    /**
     * Executes a query to the database. This method should be used for queries that do not
     * return a result.
     *
     * @param query The query to execute.
     * @throws SQLQueryException If the query failed.
     */
    public void executeUpdate(@NonNull Query query) throws SQLQueryException {
        executeUpdate(query.toString(), query.getValues());
    }

    /**
     * Saves an object to the database. This method will use the annotations on the object to
     * determine the table and columns to use.
     * <p>
     * To save an object, it must have a {@link DatabaseTable} annotation. This annotation
     * determines the table to use. The object must also have a field with the {@link DatabaseColumn}
     * annotation. This annotation determines the column to use.
     *
     * @param object The object to save.
     * @throws SQLQueryException If the query failed.
     */
    public void saveORMObject(@NotNull Object object) throws SQLQueryException {
        saveORMObject(object, true);
    }

    /**
     * Saves an object to the database. This method will use the annotations on the objects class to
     * determine the table and columns to use.
     * <p>
     * To save an object, it must have a {@link DatabaseTable} annotation. This annotation
     * determines the table to use. The object must also have a field with the {@link DatabaseColumn}
     * annotation. This annotation determines the column to use.
     *
     * @param object            The object to save.
     * @param updateOnDuplicate If the object should be updated if it already exists.
     * @throws SQLQueryException If the query failed.
     */
    public void saveORMObject(@NotNull Object object, boolean updateOnDuplicate) throws SQLQueryException {
        DatabaseTable table = object.getClass().getAnnotation(DatabaseTable.class);
        if (table == null) {
            throw new SQLQueryException("Class " + object.getClass().getName() + " is not a database object.");
        }

        executeUpdate(InsertQuery.builder()
                .setDatabase(table.database())
                .setTable(table.name())
                .setColumns(ORMUtil.getValues(object))
                .setUpdateOnDuplicate(updateOnDuplicate)
                .setIgnore(!updateOnDuplicate)
                .build());
    }

    /**
     * Loads a list of all objects of the given type from the database by mapping the table
     * to the object. This method will use the annotations on the class to determine the table
     * and columns to use.
     *
     * @param clazz                The class of the object to load.
     * @param queryBuilderCallback The callback to build the query. This callback will be called
     *                             after the query builder is ready to use. You can specify the
     *                             where clauses and other options here. Table, Database and Columns
     *                             will be set automatically after this callback.
     * @return A list of all objects of the given type loaded from the database.
     * @throws SQLQueryException If the query failed.
     */
    public <T> List<T> loadORMObjects(@NotNull Class<T> clazz, @NotNull Consumer<SelectQuery.Builder> queryBuilderCallback) throws SQLQueryException {
        List<T> objects = new ArrayList<>();
        DatabaseTable table = clazz.getAnnotation(DatabaseTable.class);
        if (table == null) {
            throw new SQLQueryException("Class " + clazz.getName() + " is not a database object.");
        }
        String[] columns = ORMUtil.getColumnNames(clazz);

        SelectQuery.Builder builder = SelectQuery.builder();
        queryBuilderCallback.accept(builder);
        builder.setDatabase(table.database())
                .setTable(table.name())
                .setColumns(columns);

        SelectQuery query = builder.build();
        executeQuery(query, (rs) -> {
            try {
                while (rs.next()) {
                    T object = (T) Unsafe.getUnsafe().allocateInstance(clazz);
                    for (String column : columns) {
                        Object value = rs.getObject(column);
                        ORMUtil.setFieldValue(object, column, value);
                    }
                    objects.add(object);
                }
            } catch (SQLException | InstantiationException e) {
                throw new SQLQueryException(e);
            }
        });
        return objects;
    }

    /**
     * Creates a table in the database for the given class. This method will use the annotations
     * on the class to determine the table and columns to use.
     * <p>
     * To create a table, the class must have a {@link DatabaseTable} annotation. This annotation
     * determines the table to use. The class must also have a field with the {@link DatabaseColumn}
     * annotation. This annotation determines the column to use.
     *
     * @param clazz       The class to create a table for.
     * @param ifNotExists If the table should only be created if it does not exist.
     * @throws SQLQueryException If the query failed.
     * @see CreateTableQuery
     */
    public void createORMTable(@NotNull Class<?> clazz, boolean ifNotExists) throws SQLQueryException {
        DatabaseTable table = clazz.getAnnotation(DatabaseTable.class);
        if (table == null) {
            throw new SQLQueryException("Class " + clazz.getName() + " is not a database object.");
        }

        CreateTableQuery.Builder builder = CreateTableQuery.builder()
                .setIfNotExists(ifNotExists)
                .setDatabase(table.database())
                .setTable(table.name());

        Arrays.stream(clazz.getDeclaredFields())
                .filter(ORMUtil::isDatabaseColumn)
                .map((field) -> {
                    DatabaseColumn column = field.getAnnotation(DatabaseColumn.class);
                    return new CreateTableQuery.Column(
                            column.name(),
                            column.type(),
                            column.primaryKey(),
                            column.autoIncrement(),
                            column.notNull(),
                            column.unique(),
                            false
                    );
                })
                .forEach(builder::addColumns);

        CreateTableQuery query = builder.build();
        executeUpdate(query);
    }

}
