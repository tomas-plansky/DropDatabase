package eu.decentsoftware.dropdatabase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a field as a database column.
 * <p>
 * The name of the column in the database is determined by the {@link DatabaseColumn#name()} annotation.
 * The type of the column in the database is determined by the {@link DatabaseColumn#type()} annotation.
 * <p>
 * The {@link DatabaseColumn#primaryKey()} annotation is used to mark the column as a primary key.
 * The {@link DatabaseColumn#autoIncrement()} annotation is used to mark the column as auto increment.
 * The {@link DatabaseColumn#notNull()} annotation is used to mark the column as not null.
 * The {@link DatabaseColumn#unique()} annotation is used to mark the column as unique.
 * <p>
 * Columns in the database are NOT created automatically. You must create them manually or
 * use the {@link eu.decentsoftware.dropdatabase.query.CreateDatabaseQuery} class.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseColumn {

    String name();

    String type();

    boolean primaryKey() default false;

    boolean autoIncrement() default false;

    boolean notNull() default false;

    boolean unique() default false;

    boolean unsigned() default false;

}
