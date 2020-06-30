package ru.otus.edu.levina.jdbc.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.model.User;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;

public class UserDaoJdbc implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

    private final SessionManager sessionManager;
    private final JdbcMapper<User> mapper;

    public UserDaoJdbc(SessionManager sessionManager, DbExecutor<User> dbExecutor,
            EntityClassMetaData<User> entityMeta,
            EntitySQLMetaData sqlMeta) {
        this.sessionManager = sessionManager;
        this.mapper = new JdbcMapperImpl<>(entityMeta, sqlMeta, sessionManager, dbExecutor);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.of(mapper.findById(id, User.class));
    }

    @Override
    public long insertUser(User user) {
        mapper.insert(user);
        return user.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public void updateUser(User user) {
        mapper.update(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        mapper.insertOrUpdate(user);

    }
}
