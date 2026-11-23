package com.skillora.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Handles basic file persistence operations:
 * 1. Loading database credentials securely from an external db.properties file.
 * 2. Exporting placement summaries or activity logs to local text files.
 * Demonstrates: File I/O, Properties, FileInputStream, BufferedWriter, Exception Handling.
 * Contributed by: Akanshi (Configuration loader) & Paras Jain (Report persistence)
 */
public class FileUtil {

    private static final String DEFAULT_PROPERTIES_PATH = "resources/db.properties";

    /**
     * Loads database connection settings from an external properties file.
     */
    public static Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        File propFile = new File(DEFAULT_PROPERTIES_PATH);

        if (!propFile.exists()) {
            // Check fallback relative path if executed from different working directory
            propFile = new File("../resources/db.properties");
        }

        if (propFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Warning: Could not read db.properties file: " + e.getMessage());
            }
        }
        return properties;
    }

    /**
     * Persists a plain text report to disk (e.g. for placement officers exporting candidate/drive lists).
     * Contributed by: Paras Jain
     */
    public static boolean writeReport(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing report to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a plain text file into a String.
     * Contributed by: Paras Jain
     */
    public static String readReport(String filename) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return builder.toString();
    }
}
