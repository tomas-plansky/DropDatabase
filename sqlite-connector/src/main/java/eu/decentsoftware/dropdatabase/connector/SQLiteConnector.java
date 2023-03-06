package eu.decentsoftware.dropdatabase.connector;

import eu.decentsoftware.dropdatabase.exception.SQLDriverException;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is an implementation of {@link Connector}, which is used to connect to a SQLite database.
 * <p>
 * SQLite is a lightweight database, which is used to store data in a file. This connector is used to
 * connect to a SQLite database. The only thing you need to provide is the path to the database file.
 *
 * @author Tomas Plansky
 * @see Connector
 * @since 1.0.0
 */
public class SQLiteConnector implements Connector {

    /**
     * The URL to the database.
     *
     * @implNote We simply create the URL in constructor and store it,
     * so we don't have to create it every time we connect.
     */
    private final @NonNull String url;

    /**
     * Create a new instance of {@link SQLiteConnector}. This connector
     * is used to connect to a SQLite database. The only thing you need
     * to provide is the path to the database file.
     *
     * @param filePath The path to the database file.
     * @implNote The path should be absolute.
     */
    @Contract(pure = true)
    public SQLiteConnector(@NonNull String filePath) {
        this.url = String.format(
                "jdbc:sqlite:%s",
                filePath
        );
    }

    @Override
    public Connection connect() throws SQLException, SQLDriverException {
        try {
            // Load the driver.
            Class.forName("org.sqlite.JDBC");

            // Connect to the database.
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            // This should not happen.
            throw new SQLDriverException("Driver for SQLite is missing.", e);
        }
    }

}
