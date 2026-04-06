package com.noteapp.noteapp.service;

import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.enum_entities.Category;
import com.noteapp.noteapp.enum_entities.Priority;
import com.noteapp.noteapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Updated to include the User
    public void addTask(String desc, Category cat, Priority prio, User user) {
        Task task = new Task();
        task.setDescription(desc);
        task.setCategory(cat);
        task.setPriority(prio);
        task.setUser(user); // Critical! This links the task to the person.
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void toggleComplete(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task); // Updates the existing record
        }
    }

    public void updateTask(Long id, String newDesc, Category newCat, Priority newPrio) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setDescription(newDesc);
            task.setCategory(newCat);
            task.setPriority(newPrio);
            taskRepository.save(task);
        });
    }
}