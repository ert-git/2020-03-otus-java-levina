package ru.otus.edu.levina.jdbc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.cachehw.MyCache;
import ru.otus.core.HibernateUtils;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoHibernate;
import ru.otus.core.service.DbServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.core.sessionmanager.SessionManagerHibernate;
import ru.otus.edu.levina.hibernate.model.AddressDataSet;
import ru.otus.edu.levina.hibernate.model.PhoneDataSet;
import ru.otus.edu.levina.hibernate.model.User;

public class HiberanteTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

    private SessionFactory sessionFactory;
    private DbServiceUser dbServiceUserCached;
    private DbServiceUser dbServiceUserNoCache;
    private UserDao userDaoCache;
    private UserDao userDaoNoCache;
    private MyCache<String, User> cache;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        cache = new MyCache<>();
        userDaoCache = new UserDaoHibernate(sessionManager, cache);
        userDaoNoCache = new UserDaoHibernate(sessionManager);
        dbServiceUserNoCache = new DbServiceUserImpl(userDaoNoCache);
        dbServiceUserCached = new DbServiceUserImpl(userDaoCache);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    @Test
    public void _01_create_user_cached() {
        User user = createUser(10, 3, dbServiceUserCached);
        checkLoadedUser(user, dbServiceUserCached);
    }

    @Test
    public void _02_update_user_cached() {
        User user = createUser(11, 2, dbServiceUserCached);
        checkLoadedUser(user, dbServiceUserCached);
        user.setName("u1");
        user.setAge(12);
        user.getAddress().setStreet("any street");
        user.getPhones().get(0).setNumber("p1");
        user.getPhones().get(1).setNumber("p2");
        dbServiceUserCached.saveUser(user);
        checkLoadedUser(user, dbServiceUserCached);
    }

    @Test
    public void _03_delete_user_cached() {
        User user = createUser(13, 1, dbServiceUserCached);
        checkLoadedUser(user, dbServiceUserCached);
        assertNotNull(cache.get(String.valueOf(user.getId())));
        dbServiceUserCached.deleteUser(user);
        assertFalse(dbServiceUserCached.getUser(user.getId()).isPresent());
        assertNull(cache.get(String.valueOf(user.getId())));
    }

    @Test
    public void _04_create_user_nocache() {
        User user = createUser(10, 3, dbServiceUserNoCache);
        checkLoadedUser(user, dbServiceUserNoCache);
    }

    @Test
    public void _05_update_user_nocache() {
        User user = createUser(11, 2, dbServiceUserNoCache);
        checkLoadedUser(user, dbServiceUserNoCache);
        user.setName("u1");
        user.setAge(12);
        user.getAddress().setStreet("any street");
        user.getPhones().get(0).setNumber("p1");
        user.getPhones().get(1).setNumber("p2");
        dbServiceUserCached.saveUser(user);
        checkLoadedUser(user, dbServiceUserNoCache);
    }

    @Test
    public void _06_delete_user_nocache() {
        User user = createUser(13, 1, dbServiceUserNoCache);
        checkLoadedUser(user, dbServiceUserNoCache);
        dbServiceUserNoCache.deleteUser(user);
        assertFalse(dbServiceUserNoCache.getUser(user.getId()).isPresent());
    }

    @Test
    public void _07_compare_performance() {
        User userNoCache = createUser(20, 3, dbServiceUserNoCache);
        // single iteration is not representative
        int N = 100;
        long startTime = System.currentTimeMillis();
        User loadedNoCache = null;
        for (int i = 0; i < N; i++) {
            loadedNoCache = dbServiceUserNoCache.getUser(userNoCache.getId()).get();
        }
        long timeNoCache = System.currentTimeMillis() - startTime;
        compareUsers(userNoCache, loadedNoCache);

        User userCached = createUser(20, 3, dbServiceUserCached);
        startTime = System.currentTimeMillis();
        User loadedCached = null;
        for (int i = 0; i < N; i++) {
            loadedCached = dbServiceUserCached.getUser(userCached.getId()).get();
        }
        long timeWithCache = System.currentTimeMillis() - startTime;
        compareUsers(userCached, loadedCached);

        //System.out.println("**************** timeNoCache=" + timeNoCache + " timeWithCache=" + timeWithCache);
        assertTrue(timeNoCache - timeWithCache > 0);

    }

    private User createUser(int age, int phonesCount, DbServiceUser dbServiceUser) {
        User user = new User();
        user.setName(UUID.randomUUID().toString());
        user.setAge(age);

        List<PhoneDataSet> listPhone = new ArrayList<>();
        for (int i = 0; i < phonesCount; i++) {
            listPhone.add(new PhoneDataSet(UUID.randomUUID().toString(), user));
        }
        user.setPhones(listPhone);
        user.setAddress(new AddressDataSet(UUID.randomUUID().toString(), user));

        dbServiceUser.saveUser(user);
        return user;
    }

    private void checkLoadedUser(User user, DbServiceUser dbServiceUser) {
        User loaded = dbServiceUser.getUser(user.getId()).get();
        compareUsers(user, loaded);
    }

    private void compareUsers(User user, User loaded) {
        assertEquals(user.getAddress(), loaded.getAddress());
        for (int i = 0; i < user.getPhones().size(); i++) {
            assertEquals(user.getPhones().get(i), loaded.getPhones().get(i));
        }
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getAge(), loaded.getAge());
        assertEquals(user.getId(), loaded.getId());
    }

}
