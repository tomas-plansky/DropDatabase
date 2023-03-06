package eu.decentsoftware.dropdatabase.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DropDatabaseQuery implements Query {

    private final String query;

    /**
     * Create a new instance of {@link DropDatabaseQuery}.
     *
     * @param database The database to drop.
     */
    @Contract(pure = true)
    public DropDatabaseQuery(@NotNull String database) {
        this.query = "DROP DATABASE `" + database + "`";
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
