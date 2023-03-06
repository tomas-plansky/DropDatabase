package eu.decentsoftware.dropdatabase.query;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.connector.Connector;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DeleteQuery implements Query {

    private final @NotNull String query;
    private final Object[] values;

    /**
     * Create a new DELETE query from the given builder.
     *
     * @param builder The builder.
     * @see Builder
     */
    private DeleteQuery(@NotNull Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ").append(builder.table);
        if (builder.where != null && builder.where.length() > 0) {
            stringBuilder.append(" WHERE ").append(builder.where);
        }
        this.query = stringBuilder.toString();
        this.values = builder.values;
    }

    @NotNull
    @Override
    public String toString() {
        return query;
    }

    @Override
    public Object[] getValues() {
        return values;
    }

    /**
     * Create a new instance of the {@link Builder} class. This builder
     * is used to create a new {@link DeleteQuery} instance.
     *
     * @return The builder.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder class for the {@link DeleteQuery} class. This builder
     * is used to create a new {@link DeleteQuery} instance.
     *
     * @author Tomas Plansky
     * @see DeleteQuery
     * @see Builder#build()
     * @since 1.0.0
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Builder {

        /**
         * The database in which the table is located. This is optional
         * as long as the database is already defined in the {@link Connector}
         * you used in your {@link DatabaseManager}.
         */
        private String database;
        /**
         * The table in which the data is located.
         */
        private String table;
        /**
         * The where clause. This is optional.
         */
        private String where;
        /**
         * The values to replace the placeholders (?) in the final query.
         */
        private Object[] values;

        /**
         * Set the values to replace the placeholders (?) in the final query.
         *
         * @param values The values to replace the placeholders (?) in the final query.
         */
        public Builder values(Object... values) {
            this.values = values;
            return this;
        }

        /**
         * Build the {@link DeleteQuery} instance.
         *
         * @return The {@link DeleteQuery} instance.
         */
        public DeleteQuery build() {
            return new DeleteQuery(this);
        }

    }

}
