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

    // get page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        // "userDto" will be used in html
        // userDto object represents the java instance
        model.addAttribute("user", new User());
        // folder / page name
        return "user/register";
    }

    // action
    @PostMapping("/register")
    public String add(Model model, @ModelAttribute User user) {
        try {
            userService.save(user);
            return "redirect:/login";
        } catch (ResourceAlreadyExistsException e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "user/login";
        }
    }

    // login

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "user/login";
    }
}
