package ru.otus.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;
import ru.otus.model.User;
import ru.otus.service.UserService;

@Slf4j
@Controller
public class UserController {

    private final UserService repository;

    public UserController(UserService repository) {
        this.repository = repository;
    }

    @GetMapping({ "/", "/user/list" })
    public String userListView(Model model) {
        List<User> users = repository.getAllUsers();
        log.info("users: {}", users);
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/user/edit")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userEdit.html";
    }

    @PostMapping("/user/edit")
    public RedirectView userSave(@ModelAttribute User user) {
        repository.saveUser(user);
        return new RedirectView("/user/list", true);
    }

}
