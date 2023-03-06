package eu.decentsoftware.dropdatabase.annotations;

import eu.decentsoftware.dropdatabase.query.CreateDatabaseQuery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a class as a database table.
 * <p>
 * The name of the table in the database is determined by the {@link DatabaseTable#name()} annotation.
 * The name of the database is determined by the {@link DatabaseTable#database()} annotation.
 * <p>
 * Tables in the database are NOT created automatically. You must create them manually or
 * use the {@link CreateDatabaseQuery} class.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseTable {

    String name();

    String database();

}
