package com.noteapp.noteapp.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.noteapp.noteapp.entities.Task;
import com.noteapp.noteapp.entities.User;
import com.noteapp.noteapp.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service for exporting and importing application data to/from JSON files.
 * Handles serialization and deserialization of users and their associated tasks.
 */
@Service
public class DataExportService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a DataExportService with the specified UserRepository.
     * Initializes the ObjectMapper with pretty-printing enabled.
     *
     * @param userRepository The repository for accessing user data.
     */
    public DataExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Exports all user data (including tasks) to a JSON file at the specified path.
     *
     * @param filePath The path where the JSON file will be saved.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public void exportDataToJson(String filePath) throws IOException {
        List<User> allUsers = userRepository.findAll();
        objectMapper.writeValue(new File(filePath), allUsers);
        System.out.println("Data successfully exported to: " + filePath);
    }

    /**
     * Imports user data (including tasks) from a JSON file at the specified path.
     * If the file does not exist, logs a message and starts with an empty database.
     *
     * @param filePath The path from which the JSON file will be read.
     * @throws IOException If an I/O error occurs during file reading.
     */
    public void importDataFromJson(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No backup file found at " + filePath + ". Starting with an empty database.");
            return;
        }

        List<User> users = objectMapper.readValue(file, new TypeReference<List<User>>() {});

        for (User user : users) {
            if (user.getTasks() != null) {
                for (Task task : user.getTasks()) {
                    task.setUser(user);
                }
            }
        }

        userRepository.saveAll(users);
        System.out.println("Successfully imported " + users.size() + " users from JSON!");
    }

    /**
     * Automatically exports all user data to a JSON file before the application shuts down.
     * This method is annotated with {@link PreDestroy} to ensure it runs on application shutdown.
     */
    @PreDestroy
    public void onShutdown() {
        try {
            exportDataToJson("backup_data.json");
            System.out.println("Auto-saved data to JSON before shutdown.");
        } catch (IOException e) {
            System.err.println("Auto-save failed: " + e.getMessage());
        }
    }
}