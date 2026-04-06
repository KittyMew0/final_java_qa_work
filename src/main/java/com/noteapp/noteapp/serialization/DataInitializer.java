package com.noteapp.noteapp.serialization;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component responsible for initializing application data on startup.
 * Implements {@link CommandLineRunner} to execute data import logic when the application starts.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final DataExportService dataExportService;

    /**
     * Constructs a DataInitializer with the specified DataExportService.
     * @param dataExportService The service responsible for importing data from JSON.
     */
    public DataInitializer(DataExportService dataExportService) {
        this.dataExportService = dataExportService;
    }

    /**
     * Imports data from a JSON file when the application starts.
     * This method is automatically called by Spring Boot on application startup.
     *
     * @param args Command-line arguments passed to the application.
     * @throws Exception If an error occurs during data import.
     */
    @Override
    public void run(String... args) throws Exception {
        dataExportService.importDataFromJson("backup_data.json");
    }
}