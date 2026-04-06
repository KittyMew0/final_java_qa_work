package com.noteapp.noteapp.config;

import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Controller for handling user authentication (signup and login).
 */
@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     *
     * @param username The username provided by the user.
     * @param password The plaintext password provided by the user.
     * @param model Spring Model for passing data to the view.
     * @return The signup page if registration fails, or redirects to the login page if successful.
     */
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists.");
            return "signup";
        }

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(username, hashedPassword));

        return "redirect:/logininapp";
    }

    /**
     * Authenticates a user and establishes a session.
     *
     * @param username The username provided by the user.
     * @param password The plaintext password provided by the user.
     * @param session HttpSession to store the logged-in user.
     * @param model Spring Model for passing data to the view.
     * @return The login page if authentication fails, or redirects to the tasks page if successful.
     */
    @PostMapping("/logininapp")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session, Model model) {

        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            session.setAttribute("loggedInUser", userOpt.get());
            return "redirect:/tasks";
        }

        model.addAttribute("error", "Invalid username or password");
        return "logininapp";
    }
}