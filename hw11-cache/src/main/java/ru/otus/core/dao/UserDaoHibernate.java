package ru.otus.core.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.cachehw.HwCache;
import ru.otus.core.sessionmanager.DatabaseSessionHibernate;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sessionmanager.SessionManagerHibernate;
import ru.otus.edu.levina.hibernate.model.User;

public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;
    private final HwCache<String, User> cache;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this(sessionManager, null);
    }
    
    public UserDaoHibernate(SessionManagerHibernate sessionManager, HwCache<String, User> cache) {
        this.sessionManager = sessionManager;
        this.cache = cache;
    }

    @Override
    public Optional<User> findById(long id) {
        if (cache != null) {
            User user = cache.get(String.valueOf(id));
            if (user != null) {
                return Optional.of(user);
            }
        }
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            User user = currentSession.getHibernateSession().find(User.class, id);
            putCached(id, user);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private void putCached(long id, User user) {
        if (cache != null && user != null) {
            cache.put(String.valueOf(id), user);
        }
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            putCached(user.getId(), user);
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
            putCached(user.getId(), user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            putCached(user.getId(), user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void delete(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.delete(user);
            if (cache != null) {
                cache.remove(String.valueOf(user.getId()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}