package ru.otus.edu.levina.jdbc.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.otus.edu.levina.jdbc.annotations.Id;

public final class ReflectionHelper {

    public static Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    public static String getFieldsNames(Class<?> clazz) {
        return Arrays.stream(getFields(clazz))
                .map(x -> x.getName())
                .collect(Collectors.joining(", "));
    }

    public static Field getIdField(Class<?> clazz) {
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getDeclaredAnnotation(Id.class) != null) {
                return field;
            }
        }
        return null;
    }

    public static Field[] getNotIdFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> !x.equals(getIdField(clazz)))
                .toArray(size -> new Field[size]);
    }

    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static List<Object> getFieldsValues(List<Field> fields, Object obj)
            throws IllegalArgumentException, IllegalAccessException, SQLException {
        List<Object> values = new ArrayList<Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            values.add(field.get(obj));
        }
        return values;
    }

}
