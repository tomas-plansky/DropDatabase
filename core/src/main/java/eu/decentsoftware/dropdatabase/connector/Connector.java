package eu.decentsoftware.dropdatabase.connector;

import eu.decentsoftware.dropdatabase.exception.SQLDriverException;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is used to connect to the database.
 *
 * @author Tomas Plansky
 * @see eu.decentsoftware.dropdatabase.DatabaseManager
 * @since 1.0.0
 */
public interface Connector {

    /**
     * This method is used to connect to the database.
     * <p>
     * Make sure to close the connection after you are done with it.
     *
     * @return The connection to the database.
     * @throws SQLException       If the connection failed.
     * @throws SQLDriverException If the driver is missing. (This should not happen.)
     */
    @Nullable
    Connection connect() throws SQLException, SQLDriverException;

}
