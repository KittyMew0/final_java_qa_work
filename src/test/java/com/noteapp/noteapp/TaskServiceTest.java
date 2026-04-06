package com.noteapp.noteapp;

import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.enum_entities.Category;
import com.noteapp.noteapp.enum_entities.Priority;
import com.noteapp.noteapp.repository.TaskRepository;
import com.noteapp.noteapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for the TaskService.
 * Uses Mockito to mock dependencies and isolate the service logic for unit testing.
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    /**
     * Tests the addTask method of TaskService.
     * Verifies that the task is saved to the repository exactly once.
     */
    @Test
    public void testAddTask() {
        User user = new User("testUser", "password");
        String desc = "Buy Coffee";

        taskService.addTask(desc, Category.PERSONAL, Priority.MEDIUM, user);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Tests the toggleComplete method of TaskService.
     * Verifies that the task's completion status is toggled and saved to the repository.
     */
    @Test
    public void testToggleTaskCompletion() {
        Task task = new Task(1L, "Test Task", Category.WORK, Priority.HIGH);
        task.setCompleted(false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.toggleComplete(1L);

        assertTrue(task.isCompleted(), "Task should be true after toggle");
        verify(taskRepository).save(task);
    }
}