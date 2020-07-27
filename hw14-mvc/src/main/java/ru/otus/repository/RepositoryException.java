package ru.otus.repository;

public class RepositoryException extends RuntimeException {
    public RepositoryException(Exception ex) {
        super(ex);
    }
}
