package ru.otus.core.service;

import java.util.Optional;

import ru.otus.edu.levina.jdbc.model.Account;

public interface DBServiceAccount {

    long saveAccount(Account user);

    Optional<Account> getAccount(long id);
}
