package eu.decentsoftware.dropdatabase.query;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.connector.Connector;
import eu.decentsoftware.dropdatabase.query.intent.ColumnValuePair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UpdateQuery implements Query {

    private final @NotNull String query;
    private final Object[] values;

    /**
     * Create a new UPDATE query from the given builder.
     *
     * @param builder The builder.
     * @see Builder
     */
    private UpdateQuery(@NotNull Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ").append(builder.table).append(" SET ");
        this.values = new Object[builder.columns.length];
        for (int i = 0; i < builder.columns.length; i++) {
            ColumnValuePair column = builder.columns[i];
            stringBuilder.append(column.getColumnName()).append(" = ?, ");
            this.values[i] = column.getValue();
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        if (builder.where != null && builder.where.length() > 0) {
            stringBuilder.append(" WHERE ").append(builder.where);
        }
        this.query = stringBuilder.toString();
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
     * is used to create a new {@link UpdateQuery} instance.
     *
     * @return The builder.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder class for the {@link UpdateQuery} class. This class
     * is used to create a new {@link UpdateQuery} instance.
     *
     * @author Tomas Plansky
     * @see UpdateQuery
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
         * The columns to update.
         */
        private ColumnValuePair[] columns;
        /**
         * The where clause. This is optional.
         */
        private String where;

        /**
         * Set the columns to update. Duplicate columns will be ignored.
         *
         * @param columns Columns to update.
         * @return Builder instance.
         */
        @Contract("_ -> this")
        public Builder setColumns(ColumnValuePair... columns) {
            this.columns = columns;
            return this;
        }

        /**
         * Build the {@link UpdateQuery}.
         *
         * @return The new {@link UpdateQuery}.
         */
        public UpdateQuery build() {
            return new UpdateQuery(this);
        }

    }

}
