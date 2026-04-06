package com.noteapp.noteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the NoteApp Spring Boot application.
 * This class serves as the entry point for the application and enables auto-configuration.
 */
@SpringBootApplication
public class NoteappApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(NoteappApplication.class, args);
	}
}