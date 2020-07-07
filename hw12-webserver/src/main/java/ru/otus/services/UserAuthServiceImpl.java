package ru.otus.services;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.dao.UserDao;
import ru.otus.core.sessionmanager.SessionManager;

@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.findByLogin(login)
                        .map(user -> user.getPassword().equals(password))
                        .orElse(false);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return false;
        }
    }

}
