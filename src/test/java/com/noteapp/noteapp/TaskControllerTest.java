package com.noteapp.noteapp;

import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.enum_entities.Category;
import com.noteapp.noteapp.enum_entities.Priority;
import com.noteapp.noteapp.repository.TaskRepository;
import com.noteapp.noteapp.repository.UserRepository;
import com.noteapp.noteapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TaskController.
 * Uses Spring Boot's testing support and MockMvc to test controller endpoints.
 * Mocks repositories and services to isolate controller logic.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private UserRepository userRepository;

    /**
     * Tests that accessing the tasks endpoint without being logged in
     * results in a redirect to the login page.
     */
    @Test
    public void testAccessTasksWithoutLogin() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logininapp"));
    }

    /**
     * Tests that a user cannot delete a task owned by another user.
     * Verifies that the delete operation is not called for unauthorized users.
     */
    @Test
    public void testDeleteTaskAccessDenied() throws Exception {
        User owner = new User("owner", "pass");
        owner.setId(1L);

        User hacker = new User("hacker", "pass");
        hacker.setId(2L);

        Task task = new Task(99L, "Secret Task", Category.WORK, Priority.HIGH);
        task.setUser(owner);

        when(taskRepository.findById(99L)).thenReturn(Optional.of(task));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", hacker);

        mockMvc.perform(post("/tasks/delete/99")
                        .sessionAttr("loggedInUser", hacker))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService, never()).deleteTask(99L);
    }

    /**
     * Tests that the task list endpoint shows only the tasks belonging to the logged-in user.
     * Verifies that the correct model attributes and view are returned.
     */
    @Test
    public void testShowTaskListShowsOnlyUserTasks() throws Exception {
        User currentUser = new User("bob", "password");

        mockMvc.perform(get("/tasks")
                        .sessionAttr("loggedInUser", currentUser))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tasks"))
                .andExpect(view().name("task-list"));

        verify(taskRepository).findByUser(currentUser);
    }

    /**
     * Tests that attempting to delete a non-existent task
     * results in a redirect and does not call the delete service.
     */
    @Test
    public void testDeleteNonExistentTask() throws Exception {
        User user = new User("bob", "pass");
        user.setId(1L);

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/tasks/delete/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService, never()).deleteTask(999L);
    }

    /**
     * Tests that a user cannot access the edit form for a task owned by another user.
     * Verifies that unauthorized access results in a redirect.
     */
    @Test
    public void testShowEditFormUnauthorized() throws Exception {
        User owner = new User("owner", "pass");
        owner.setId(1L);
        User stranger = new User("stranger", "pass");
        stranger.setId(2L);

        Task task = new Task(10L, "Private Task", Category.PERSONAL, Priority.LOW);
        task.setUser(owner);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/edit/10")
                        .sessionAttr("loggedInUser", stranger))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    /**
     * Tests that a user can log in with a correctly encrypted password.
     * Verifies that successful login redirects to the tasks page.
     */
    @Test
    public void testLoginWithEncryptedPassword() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securePassword = encoder.encode("correct_pass");

        User user = new User("bob", securePassword);

        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/logininapp")
                        .param("username", "bob")
                        .param("password", "correct_pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }
}