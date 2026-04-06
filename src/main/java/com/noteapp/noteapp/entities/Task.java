package com.noteapp.noteapp.entities;

import com.noteapp.noteapp.enum_entities.Category;
import com.noteapp.noteapp.enum_entities.Priority;
import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Represents a task entity in the application.
 * Each task has a description, category, priority, completion status, creation timestamp, and an associated user.
 */
@Entity
public class Task {
    /**
     * Private fields: unique identifier for tasks,
     * description, category, priority of the task
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    /**
     * Indicates whether the task is completed.
     * The timestamp when the task was created.
     */
    private boolean completed;
    private LocalDateTime createdAt;

    /**
     * The user who owns the task.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    /**
     * Default constructor for JPA.
     */
    public Task() {}

    /**
     * Constructs a new Task with the specified details.
     *
     * @param id          The unique identifier for the task.
     * @param description The description of the task.
     * @param category    The category of the task.
     * @param priority    The priority of the task.
     */
    public Task(Long id, String description, Category category, Priority priority) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Returns the CSS class for the task based on its completion status.
     *
     * @return "task-completed" if the task is completed, otherwise "task-active".
     */
    public String getCssClass() {
        return completed ? "task-completed" : "task-active";
    }

    /** Getters and Setters
     * @return The unique identifier of the task.
     * @return The description of the task.
     * @return The category of the task.
     * @return The priority of the task.
     * @return The completion status of the task.
     * @return The timestamp when the task was created.
     */

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * @param id The unique identifier to set for the task.
     * @param description The description to set for the task.
     * @param category The category to set for the task.
     * @param priority The priority to set for the task.
     * @param completed The completion status to set for the task.
     * @param createdAt The timestamp to set for when the task was created.
     */
    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * @return The user who owns the task.
     */
    public User getUser() { return user; }

    /**
     * @param user The user to associate with the task.
     */
    public void setUser(User user) { this.user = user; }
}