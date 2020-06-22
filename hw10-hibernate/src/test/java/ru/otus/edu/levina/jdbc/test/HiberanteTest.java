package ru.otus.edu.levina.jdbc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private DbServiceUser dbServiceUser;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    @Test
    public void _01_create_user() {
        User user = new User();
        user.setName("u1");
        user.setAge(10);

        List<PhoneDataSet> listPhone = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            listPhone.add(new PhoneDataSet("phone " + i, user));
        }
        user.setPhones(listPhone);
        user.setAddress(new AddressDataSet("address", user));

        dbServiceUser.saveUser(user);

        User loaded = dbServiceUser.getUser(1).get();
        assertEquals(user.getAddress(), loaded.getAddress());
        for (int i = 0; i < user.getPhones().size(); i++) {
            assertEquals(user.getPhones().get(i), loaded.getPhones().get(i));
        }
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getAge(), loaded.getAge());
        assertEquals(user.getId(), loaded.getId());
    }

}
