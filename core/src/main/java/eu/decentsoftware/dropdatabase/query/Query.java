package eu.decentsoftware.dropdatabase.query;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a query. It is used to create a query and to store
 * the values to replace the placeholders (?) in the final query.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
public interface Query {

	/**
	 * Returns the query as a String.
	 *
	 * @return The String.
	 */
	@NotNull
	@MustBeInvokedByOverriders
	@Override
	String toString();

	/**
	 * Get the values of the query.
	 * <p>
	 * The values are used to replace the placeholders (?) in the final query. The
	 * values will be set in the order they are passed.
	 */
	Object[] getValues();

}
