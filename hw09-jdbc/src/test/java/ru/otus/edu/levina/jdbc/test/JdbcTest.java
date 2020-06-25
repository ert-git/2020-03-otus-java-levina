package ru.otus.edu.levina.jdbc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.edu.levina.jdbc.impl.AccountDaoJdbc;
import ru.otus.edu.levina.jdbc.impl.EntityClassMetaDataImpl;
import ru.otus.edu.levina.jdbc.impl.EntitySQLMetaDataImpl;
import ru.otus.edu.levina.jdbc.impl.UserDaoJdbc;
import ru.otus.edu.levina.jdbc.model.Account;
import ru.otus.edu.levina.jdbc.model.User;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class JdbcTest {
    private static SessionManager sessionManager;
    private static DbServiceUserImpl dbServiceUser;
    private static DbServiceAccountImpl dbServiceAcc;

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

        EntityClassMetaDataImpl<User> userMeta = new EntityClassMetaDataImpl<>(User.class);
        EntityClassMetaDataImpl<Account> accMeta = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaDataImpl userSqlMeta = new EntitySQLMetaDataImpl(userMeta);
        EntitySQLMetaDataImpl accSqlMeta = new EntitySQLMetaDataImpl(accMeta);

        dbServiceUser = new DbServiceUserImpl(new UserDaoJdbc(sessionManager, new DbExecutorImpl<>(), userMeta, userSqlMeta));
        dbServiceAcc = new DbServiceAccountImpl(new AccountDaoJdbc(sessionManager, new DbExecutorImpl<>(), accMeta, accSqlMeta));
    }


    @Test
    public void _01_create_user() {
        User originUser1 = new User(1, "u1", 10);
        dbServiceUser.saveUser(originUser1);
        User loaded1 = dbServiceUser.getUser(originUser1.getId()).get();
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u2", 20);
        dbServiceUser.saveUser(originUser2);
        User loaded2 = dbServiceUser.getUser(originUser2.getId()).get();
        assertEquals(originUser2, loaded2);

    }

    @Test
    public void _02_update_user() {
        User originUser1 = new User(1, "u3", 10);
        dbServiceUser.saveUser(originUser1);
        originUser1.setAge(11);
        originUser1.setName("u3-updated");
        dbServiceUser.updateUser(originUser1);
        User loaded1 = dbServiceUser.getUser(originUser1.getId()).get();
        assertEquals(originUser1, loaded1);

        User originUser2 = new User(2, "u4", 20);
        dbServiceUser.saveUser(originUser2);
        originUser2.setAge(21);
        originUser2.setName("u4-updated");
        dbServiceUser.saveUser(originUser2);
        User loaded2 = dbServiceUser.getUser(originUser2.getId()).get();
        assertEquals(originUser2, loaded2);
    }

    @Test
    public void _03_create_account() {
        Account originAcc1 = new Account(1, "u1", new BigDecimal(10));
        dbServiceAcc.saveAccount(originAcc1);
        Account loaded1 = dbServiceAcc.getAccount(originAcc1.getNo()).get();
        assertEquals(originAcc1, loaded1);

        Account originAcc2 = new Account(2, "u2", new BigDecimal(20));
        dbServiceAcc.saveAccount(originAcc2);
        Account loaded2 = dbServiceAcc.getAccount(originAcc2.getNo()).get();
        assertEquals(originAcc2, loaded2);

    }

  
}
