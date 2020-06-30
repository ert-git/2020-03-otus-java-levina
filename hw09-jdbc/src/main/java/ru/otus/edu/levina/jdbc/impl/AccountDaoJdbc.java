package ru.otus.edu.levina.jdbc.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.model.Account;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;

public class AccountDaoJdbc implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final SessionManager sessionManager;
    private final JdbcMapper<Account> mapper;

    public AccountDaoJdbc(SessionManager sessionManager, DbExecutor<Account> dbExecutor,
            EntityClassMetaData<Account> entityMeta,
            EntitySQLMetaData sqlMeta) {
        this.sessionManager = sessionManager;
        this.mapper = new JdbcMapperImpl<>(entityMeta, sqlMeta, sessionManager, dbExecutor);
    }

    @Override
    public Optional<Account> findById(long id) {
        return Optional.of(mapper.findById(id, Account.class));
    }

    @Override
    public long insertAccount(Account acc) {
        mapper.insert(acc);
        return acc.getNo();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public void updateAccount(Account acc) {
        mapper.update(acc);
    }

    @Override
    public void insertOrUpdate(Account acc) {
        mapper.insertOrUpdate(acc);

    }
}
