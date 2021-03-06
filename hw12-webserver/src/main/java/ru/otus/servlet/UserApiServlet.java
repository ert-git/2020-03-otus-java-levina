package ru.otus.servlet;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import ru.otus.services.DbServiceUser;

@Slf4j
public class UserApiServlet extends HttpServlet {

    private final static Pattern URI_WITH_ID_PATTERN = Pattern.compile("/users/(\\d+)");
    private final DbServiceUser userService;
    private final Gson gson;

    public UserApiServlet(DbServiceUser userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        Matcher matcher = URI_WITH_ID_PATTERN.matcher(requestURI);
        Long id = null;
        if (matcher.find()) {
            try {
                id = Long.parseLong(matcher.group(1));
            } catch (Exception e) {
                log.error("doGet: corrupted id in uri {}: {}", requestURI, e.toString());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        try {
            Object data = userService.getUser(id).orElseThrow(NoSuchElementException::new);
            sendResponse(response, data);
        } catch (NoSuchElementException e) {
            log.error("doGet: user not found for uri {}: {}", requestURI, e.toString());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
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
