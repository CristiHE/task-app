package com.sda.project.controller;

import com.sda.project.model.Project;
import com.sda.project.model.Sprint;
import com.sda.project.service.ProjectService;
import com.sda.project.service.SprintService;
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
public class SprintController {

    private static final Logger log = LoggerFactory.getLogger(SprintController.class);

    private final SprintService sprintService;
    private final UserService userService;


    @Autowired
    public SprintController(SprintService sprintService, UserService userService) {
        this.sprintService = sprintService;
        this.userService = userService;
    }

    @GetMapping("/sprints")
    public String showSprintsPage(Model model) {
        model.addAttribute("sprints", sprintService.findAll());
        return "sprint/sprints";
    }

    @GetMapping("/sprints/add")
    public String showAddForm(Model model) {
        model.addAttribute("sprint", new Sprint());
        model.addAttribute("users", userService.findAll());
        return "sprint/sprint-add";
    }

    @PostMapping("/sprints/add")
    public String add(Model model, @ModelAttribute Sprint sprint) {
        try {
            sprintService.save(sprint);
            return "redirect:/sprints";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("sprint", sprint);
            model.addAttribute("users", userService.findAll());
            return "sprint/sprint-add";
        }
    }

    @GetMapping("/sprints/{id}/edit")
    public String showEditForm(Model model, @PathVariable Long id) {
        model.addAttribute("sprint", sprintService.findById(id));
        model.addAttribute("users", userService.findAll());
        return "sprint/sprint-edit";
    }

    @PostMapping("/sprints/{id}/edit")
    public String edit(
            Model model,
            @PathVariable Long id,
            @ModelAttribute Sprint sprint) {
        try {
            sprintService.update(sprint);
            return "redirect:/sprints";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("users", userService.findAll());
            return "sprint/sprint-edit";
        }
    }

    @GetMapping("/sprints/{id}/delete")
    public String delete(Model model, @PathVariable Long id) {
        try {
            sprintService.delete(id);
            return "redirect:/sprints";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "sprint/sprints";
        }
    }
}