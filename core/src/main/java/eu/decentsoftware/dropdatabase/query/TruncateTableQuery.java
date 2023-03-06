package eu.decentsoftware.dropdatabase.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TruncateTableQuery implements Query {

    private final String query;

    /**
     * Create a new TruncateTableQuery.
     *
     * @param table The table to truncate.
     */
    @Contract(pure = true)
    public TruncateTableQuery(@NotNull String table) {
        this.query = "TRUNCATE TABLE `" + table + "`";
    }

    /**
     * Create a new TruncateTableQuery.
     *
     * @param database The database to truncate the table from.
     * @param table    The table to truncate.
     */
    @Contract(pure = true)

    public TruncateTableQuery(@NotNull String database, @NotNull String table) {
        this.query = "TRUNCATE TABLE `" + database + "`.`" + table + "`";
    }

    @NotNull
    @Override
    public String toString() {
        return query;
    }

    @Override
    public Object[] getValues() {
        return new Object[0];
    }

}
