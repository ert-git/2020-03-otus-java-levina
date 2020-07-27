package ru.otus.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hibernate.DatabaseSession;
import ru.otus.hibernate.SessionManager;
import ru.otus.model.User;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionManager sessionManager;

    public UserRepositoryImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RepositoryException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }


    @Override
    public List<User> getAllUsers() {
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            return currentSession.getHibernateSession().createNamedQuery("User.getAllUsers", User.class)
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
