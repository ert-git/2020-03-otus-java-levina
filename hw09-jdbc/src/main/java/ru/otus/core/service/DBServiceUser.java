package ru.otus.core.service;

import java.util.Optional;

import ru.otus.edu.levina.jdbc.model.User;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
