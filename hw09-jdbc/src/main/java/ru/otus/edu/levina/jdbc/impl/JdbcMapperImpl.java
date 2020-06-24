package ru.otus.edu.levina.jdbc.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.tools.ReflectionHelper;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;

@Slf4j
public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private EntityClassMetaData<T> classMetaData;
    private EntitySQLMetaData sqlMetaData;

    private DbExecutor<T> executor;
    private SessionManager sessionManager;

    public JdbcMapperImpl(EntityClassMetaData<T> classMetaData, EntitySQLMetaData sqlMetaData, SessionManager sessionManager, DbExecutorImpl<T> dbExecutor) {
        validate(classMetaData);
        this.sessionManager = sessionManager;
        this.classMetaData = classMetaData;
        this.sqlMetaData = sqlMetaData;
        this.executor = dbExecutor;
    }

    @Override
    public void insert(T obj) {
        String sql = sqlMetaData.getInsertSql();
        try {
            sessionManager.beginSession();
            long id = executor.executeInsert(sessionManager.getCurrentSession().getConnection(), sql,
                    ReflectionHelper.getFieldsValues(classMetaData.getFieldsWithoutId(), obj));
            classMetaData.getIdField().set(obj, id);
            sessionManager.commitSession();
            log.debug("create: inserted id = {} for {}", id, obj);
        } catch (Exception e) {
            log.error("create failed for {}", obj, e);
            sessionManager.rollbackSession();
        }
    }

    @Override
    public void update(T obj) {
        String sql = sqlMetaData.getUpdateSql();
        try {
            sessionManager.beginSession();
            int rowCount = executor.executeUpdate(sessionManager.getCurrentSession().getConnection(), sql,
                    ReflectionHelper.getFieldsValues(classMetaData.getFieldsWithoutId(), obj),
                    classMetaData.getIdField().get(obj));
            sessionManager.commitSession();
            log.debug("update: updated {} rows for {}", rowCount, obj);
        } catch (Exception e) {
            log.error("update failed for {}", obj, e);
            sessionManager.rollbackSession();
        }
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        try {
            T result = executor.executeSelect(sessionManager.getCurrentSession().getConnection(), sqlMetaData.getSelectByIdSql(),
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
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private void validate(EntityClassMetaData<T> classMetaData) {
        if (classMetaData.getIdField() == null) {
            throw new IllegalArgumentException("No @Id annotated field in " + classMetaData);
        }
        if (classMetaData.getConstructor() == null) {
            throw new IllegalArgumentException("No default ctor " + classMetaData);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            Object id = classMetaData.getIdField().get(objectData);
            if (id == null || ((long) id) == 0) {
                insert(objectData);
            } else {
                update(objectData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}