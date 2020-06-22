package ru.otus.edu.levina.jdbc.mapper;

public interface JdbcMapper<T> {

    void create(T objectData);
    
    void update(T objectData);
    
    T load(long id, Class<T> clazz);
    
}