package com.tomas.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class ConfigurationLoader {
    private static final String CONFIGURATION_FOLDER = "configuration";
    private static final String CONFIGURATION_PATH = CONFIGURATION_FOLDER + "/config.properties";
    private static final String DEFAULT_CONFIG_PATH = CONFIGURATION_FOLDER + "/default_config.properties";

    public static Configuration loadConfiguration() throws IOException {
        File configurationFile = new File(CONFIGURATION_PATH);
        if (!configurationFile.exists()) {
            copyDefaultConfigs();
        }

        FileReader configurationFileReader = new FileReader(configurationFile);
        Properties properties = new Properties();
        properties.load(configurationFileReader);
        return new Configuration(properties);
    }

    private static void copyDefaultConfigs() throws IOException {
        InputStream defaultConfigInputStream  = ConfigurationLoader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH);
        if (defaultConfigInputStream == null) {
            System.err.println("Default configs don't exist");
            System.exit(1);
        }

        File destinationFile = new File(CONFIGURATION_PATH);
        //noinspection ResultOfMethodCallIgnored
        destinationFile.getParentFile().mkdirs();
        //noinspection ResultOfMethodCallIgnored
        destinationFile.createNewFile();
        Path destinationPath = destinationFile.toPath();
        Files.copy(defaultConfigInputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
