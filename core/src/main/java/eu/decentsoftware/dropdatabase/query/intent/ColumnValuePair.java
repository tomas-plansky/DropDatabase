package eu.decentsoftware.dropdatabase.query.intent;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to hold the name of the column in a database table
 * and the value of that column.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
@Data
public class ColumnValuePair {

    /**
     * The name of the column in a database table.
     */
    private final @NotNull String columnName;
    /**
     * The value of the column in a database table.
     */
    private final @Nullable Object value;

}
