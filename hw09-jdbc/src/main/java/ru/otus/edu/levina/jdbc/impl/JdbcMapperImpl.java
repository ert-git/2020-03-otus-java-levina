package ru.otus.edu.levina.jdbc.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.otus.edu.levina.jdbc.mapper.DbExecutor;
import ru.otus.edu.levina.jdbc.mapper.EntityClassMetaData;
import ru.otus.edu.levina.jdbc.mapper.EntitySQLMetaData;
import ru.otus.edu.levina.jdbc.mapper.JdbcMapper;
import ru.otus.edu.levina.jdbc.tools.ReflectionHelper;

@Slf4j
public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private EntityClassMetaData<T> classMetaData;
    private EntitySQLMetaData sqlMetaData;

    private final Connection connection;
    private DbExecutor<T> executor;

    public JdbcMapperImpl(Class<T> clazz, Connection connection) {
        validate(clazz);
        this.classMetaData = new EntityClassMetaDataImpl<T>(clazz);
        this.sqlMetaData = new EntitySQLMetaDataImpl(classMetaData);
        this.connection = connection;
        this.executor = new DbExecutorImpl<T>();
    }

    @Override
    public void create(T obj) {
        String sql = sqlMetaData.getInsertSql();
        try {
            long id = executor.executeInsert(connection, sql, ReflectionHelper.getFieldsValues(classMetaData.getFieldsWithoutId(), obj));
            connection.commit();
            log.debug("create: inserted id = {} for {}", id, obj);
        } catch (Exception e) {
            log.error("create failed for {}", obj, e);
        }
    }

    @Override
    public void update(T obj) {
        String sql = sqlMetaData.getUpdateSql();
        try {
            int rowCount = executor.executeUpdate(connection, sql,
                    ReflectionHelper.getFieldsValues(classMetaData.getFieldsWithoutId(), obj),
                    classMetaData.getIdField().get(obj));
            connection.commit();
            log.debug("update: updated {} rows for {}", rowCount, obj);
        } catch (Exception e) {
            log.error("update failed for {}", obj, e);
        }
    }

    @Override
    public T load(long id, Class<T> clazz) {
        try {
            return executor.executeSelect(connection, sqlMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                T newInstance = clazz.getConstructor().newInstance();
                                List<Field> fields = classMetaData.getAllFields();
                                for (Field field : fields) {
                                    field.setAccessible(true);
                                    String fieldName = field.getName();
                                    Object value = rs.getObject(fieldName);
                                    field.set(newInstance, value);
                                }
                                return newInstance;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    }).orElse(null);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private void validate(Class<?> clazz) {
        if (ReflectionHelper.getIdField(clazz) == null) {
            throw new IllegalArgumentException("No @Id annotated field in " + clazz);
        }
        try {
            clazz.getConstructor();
        } catch (Exception e) {
            throw new IllegalArgumentException("No default ctor " + clazz);
        }
    }
}