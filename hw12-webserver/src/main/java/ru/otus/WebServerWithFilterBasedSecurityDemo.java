package ru.otus;

import org.hibernate.SessionFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.otus.core.HibernateUtils;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoHibernate;
import ru.otus.core.sessionmanager.SessionManagerHibernate;
import ru.otus.edu.levina.hibernate.model.AddressDataSet;
import ru.otus.edu.levina.hibernate.model.PhoneDataSet;
import ru.otus.edu.levina.hibernate.model.User;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.DbServiceUser;
import ru.otus.services.DbServiceUserImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/users/1
*/
public class WebServerWithFilterBasedSecurityDemo {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        User user = new User("admin", "admin", "1");
        dbServiceUser.saveUser(user);
        
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
