package com.noteapp.noteapp.serialization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Controller for handling administrative operations, such as data export.
 * Provides endpoints for administrative tasks, including exporting application data.
 */
@Controller
public class AdminController {
    private final DataExportService dataExportService;

    /**
     * Constructs an AdminController with the specified DataExportService.
     * @param dataExportService The service responsible for exporting data.
     */
    public AdminController(DataExportService dataExportService) {
        this.dataExportService = dataExportService;
    }

    /**
     * Triggers the export of application data to a JSON file.
     * The exported file is saved as "backup_data.json" in the project root.
     * @return A message indicating the success or failure of the export operation.
     */
    @GetMapping("/admin/export")
    @ResponseBody
    public String triggerExport() {
        try {
            dataExportService.exportDataToJson("backup_data.json");
            return "Backup created successfully in project root!";
        } catch (IOException e) {
            return "Export failed: " + e.getMessage();
        }
    }
}