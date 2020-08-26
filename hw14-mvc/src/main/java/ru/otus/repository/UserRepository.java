package ru.otus.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.hibernate.SessionManager;
import ru.otus.model.User;

public interface UserRepository {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);
    void insertOrUpdate(User user);

    List<User> getAllUsers();
    SessionManager getSessionManager();
}
