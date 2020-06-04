package ru.otus.edu.levina.serialization.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

    private static final Integer MAX_RECURSION_DEPTH = 5;

    private final ThreadLocal<Integer> threadLocalScope = new ThreadLocal<>();

    private final Integer getRecursionDepth() {
        return threadLocalScope.get();
    }

    public final void setRecursionDepth(int len) {
        threadLocalScope.set(len);
    }

    private Map<String, BiFunction<String, Object, UnaryOperator<StringBuilder>>> map = new HashMap<>();

    public MyGson() {
        // use strings to avoid coping for primitive and wrappers (like Double/double)
        map.put("string", (name, val) -> buf -> toJsonString(name, val, buf));
        map.put("int", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("long", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("byte", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("float", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("double", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("boolean", (name, val) -> buf -> toJsonNum(name, val, buf));
        map.put("integer", (name, val) -> buf -> toJsonNum(name, val, buf));
    }

    
    public String toJson(Object any) throws RuntimeException {
        if (any == null) {
            return null;
        }
        try {
            setRecursionDepth(0);
            StringBuilder buf = new StringBuilder();
            serializeObject(null, any, buf);
            deleteLastComma(buf);
            return buf.toString();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void serializeObject(String name, Object val, StringBuilder buf) throws IllegalAccessException {
        if (val == null) {
            return;
        }
        if (getRecursionDepth() > MAX_RECURSION_DEPTH) {
            throw new RuntimeException("Recursion overflow: max = " + MAX_RECURSION_DEPTH);
        }
        Class<?> valType = val.getClass();
        String valTypeName = valType.getSimpleName().toLowerCase();
        if (map.containsKey(valTypeName)) {
            map.get(valTypeName).apply(name, val).apply(buf);
        } else if (valType.isArray()) {
            putName(name, buf);
            buf.append("[");
            int len = Array.getLength(val);
            for (int i = 0; i < len; i++) {
                serializeObject(null, Array.get(val, i), buf);
            }
            deleteLastComma(buf);
            buf.append("],");
        } else if (val instanceof Collection) {
            putName(name, buf);
            buf.append("[");
            Collection col = (Collection) val;
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                serializeObject(null, iterator.next(), buf);
            }
            deleteLastComma(buf);
            buf.append("],");
        } else {
            setRecursionDepth(getRecursionDepth() + 1);
            putName(name, buf);
            buf.append("{");
            Field[] fields = valType.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                serializeObject(f.getName(), f.get(val), buf);
            }
            deleteLastComma(buf);
            buf.append("},");
            setRecursionDepth(getRecursionDepth() - 1);
        }
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

}
