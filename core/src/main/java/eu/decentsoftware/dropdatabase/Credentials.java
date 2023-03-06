package eu.decentsoftware.dropdatabase;

import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to store the credentials for connecting to the database.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
@ToString(exclude = "password")
@AllArgsConstructor
public final class Credentials {

	private final @NonNull String host;
	private final @NonNull String port;
	private final @NonNull String username;
	private final @NonNull String password;

	/**
	 * The name of the database to connect to. Can be null.
	 *
	 * @implNote This can be null, because we don't need to connect to a specific database, we can
	 * connect to the server and access all the databases. Make sure to specify the database in the
	 * query before table names if you leave this null.
	 */
	private final @Nullable String database;

}
