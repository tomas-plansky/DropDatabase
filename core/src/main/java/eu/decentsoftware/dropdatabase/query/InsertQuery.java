package eu.decentsoftware.dropdatabase.query;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.connector.Connector;
import eu.decentsoftware.dropdatabase.query.intent.ColumnValuePair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class InsertQuery implements Query {

    private final @NotNull String query;
    private Object[] values;

    /**
     * Create a new INSERT query from the given builder.
     *
     * @param builder The builder.
     * @see Builder
     */
    private InsertQuery(@NotNull Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        if (builder.ignore) {
            stringBuilder.append("INSERT IGNORE INTO ");
        } else {
            stringBuilder.append("INSERT INTO ");
        }
        stringBuilder.append(builder.table).append(" (");
        int length = builder.columns.length;
        this.values = new Object[length];
        for (int i = 0; i < builder.columns.length; i++) {
            this.values[i] = builder.columns[i].getValue();
            stringBuilder.append("`").append(builder.columns[i]).append("`");
            if (i != builder.columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(") VALUES (");
        for (int i = 0; i < builder.columns.length; i++) {
            stringBuilder.append("?");
            if (i != builder.columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
        if (builder.updateOnDuplicate) {
            stringBuilder.append(") ON DUPLICATE KEY UPDATE ");
            for (int i = 0; i < builder.columns.length; i++) {
                stringBuilder.append("`").append(builder.columns[i]).append("`").append(" = ?");
                if (i != builder.columns.length - 1) {
                    stringBuilder.append(", ");
                }
            }
            // Duplicate values for update
            this.values = new Object[length * 2];
            for (int i = 0; i < length; i++) {
                this.values[i] = builder.columns[i].getValue();
                this.values[i + length] = builder.columns[i].getValue();
            }
        } else {
            stringBuilder.append(")");
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
     * is used to create a new {@link InsertQuery} instance.
     *
     * @return The builder.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder class for the {@link InsertQuery} class. This class
     * is used to create a new {@link InsertQuery} instance.
     *
     * @author Tomas Plansky
     * @see InsertQuery
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
         * The columns to insert.
         */
        private ColumnValuePair[] columns;
        /**
         * Whether to update the row if it already exists.
         */
        private boolean updateOnDuplicate;
        /**
         * Whether to ignore the error if the row already exists.
         */
        private boolean ignore;

        /**
         * Set the columns to insert into the database table.
         *
         * @param columns The columns.
         * @return The builder.
         */
        public InsertQuery.Builder setColumns(ColumnValuePair... columns) {
            this.columns = columns;
            return this;
        }

        public InsertQuery build() {
            return new InsertQuery(this);
        }

    }

}
