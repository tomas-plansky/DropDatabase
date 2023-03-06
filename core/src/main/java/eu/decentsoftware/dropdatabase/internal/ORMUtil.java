package eu.decentsoftware.dropdatabase.internal;

import eu.decentsoftware.dropdatabase.DatabaseManager;
import eu.decentsoftware.dropdatabase.annotations.DatabaseColumn;
import eu.decentsoftware.dropdatabase.query.intent.ColumnValuePair;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class for ORM. This class is not intended to be used by the end user
 * and is only used internally by the {@link DatabaseManager}.
 *
 * @author Tomas Plansky
 * @since 1.0.0
 */
@ApiStatus.Internal
@UtilityClass
public final class ORMUtil {

    public static ColumnValuePair[] getValues(@NotNull Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DatabaseColumn.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return new ColumnValuePair(field.getAnnotation(DatabaseColumn.class).name(), field.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(ColumnValuePair[]::new);
    }

    public static String[] getColumnNames(@NotNull Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(ORMUtil::isDatabaseColumn)
                .map(field -> {
                    field.setAccessible(true);
                    DatabaseColumn columnAnnotation = field.getAnnotation(DatabaseColumn.class);
                    return columnAnnotation.name();
                })
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }

    public static void setFieldValue(@NotNull Object parent, @NotNull String fieldName, Object value) {
        try {
            Field field = parent.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                field.set(null, value);
            } else {
                field.set(parent, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDatabaseColumn(@NotNull Field field) {
        return !Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(DatabaseColumn.class);
    }

}
