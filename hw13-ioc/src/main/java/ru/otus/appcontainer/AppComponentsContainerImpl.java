package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@Slf4j
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByType = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> methods = getComponentMethods(configClass);
        try {
            Object cfg = configClass.newInstance();
            for (Method method : methods) {
                AppComponent annotation = method.getAnnotation(AppComponent.class);
                String componentName = annotation.name();
                if (appComponentsByName.containsKey(componentName)) {
                    throw new RuntimeException(String.format("Duplicated component name %s", componentName));
                }
                Class<?> returnType = method.getReturnType();
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    args[i] = getAppComponent(parameters[i].getType());
                    if (args[i] == null) {
                        throw new RuntimeException(
                                String.format("No component found for class %s", parameters[i].getType().getName()));
                    }
                }
                Object component = method.invoke(cfg, args);
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
                appComponentsByType.put(returnType, component);
                log.info("created component {} ({}) of {} ", componentName, component, returnType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Method> getComponentMethods(Class<?> configClass) {
        return Arrays
                .stream(configClass.getMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted((m1, m2) -> m1.getAnnotation(AppComponent.class).order() - m2.getAnnotation(AppComponent.class).order())
                .collect(Collectors.toList());

    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Optional<Object> component = appComponents.stream().filter(c -> c.getClass() == componentClass).findFirst();
        if (component.isPresent()) {
            return (C) component.get();
        } else {
            return (C) appComponentsByType.get(componentClass);
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

}
