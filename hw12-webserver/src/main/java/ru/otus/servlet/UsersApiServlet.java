package ru.otus.servlet;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import ru.otus.edu.levina.hibernate.model.User;
import ru.otus.helpers.IOHelper;
import ru.otus.services.DbServiceUser;

@Slf4j
public class UsersApiServlet extends HttpServlet {

    private final DbServiceUser userService;
    private final Gson gson;

    public UsersApiServlet(DbServiceUser userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        try {
            Object data = userService.getAllUsers();
            sendResponse(response, data);
        } catch (NoSuchElementException e) {
            log.error("doGet: user not found for uri {}: {}", requestURI, e.toString());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = IOHelper.readAsString(request.getInputStream());
        User user = gson.fromJson(json, User.class);
        userService.saveUser(user);
        sendResponse(response, user);
    }

    private void sendResponse(HttpServletResponse response, Object data) {
        try {
            response.setContentType("application/json");
            response.getWriter().println(gson.toJson(data));
        } catch (Exception e) {
            log.error("sendResponse: failed", e);
        }
    }
}
