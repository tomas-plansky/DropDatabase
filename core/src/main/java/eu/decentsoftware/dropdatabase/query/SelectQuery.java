package eu.decentsoftware.dropdatabase.query;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.connector.Connector;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SelectQuery implements Query {

    private final @NotNull String query;
    private final Object[] values;

    /**
     * Create a new SELECT query from the given builder.
     *
     * @param builder The builder.
     * @see Builder
     */
    private SelectQuery(@NotNull Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        if (builder.columns == null || builder.columns.length == 0) {
            stringBuilder.append("*");
        } else {
            for (int i = 0; i < builder.columns.length; i++) {
                if (builder.columns[i].contains(" AS ")) {
                    stringBuilder.append(builder.columns[i]);
                } else {
                    stringBuilder.append("`").append(builder.columns[i]).append("`");
                }
                if (i != builder.columns.length - 1) {
                    stringBuilder.append(", ");
                }
            }
        }
        stringBuilder.append(" FROM ");
        if (builder.database != null && builder.database.length() > 0) {
            stringBuilder.append("`").append(builder.database).append("`.`");
        }
        stringBuilder.append(builder.table).append("`");
        if (builder.where != null && builder.where.length() > 0) {
            stringBuilder.append(" WHERE ").append(builder.where);
        }
        if (builder.groupBy != null && builder.groupBy.length() > 0) {
            stringBuilder.append(" GROUP BY ").append("`").append(builder.groupBy).append("`");
        }
        if (builder.having != null && builder.having.length() > 0) {
            stringBuilder.append(" HAVING ").append(builder.having);
        }
        if (builder.orderBy != null && builder.orderBy.length() > 0) {
            stringBuilder.append(" ORDER BY ").append("`").append(builder.orderBy).append("`");
        }
        if (builder.limit != 0) {
            stringBuilder.append(" LIMIT ").append(builder.limit);
        }
        if (builder.offset != 0) {
            stringBuilder.append(" OFFSET ").append(builder.offset);
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
     * is used to create a new {@link SelectQuery} instance.
     *
     * @return The builder.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder class for the {@link SelectQuery} class. This class
     * is used to create a new {@link SelectQuery} instance.
     *
     * @author Tomas Plansky
     * @see SelectQuery
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
         * The columns to select. If this is null or empty, all columns
         * will be selected.
         */
        private String[] columns;
        /**
         * The where clause. This is optional.
         */
        private String where;
        /**
         * The order by clause. This is optional.
         */
        private String orderBy;
        /**
         * The group by clause. This is optional.
         */
        private String groupBy;
        /**
         * The having clause. This is optional.
         */
        private String having;
        /**
         * The limit of the query. This is optional. If this is 0, the
         * limit will not be used.
         */
        private int limit;
        /**
         * The offset of the query. This is optional. If this is 0, the
         * offset will not be used.
         */
        private int offset;
        /**
         * The values to replace the placeholders (?) in the final query.
         */
        private Object[] values;

        /**
         * Set the columns to select. If this is null or empty, all columns
         * will be selected.
         *
         * @param columns The columns to select.
         */
        public SelectQuery.Builder setColumns(String... columns) {
            this.columns = columns;
            return this;
        }

        /**
         * Set the values to replace the placeholders (?) in the final query.
         *
         * @param values The values to replace the placeholders (?) in the final query.
         */
        public SelectQuery.Builder setValues(Object... values) {
            this.values = values;
            return this;
        }

        /**
         * Build the {@link SelectQuery} instance.
         *
         * @return The {@link SelectQuery} instance.
         */
        public SelectQuery build() {
            return new SelectQuery(this);
        }

    }


}
