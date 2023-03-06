package eu.decentsoftware.dropdatabase.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DropTableQuery implements Query {

    private final @NotNull String query;

    /**
     * Create a new instance of {@link DropTableQuery}.
     *
     * @param table The table to drop.
     */
    @Contract(pure = true)
    public DropTableQuery(@NotNull String table) {
        this.query = "DROP TABLE `" + table + "`";
    }

    /**
     * Create a new instance of {@link DropTableQuery}.
     *
     * @param database The database to drop the table from.
     * @param table    The table to drop.
     */
    @Contract(pure = true)
    public DropTableQuery(@NotNull String database, @NotNull String table) {
        this.query = "DROP TABLE `" + database + "`.`" + table + "`";
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
