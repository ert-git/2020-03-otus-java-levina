package ru.otus.edu.levina.jdbc.mapper;


public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}