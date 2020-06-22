package ru.otus.edu.levina.jdbc.impl;

import java.util.Collections;
import java.util.stream.Collectors;

import ru.otus.edu.levina.jdbc.mapper.EntityClassMetaData;
import ru.otus.edu.levina.jdbc.mapper.EntitySQLMetaData;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private String insertSql;
    private String updateSql;
    private String selectByIdSql;
    private String selectAllSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMetaData) {
        insertSql = new StringBuilder()
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

        updateSql = new StringBuilder()
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

        selectByIdSql = new StringBuilder()
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

        selectAllSql = new StringBuilder()
                .append("select ")
                .append(classMetaData.getFieldsWithoutId().stream()
                        .map(x -> x.getName())
                        .collect(Collectors.joining(", ")))
                .append(" from ")
                .append(classMetaData.getName())
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
