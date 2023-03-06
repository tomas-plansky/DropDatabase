package eu.decentsoftware.dropdatabase.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CreateDatabaseQuery implements Query {

    private final String query;

    /**
     * Create a new instance of {@link CreateDatabaseQuery}.
     *
     * @param database The name of the database to create.
     */
    @Contract(pure = true)
    public CreateDatabaseQuery(@NotNull String database) {
        this.query = "CREATE DATABASE `" + database + "`";
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
