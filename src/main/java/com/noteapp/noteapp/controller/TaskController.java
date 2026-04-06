package com.noteapp.noteapp.controller;

import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.service.TaskService;
import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.enum_entities.Category;
import com.noteapp.noteapp.enum_entities.Priority;
import com.noteapp.noteapp.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for managing user tasks, including listing, adding, editing, completing, and deleting tasks.
 * All endpoints are prefixed with "/tasks".
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    /**
     * Displays the list of tasks for the logged-in user.
     *
     * @param model   Spring Model for passing data to the view.
     * @param session HttpSession to retrieve the logged-in user.
     * @return The task list page if the user is logged in, otherwise redirects to the login page.
     */
    @GetMapping
    public String showTaskList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/logininapp";
        }

        model.addAttribute("tasks", taskRepository.findByUser(user));
        model.addAttribute("categories", Category.values());
        model.addAttribute("priorities", Priority.values());

        return "task-list";
    }

    /**
     * Adds a new task for the logged-in user.
     *
     * @param description The description of the task.
     * @param category    The category of the task.
     * @param priority    The priority of the task.
     * @param session     HttpSession to retrieve the logged-in user.
     * @return Redirects to the task list page.
     */
    @PostMapping("/add")
    public String addNewTask(@RequestParam String description,
                             @RequestParam Category category,
                             @RequestParam Priority priority,
                             HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/logininapp";

        taskService.addTask(description, category, priority, user);
        return "redirect:/tasks";
    }

    /**
     * Marks a task as complete for the logged-in user.
     *
     * @param id      The ID of the task to mark as complete.
     * @param session HttpSession to retrieve the logged-in user.
     * @return Redirects to the task list page.
     */
    @PostMapping("/complete/{id}")
    public String completeTask(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Optional<Task> task = taskRepository.findById(id);

        if (user != null && task.isPresent() && task.get().getUser().getId().equals(user.getId())) {
            taskService.toggleComplete(id);
        }

        return "redirect:/tasks";
    }

    /**
     * Deletes a task for the logged-in user.
     *
     * @param id      The ID of the task to delete.
     * @param session HttpSession to retrieve the logged-in user.
     * @return Redirects to the task list page.
     */
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Optional<Task> task = taskRepository.findById(id);

        if (user != null && task.isPresent() && task.get().getUser().getId().equals(user.getId())) {
            taskService.deleteTask(id);
        }
        return "redirect:/tasks";
    }

    /**
     * Toggles the completion status of a task for the logged-in user.
     *
     * @param id      The ID of the task to toggle.
     * @param session HttpSession to retrieve the logged-in user.
     * @return Redirects to the task list page.
     */
    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        Optional<Task> task = taskRepository.findById(id);
        if (user != null && task.isPresent() && task.get().getUser().getId().equals(user.getId())) {
            taskService.toggleComplete(id);
        }

        return "redirect:/tasks";
    }

    /**
     * Displays the edit form for a task.
     *
     * @param id      The ID of the task to edit.
     * @param model   Spring Model for passing data to the view.
     * @param session HttpSession to retrieve the logged-in user.
     * @return The edit task page if the user is authorized, otherwise redirects to the task list page.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Optional<Task> task = taskRepository.findById(id);

        if (user != null && task.isPresent() && task.get().getUser().getId().equals(user.getId())) {
            model.addAttribute("task", task.get());
            model.addAttribute("categories", Category.values());
            model.addAttribute("priorities", Priority.values());
            return "edit-task";
        }
        return "redirect:/tasks";
    }
}