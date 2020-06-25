package ru.otus.core.dao;

import java.util.Optional;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.model.Account;

public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount(Account user);

    void updateAccount(Account user);
    void insertOrUpdate(Account user);

    SessionManager getSessionManager();
}
