package com.sda.project.controller;

import com.sda.project.controller.exception.ResourceAlreadyExistsException;
import com.sda.project.model.User;
import com.sda.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // register

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register/add")
    public String add(Model model, @ModelAttribute User user) {
        try {
            userService.save(user);
            return "redirect:/login";
        } catch (ResourceAlreadyExistsException e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "user/register";
        }
    }

    // login

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    // crud

    @GetMapping("/users")
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditForm(Model model, @PathVariable Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/edit-user";
    }

    @PostMapping("/users/{id}/edit")
    public String edit(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enable")
    public String enable(Model model, @PathVariable Long id) {
        try {
            userService.enable(id);
            return "redirect:/users";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("users", userService.findAll());
            return "user/users";
        }
    }

    @GetMapping("/users/{id}/disable")
    public String disable(Model model, @PathVariable Long id) {
        try {
            userService.disable(id);
            return "redirect:/users";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("users", userService.findAll());
            return "user/users";
        }
    }
}
