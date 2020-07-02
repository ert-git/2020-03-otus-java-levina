package ru.otus.core.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.hibernate.model.User;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();

    Optional<User> findByLogin(String login);

    List<User> getAllUsers();
}
