package com.sda.project.controller;

import com.sda.project.model.Task;
import com.sda.project.service.TaskService;
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
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String showTasksPage(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "task/tasks";
    }

    @GetMapping("/tasks/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userService.findAll());
        return "task/task-add";
    }

    @PostMapping("/tasks/add")
    public String add(Model model, @ModelAttribute Task task) {
        try {
            taskService.save(task);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("task", task);
            model.addAttribute("users", userService.findAll());
            return "task/task-add";
        }
    }

    @GetMapping("/tasks/{id}/delete")
    public String delete(Model model, @PathVariable Long id) {
        try {
            taskService.delete(id);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "task/tasks";
        }
    }
}