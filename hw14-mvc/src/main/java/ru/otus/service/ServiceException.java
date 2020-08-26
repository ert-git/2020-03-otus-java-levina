package ru.otus.service;

public class ServiceException extends RuntimeException {
    public ServiceException(Exception e) {
        super(e);
    }
}
