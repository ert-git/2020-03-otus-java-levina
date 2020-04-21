package ru.otus.edu.levina.fw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    private TestUtils() {
    }

    public static Object newInstance(Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor[] ctors = clazz.getDeclaredConstructors();
        for (Constructor ctor : ctors) {
            if (ctor.getParameterCount() == 0) {
                return ctor.newInstance();
            }
        }
        throw new InstantiationException("You have to declare default (no args) costructor");
    }

    public static Throwable runTestMethod(Method method, Object clazzTestInst) {
        try {
            method.invoke(clazzTestInst);
        } catch (Throwable e) {
            return e.getCause() != null ? e.getCause() : e;
        }
        return null;
    }

    public static void runMethod(Method method, Object clazzTestInst)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.invoke(clazzTestInst);
    }

    @SuppressWarnings("rawtypes")
    public static List<Method> getAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation) {
        List<Method> result = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotation)) {
                result.add(method);
            }
        }
        return result;
    }
}
