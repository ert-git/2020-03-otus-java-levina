package ru.otus.core.dao;

import java.util.Optional;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.model.User;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);
    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
