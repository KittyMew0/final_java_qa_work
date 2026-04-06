package com.noteapp.noteapp.repository;

import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Task} entities.
 * Provides methods for querying tasks, including finding tasks by user.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks associated with a specific user.
     *
     * @param user The user whose tasks are to be retrieved.
     * @return A list of tasks belonging to the specified user.
     */
    List<Task> findByUser(User user);
}