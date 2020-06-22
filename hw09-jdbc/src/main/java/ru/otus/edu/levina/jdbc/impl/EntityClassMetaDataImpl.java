package ru.otus.edu.levina.jdbc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import ru.otus.edu.levina.jdbc.mapper.EntityClassMetaData;
import ru.otus.edu.levina.jdbc.tools.ReflectionHelper;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private String name;
    private List<Field> fields;
    private Field idField;
    private List<Field> notIdFields;
    private Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        name = clazz.getSimpleName();
        fields = Arrays.asList(ReflectionHelper.getFields(clazz));
        idField = ReflectionHelper.getIdField(clazz);
        idField.setAccessible(true);
        notIdFields = Arrays.asList(ReflectionHelper.getNotIdFields(clazz));
        constructor = ReflectionHelper.getDefaultConstructor(clazz);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return notIdFields;
    }

    @Override
    public Field getIdField() {
        return idField;
    }
}