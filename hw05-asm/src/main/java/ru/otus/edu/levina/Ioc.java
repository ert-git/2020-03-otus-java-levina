package ru.otus.edu.levina;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[] { TestLoggingInterface.class }, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface myClass;

        private final Set<String> loggedMethods = new HashSet<>();

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            cacheTrgClassMethods();
        }

        private void cacheTrgClassMethods() {
            Arrays.stream(this.myClass.getClass().getDeclaredMethods())
                    .filter(method -> null != method.getAnnotation(Log.class))
                    .forEach(method -> loggedMethods.add(getMethodId(method)));
        }

        private String getMethodId(Method method) {
            return method.getName() + Arrays.stream(method.getParameterTypes()).collect(Collectors.toList());
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.contains(getMethodId(method))) {
                System.out.println(String.format("executed method: %s, params: %s", method.getName(), Arrays.asList(args)));
            }
            return method.invoke(myClass, args);
        }

    }
}
