package eu.decentsoftware.dropdatabase.connector;

import eu.decentsoftware.dropdatabase.Credentials;
import eu.decentsoftware.dropdatabase.exception.SQLDriverException;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class is an implementation of {@link Connector}, which is used to connect to a MySQL database.
 * <p>
 * MySQL is a relational database, which is used to store data in a database. This connector is used to
 * connect to a MySQL database. The only thing you need to provide is the credentials to the database.
 *
 * @author Tomas Plansky
 * @see Connector
 * @see Credentials
 * @since 1.0.0
 */
public class MySQLConnector implements Connector {

	/**
	 * The URL to the database.
	 *
	 * @implNote We simply create the URL in constructor and store it,
	 * so we don't have to create it every time we connect.
	 */
	private final @NonNull String url;

	/**
	 * The properties of the database connection.
	 */
	private final @Nullable Properties properties;

	/**
	 * Create a new instance of {@link MySQLConnector}. This connector
	 * is used to connect to a MySQL database. The only thing you need
	 * to provide is the credentials to the database.
	 * <p>
	 * Optionally, you can provide properties to the database. These
	 * properties are used to configure the connection to the database.
	 * You can find the list of properties
	 * <a href="https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html">here</a>.
	 *
	 * @param credentials The credentials to the database.
	 * @param properties  The properties to the database. (Can be null)
	 * @since 1.0.0
	 */
	@Contract(pure = true)
	public MySQLConnector(@NonNull Credentials credentials, @Nullable Properties properties) {
		this.url = String.format(
				"jdbc:mysql://%s:%s/%s",
				credentials.getHost(),
				credentials.getPort(),
				credentials.getDatabase() == null ? "" : credentials.getDatabase()
		);
		this.properties = properties;
	}

	/**
	 * Create a new instance of {@link MySQLConnector}. This connector
	 * is used to connect to a MySQL database. The only thing you need
	 * to provide is the credentials to the database.
	 *
	 * @param credentials The credentials to the database.
	 * @since 1.0.0
	 */
	@Contract(pure = true)
	public MySQLConnector(@NonNull Credentials credentials) {
		this(credentials, null);
	}

	@Override
	public Connection connect() throws SQLException, SQLDriverException {
		try {
			// Load the driver.
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Connect to the database.
			if (properties == null) {
				return DriverManager.getConnection(url);
			} else {
				return DriverManager.getConnection(url, properties);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLDriverException("Driver for MySQL is missing.", e);
		}
	}

}
