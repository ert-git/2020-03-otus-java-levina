package ru.otus.services;


public class DbServiceException extends RuntimeException {
    public DbServiceException(Exception e) {
        super(e);
    }
}