package com.noteapp.noteapp.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user entity in the application.
 * Each user has a unique username, password, and a list of associated tasks.
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user. Must be unique and not null.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The password of the user. Must not be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The list of tasks associated with the user.
     * Tasks are eagerly fetched and cascaded for all operations.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();

    /**
     * Default constructor for JPA.
     */
    public User() {}

    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * @return The unique identifier of the user.
     * @return The list of tasks associated with the user.
     * @return The username of the user.
     * @return The password of the user.
     */
    public Long getId() { return id; }
    public List<Task> getTasks() { return tasks; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    /**
     * @param id The unique identifier to set for the user.
     * @param tasks The list of tasks to associate with the user.
     * @param username The username to set for the user.
     * @param password The password to set for the user.
     */
    public void setId(Long id) { this.id = id; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}