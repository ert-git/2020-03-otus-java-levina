package ru.otus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hibernate.SessionManager;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userDao;

    public UserServiceImpl(UserRepository userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();

                log.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                log.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userDao.getAllUsers();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ServiceException(e);
            }
        }
    }
}
