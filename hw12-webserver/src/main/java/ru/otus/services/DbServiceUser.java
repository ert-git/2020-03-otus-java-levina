package ru.otus.services;

import java.util.List;
import java.util.Optional;

import ru.otus.edu.levina.hibernate.model.User;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getAllUsers();
}
