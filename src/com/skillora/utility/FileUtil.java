package com.skillora.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility for loading configuration properties.
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
}
