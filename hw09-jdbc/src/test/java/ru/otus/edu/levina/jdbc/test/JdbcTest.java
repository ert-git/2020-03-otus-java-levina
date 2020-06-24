package ru.otus.edu.levina.jdbc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.impl.EntityClassMetaDataImpl;
import ru.otus.edu.levina.jdbc.impl.EntitySQLMetaDataImpl;
import ru.otus.edu.levina.jdbc.impl.JdbcMapperImpl;
import ru.otus.edu.levina.jdbc.model.Account;
import ru.otus.edu.levina.jdbc.model.User;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class JdbcTest {
    private static JdbcMapper<User> userMapper;
    private static JdbcMapper<Account> accMapper;
    private static SessionManager sessionManager;
    private static EntityClassMetaData<User> userMeta;
    private static EntityClassMetaData<Account> accMeta;
    private static EntitySQLMetaData userSqlMeta;
    private static EntitySQLMetaData accSqlMeta;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        sessionManager = new SessionManagerJdbc(new DataSourceH2());

        sessionManager.beginSession();
        Connection connection = sessionManager.getCurrentSession().getConnection();
        try (PreparedStatement pst = connection
                .prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {
            pst.executeUpdate();
        }
        try (PreparedStatement pst = connection
                .prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
        sessionManager.commitSession();

        userMeta = new EntityClassMetaDataImpl<>(User.class);
        accMeta = new EntityClassMetaDataImpl<>(Account.class);
        userSqlMeta = new EntitySQLMetaDataImpl(userMeta);
        accSqlMeta = new EntitySQLMetaDataImpl(accMeta);

        userMapper = new JdbcMapperImpl<>(userMeta, userSqlMeta, sessionManager, new DbExecutorImpl<>());
        accMapper = new JdbcMapperImpl<>(accMeta, accSqlMeta, sessionManager, new DbExecutorImpl<>());
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        sessionManager.close();
    }

    @Test
    public void _01_create_user() {
        User originUser1 = new User(1, "u1", 10);
        userMapper.insert(originUser1);
        User loaded1 = userMapper.findById(originUser1.getId(), User.class);
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u2", 20);
        userMapper.insert(originUser2);
        User loaded2 = userMapper.findById(originUser2.getId(), User.class);
        assertEquals(originUser2, loaded2);

    }

    @Test
    public void _02_update_user() {
        User originUser1 = new User(1, "u3", 10);
        userMapper.insert(originUser1);
        originUser1.setAge(11);
        originUser1.setName("u3-updated");
        userMapper.update(originUser1);
        User loaded1 = userMapper.findById(originUser1.getId(), User.class);
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u4", 20);
        userMapper.insert(originUser2);
        originUser2.setAge(21);
        originUser2.setName("u4-updated");
        userMapper.update(originUser2);
        User loaded2 = userMapper.findById(originUser2.getId(), User.class);
        assertEquals(originUser2, loaded2);
    }

    @Test
    public void _03_create_account() {
        Account originAcc1 = new Account(1, "u1", new BigDecimal(10));
        accMapper.insert(originAcc1);
        Account loaded1 = accMapper.findById(originAcc1.getNo(), Account.class);
        assertEquals(originAcc1, loaded1);

        Account originAcc2 = new Account(2, "u2", new BigDecimal(20));
        accMapper.insert(originAcc2);
        Account loaded2 = accMapper.findById(originAcc2.getNo(), Account.class);
        assertEquals(originAcc2, loaded2);

    }

    @Test
    public void _04_update_account() {
        Account originAcc1 = new Account(1, "u1", new BigDecimal(10));
        accMapper.insert(originAcc1);
        originAcc1.setRest(new BigDecimal(11));
        originAcc1.setType("u1-updated");
        accMapper.update(originAcc1);
        Account loaded1 = accMapper.findById(originAcc1.getNo(), Account.class);
        assertEquals(originAcc1, loaded1);

        Account originAcc2 = new Account(2, "u2", new BigDecimal(20));
        accMapper.insert(originAcc2);
        originAcc1.setRest(new BigDecimal(21));
        originAcc1.setType("u2-updated");
        accMapper.update(originAcc2);
        Account loaded2 = accMapper.findById(originAcc2.getNo(), Account.class);
        assertEquals(originAcc2, loaded2);

    }
    
    @Test
    public void _05_insert_or_update_user() {
        User originUser1 = new User();
        originUser1.setAge(40);
        originUser1.setName("u5");
        userMapper.insertOrUpdate(originUser1);
        long id = originUser1.getId();
        User loaded1 = userMapper.findById(id, User.class);
        assertEquals(originUser1, loaded1);
        
        originUser1.setAge(45);
        originUser1.setName("u5-updated");
        userMapper.insertOrUpdate(originUser1);
        User loaded2 = userMapper.findById(id, User.class);
        assertEquals(originUser1, loaded2);
    }
}
