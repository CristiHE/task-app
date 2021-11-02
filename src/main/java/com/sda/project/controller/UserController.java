package com.sda.project.controller;

import com.sda.project.controller.exception.ResourceAlreadyExistsException;
import com.sda.project.dto.UserDto;
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
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // register
    // mapping
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        // folder / page name
        return "user/register";
    }

    @PostMapping("/register/add")
    public String register(@ModelAttribute("userDto") UserDto userDto) {
        userService.save(userDto);
        return "user/login";
    }

    // login

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userDto") UserDto userDto) {
        userService.findByEmail(userDto.getEmail());
        return "redirect:user/home";
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
