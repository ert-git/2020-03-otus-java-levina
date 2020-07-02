package ru.otus.edu.levina.webserver.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.server.utils.HttpUrlConnectionHelper.buildUrl;
import static ru.otus.server.utils.HttpUrlConnectionHelper.sendRequest;
import static ru.otus.server.utils.WebServerHelper.COOKIE_HEADER;
import static ru.otus.server.utils.WebServerHelper.login;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;

import org.eclipse.jetty.http.HttpMethod;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
import ru.otus.server.utils.WebServerHelper;
import ru.otus.services.DbServiceUser;
import ru.otus.services.DbServiceUserImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

public class WebserverTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN_URL = "login";
    private static final String API_USER_URL = "api/users";

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "1";
    private static final User ADMIN = new User(ADMIN_LOGIN, ADMIN_LOGIN, ADMIN_PASSWORD);
    private static final String TEMPLATES_DIR = "/templates/";

    private static Gson gson;
    private static UsersWebServer webServer;

    @BeforeAll
    public static void setUp() throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        dbServiceUser.saveUser(ADMIN);

        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        webServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        webServer.start();
    }
    
    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }


    @Test
    void _01_add_user() throws IOException {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), ADMIN_LOGIN, ADMIN_PASSWORD);

        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        int responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        // add user
        User user = new User("u1-name", "u1-login", "u1-passwd");
        connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.POST);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        connection.setDoOutput(true);
        connection.getOutputStream().write((new Gson().toJson(user)).getBytes());
        responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
        String json = WebServerHelper.readInput(connection.getInputStream());
        User newUser = gson.fromJson(json, User.class);
        assertEquals(user.getLogin(), newUser.getLogin());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getPassword(), newUser.getPassword());
        
        // get user
        connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL + "/" + newUser.getId(), null), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
        json = WebServerHelper.readInput(connection.getInputStream());
        newUser = gson.fromJson(json, User.class);
        assertEquals(user.getLogin(), newUser.getLogin());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getPassword(), newUser.getPassword());
        
        // list users
        connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
        json = WebServerHelper.readInput(connection.getInputStream());
        User[] users = gson.fromJson(json, User[].class);
        assertEquals(2, users.length);
        assertEquals(ADMIN.getLogin(), users[0].getLogin());
        assertEquals(newUser.getLogin(), users[1].getLogin());
    }

}
