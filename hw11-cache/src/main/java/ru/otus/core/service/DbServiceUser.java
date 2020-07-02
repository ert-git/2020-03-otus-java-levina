package ru.otus.core.service;

import java.util.Optional;

import ru.otus.edu.levina.hibernate.model.User;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    void deleteUser(User user);
}
