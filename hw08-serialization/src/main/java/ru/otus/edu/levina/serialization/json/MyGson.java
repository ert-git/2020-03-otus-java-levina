package ru.otus.edu.levina.serialization.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class MyGson {

    private static final char COLON = ':';
    private static final char QUOTE = '"';
    private static final char COMMA = ',';

    public static final Integer MAX_RECURSION_DEPTH = 5;

    private final ThreadLocal<Integer> threadLocalScope = new ThreadLocal<>();

    private final Integer getRecursionDepth() {
        return threadLocalScope.get();
    }

    public final void setRecursionDepth(int len) {
        threadLocalScope.set(len);
    }

    private Map<Class<?>, BiFunction<String, Object, UnaryOperator<StringBuilder>>> typeSerializationMap = new HashMap<>();

    public MyGson() {
        typeSerializationMap.put(String.class, (name, val) -> buf -> toJsonString(name, val, buf));
        typeSerializationMap.put(char.class, (name, val) -> buf -> toJsonString(name, val, buf));
        typeSerializationMap.put(Character.class, (name, val) -> buf -> toJsonString(name, val, buf));
        typeSerializationMap.put(short.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Short.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(int.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Integer.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(long.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Long.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(byte.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Byte.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(float.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Float.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(double.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Double.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(boolean.class, (name, val) -> buf -> toJsonNum(name, val, buf));
        typeSerializationMap.put(Boolean.class, (name, val) -> buf -> toJsonNum(name, val, buf));
    }

    public String toJson(Object any) throws JsonSerializationException {
        if (any == null) {
            return "null";
        }
        try {
            setRecursionDepth(0);
            StringBuilder buf = new StringBuilder(serializeObject(null, any));
            deleteLastComma(buf);
            return buf.toString();
        } catch (JsonSerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonSerializationException(e);
        }
    }

    private String serializeObject(String name, Object val) throws IllegalAccessException, JsonSerializationException {
        if (val == null) {
            return "";
        }
        if (getRecursionDepth() > MAX_RECURSION_DEPTH) {
            throw new JsonSerializationException("Recursion overflow: max = " + MAX_RECURSION_DEPTH);
        }
        StringBuilder buf = new StringBuilder();
        Class<?> valType = val.getClass();
        if (typeSerializationMap.containsKey(valType)) {
            typeSerializationMap.get(valType).apply(name, val).apply(buf);
        } else if (valType.isArray()) {
            buf.append(serializeArray(name, val));
        } else if (val instanceof Collection) {
            buf.append(serializeCollection(name, val));
        } else {
            buf.append(serializeAny(name, val, valType));
        }
        return buf.toString();
    }

    private StringBuilder serializeAny(String name, Object val, Class<?> valType)
            throws IllegalAccessException, JsonSerializationException {
        StringBuilder buf = new StringBuilder();
        setRecursionDepth(getRecursionDepth() + 1);
        putName(name, buf);
        buf.append("{");
        Field[] fields = valType.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!isSerializable(f)) {
                continue;
            }
            buf.append(serializeObject(f.getName(), f.get(val)));
        }
        deleteLastComma(buf);
        buf.append("},");
        setRecursionDepth(getRecursionDepth() - 1);
        return buf;
    }

    private StringBuilder serializeCollection(String name, Object val) throws IllegalAccessException, JsonSerializationException {
        StringBuilder buf = new StringBuilder();
        putName(name, buf);
        buf.append("[");
        Collection col = (Collection) val;
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            buf.append(serializeObject(null, iterator.next()));
        }
        deleteLastComma(buf);
        buf.append("],");
        return buf;
    }

    private StringBuilder serializeArray(String name, Object val) throws IllegalAccessException, JsonSerializationException {
        StringBuilder buf = new StringBuilder();
        putName(name, buf);
        buf.append("[");
        int len = Array.getLength(val);
        for (int i = 0; i < len; i++) {
            buf.append(serializeObject(null, Array.get(val, i)));
        }
        deleteLastComma(buf);
        buf.append("],");
        return buf;
    }

    private void deleteLastComma(StringBuilder buf) {
        if (buf.charAt(buf.length() - 1) == COMMA) {
            buf.delete(buf.length() - 1, buf.length());
        }
    }

    private void putName(String name, StringBuilder buf) {
        if (name != null) {
            buf.append(QUOTE).append(name).append(QUOTE).append(COLON);
        }
    }

    private StringBuilder toJsonString(String name, Object val, StringBuilder buf) {
        if (val != null) {
            putName(name, buf);
            buf.append(QUOTE).append(val).append(QUOTE).append(COMMA);
        }
        return buf;
    }

    private StringBuilder toJsonNum(String name, Object val, StringBuilder buf) {
        if (val != null) {
            putName(name, buf);
            buf.append(val).append(COMMA);
        }
        return buf;
    }

    private static boolean isSerializable(Field f) {
        return (f.getModifiers() & Modifier.TRANSIENT) != Modifier.TRANSIENT
                && (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC;
    }
    
}
