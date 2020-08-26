package ru.otus.service;

import java.util.List;
import java.util.Optional;

import ru.otus.model.User;

public interface UserService {

    long saveUser(User user);
    Optional<User> getUser(long id);
    List<User> getAllUsers();
}
