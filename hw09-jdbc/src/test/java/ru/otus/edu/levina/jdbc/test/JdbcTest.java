package ru.otus.edu.levina.jdbc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.edu.levina.jdbc.impl.JdbcMapperImpl;
import ru.otus.edu.levina.jdbc.mapper.JdbcMapper;
import ru.otus.edu.levina.jdbc.model.Account;
import ru.otus.edu.levina.jdbc.model.User;

public class JdbcTest {
    private static final String URL = "jdbc:h2:mem:";
    private Connection connection;
    private JdbcMapper<User> userMapper;
    private JdbcMapper<Account> accMapper;

    @BeforeEach
    public void before() throws SQLException {
        connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        try (PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {
            pst.executeUpdate();
        }
        try (PreparedStatement pst = connection
                .prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
        userMapper = new JdbcMapperImpl<>(User.class, connection);
        accMapper = new JdbcMapperImpl<>(Account.class, connection);

    }

    @AfterEach
    public void after() throws SQLException {
        connection.close();
    }

    @Test
    public void _01_create_user() {
        User originUser1 = new User(1, "u1", 10);
        userMapper.create(originUser1);
        User loaded1 = userMapper.load(1, User.class);
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u2", 20);
        userMapper.create(originUser2);
        User loaded2 = userMapper.load(2, User.class);
        assertEquals(originUser2, loaded2);

    }

    @Test
    public void _02_update_user() {
        User originUser1 = new User(1, "u1", 10);
        userMapper.create(originUser1);
        originUser1.setAge(11);
        originUser1.setName("u1-updated");
        userMapper.update(originUser1);
        User loaded1 = userMapper.load(1, User.class);
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u2", 20);
        userMapper.create(originUser2);
        originUser2.setAge(21);
        originUser2.setName("u2-updated");
        userMapper.update(originUser2);
        User loaded2 = userMapper.load(2, User.class);
        assertEquals(originUser2, loaded2);

    }

    @Test
    public void _03_create_account() {
        Account originAcc1 = new Account(1, "u1", new BigDecimal(10));
        accMapper.create(originAcc1);
        Account loaded1 = accMapper.load(1, Account.class);
        assertEquals(originAcc1, loaded1);

        Account originAcc2 = new Account(2, "u2", new BigDecimal(20));
        accMapper.create(originAcc2);
        Account loaded2 = accMapper.load(2, Account.class);
        assertEquals(originAcc2, loaded2);

    }

    @Test
    public void _04_update_account() {
        Account originAcc1 = new Account(1, "u1", new BigDecimal(10));
        accMapper.create(originAcc1);
        originAcc1.setRest(new BigDecimal(11));
        originAcc1.setType("u1-updated");
        accMapper.update(originAcc1);
        Account loaded1 = accMapper.load(1, Account.class);
        assertEquals(originAcc1, loaded1);

        Account originAcc2 = new Account(2, "u2", new BigDecimal(20));
        accMapper.create(originAcc2);
        originAcc1.setRest(new BigDecimal(21));
        originAcc1.setType("u2-updated");
        accMapper.update(originAcc2);
        Account loaded2 = accMapper.load(2, Account.class);
        assertEquals(originAcc2, loaded2);

    }
}
