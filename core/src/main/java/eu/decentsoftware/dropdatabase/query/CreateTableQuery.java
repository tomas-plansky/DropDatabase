package eu.decentsoftware.dropdatabase.query;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.connector.Connector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTableQuery implements Query {

    private final String query;

    /**
     * Create a new CREATE TABLE query from the given builder.
     *
     * @param builder The builder.
     * @see Builder
     */
    @Contract(pure = true)
    private CreateTableQuery(@NotNull Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        if (builder.ifNotExists) {
            stringBuilder.append("IF NOT EXISTS ");
        }
        if (builder.database != null && !builder.database.isEmpty()) {
            stringBuilder.append("`").append(builder.database).append("`.");
        }
        stringBuilder.append("`").append(builder.table).append("`").append(" (");
        int size = builder.columns.size();
        for (int i = 0; i < size; i++) {
            Column column = builder.columns.get(i);
            stringBuilder.append(column.name).append(" ").append(column.type);
            if (column.primaryKey) {
                stringBuilder.append(" PRIMARY KEY");
            }
            if (column.autoIncrement) {
                stringBuilder.append(" AUTOINCREMENT");
            }
            if (column.notNull) {
                stringBuilder.append(" NOT NULL");
            }
            if (column.unique && !column.primaryKey) {
                stringBuilder.append(" UNIQUE");
            }
            if (column.defaultValue) {
                stringBuilder.append(" DEFAULT ?");
            }
            if (i != size - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        this.query = stringBuilder.toString();
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

    /**
     * Create a new instance of the {@link Builder} class. This builder
     * is used to create a new {@link CreateTableQuery} instance.
     *
     * @return The builder.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder class for the {@link CreateTableQuery} class. This class
     * is used to create a new {@link CreateTableQuery} instance.
     *
     * @author Tomas Plansky
     * @see CreateTableQuery
     * @see CreateTableQuery.Builder#build()
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
         * The columns to create in the table.
         *
         * @see Column
         */
        private final List<Column> columns = new ArrayList<>();
        /**
         * If true, the table will be created only if it does not exist.
         */
        private boolean ifNotExists = false;

        /**
         * Add columns to the table.
         *
         * @param columns The columns to add.
         * @return The builder.
         */
        public Builder addColumns(Column... columns) {
            this.columns.addAll(Arrays.asList(columns));
            return this;
        }

        /**
         * Build the {@link CreateTableQuery} instance.
         *
         * @return The {@link CreateTableQuery} instance.
         */
        @NotNull
        public CreateTableQuery build() {
            return new CreateTableQuery(this);
        }

    }

    /**
     * This class represents a column in a table. It is used in the
     * {@link Builder} class to create a new {@link CreateTableQuery} instance.
     *
     * @author Tomas Plansky
     * @see CreateTableQuery
     * @see CreateTableQuery.Builder#build()
     * @since 1.0.0
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Column {

        private @NotNull String name;
        private @NotNull String type;
        private boolean primaryKey = false;
        private boolean autoIncrement = false;
        private boolean notNull = false;
        private boolean unique = false;

        /**
         * If true, the column will have a default value. The actual value will then be specified
         * when the query is executed as an argument to the PreparedStatement.
         *
         * @see DatabaseManager#executeUpdate(Query)
         */
        private boolean defaultValue = false;

        @Contract(pure = true)
        public Column(@NotNull String name, @NotNull String type) {
            this.name = name;
            this.type = type;
        }

    }

}
