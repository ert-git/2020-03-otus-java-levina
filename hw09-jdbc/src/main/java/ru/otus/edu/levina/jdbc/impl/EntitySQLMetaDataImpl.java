package ru.otus.edu.levina.jdbc.impl;

import java.util.Collections;
import java.util.stream.Collectors;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String insertSql;
    private final String updateSql;
    private final String selectByIdSql;
    private final String selectAllSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMetaData) {
        insertSql = initInsertSql(classMetaData);

        updateSql = initUpdateSql(classMetaData);

        selectByIdSql = initSelectSql(classMetaData);

        selectAllSql = initSelectAllSql(classMetaData);
    }

    private String initSelectAllSql(EntityClassMetaData<?> classMetaData) {
        return new StringBuilder()
                .append("select ")
                .append(classMetaData.getFieldsWithoutId().stream()
                        .map(x -> x.getName())
                        .collect(Collectors.joining(", ")))
                .append(" from ")
                .append(classMetaData.getName())
                .toString();
    }

    private String initSelectSql(EntityClassMetaData<?> classMetaData) {
        return new StringBuilder()
                .append("select ")
                .append(classMetaData.getAllFields().stream()
                        .map(x -> x.getName())
                        .collect(Collectors.joining(", ")))
                .append(" from ")
                .append(classMetaData.getName())
                .append(" where ")
                .append(classMetaData.getIdField().getName())
                .append(" = ?")
                .toString();
    }

    private String initUpdateSql(EntityClassMetaData<?> classMetaData) {
        return new StringBuilder()
                .append("update ")
                .append(classMetaData.getName())
                .append(" set ")
                .append(classMetaData.getFieldsWithoutId().stream()
                        .map(x -> x.getName() + " = ?")
                        .collect(Collectors.joining(", ")))
                .append(" where ")
                .append(classMetaData.getIdField().getName())
                .append(" = ?")
                .toString();
    }

    private String initInsertSql(EntityClassMetaData<?> classMetaData) {
        return new StringBuilder()
                .append("insert into ")
                .append(classMetaData.getName())
                .append(" ( ")
                .append(classMetaData.getFieldsWithoutId().stream()
                        .map(x -> x.getName())
                        .collect(Collectors.joining(", ")))
                .append(" ) values ( ")
                .append(Collections.nCopies(classMetaData.getFieldsWithoutId().size(), "?").stream()
                        .collect(Collectors.joining(", ")))
                .append(")")
                .toString();
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

}
