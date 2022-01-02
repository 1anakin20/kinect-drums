package com.tomas.configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class ConfigurationLoader {
    private static Configuration configurationInstance = null;
    private static final String CONFIGURATION_FOLDER = "configuration";
    private static final String CONFIGURATION_PATH = CONFIGURATION_FOLDER + "/config.properties";
    private static final String DEFAULT_CONFIG_PATH = CONFIGURATION_FOLDER + "/default_config.properties";

    private ConfigurationLoader() {
    }

    /**
     * Gives the configuration of the app
     * @return The same {@link Configuration} instance
     * @throws ConfigurationLoadingException The configuration file couldn't be loaded
     */
    public static Configuration loadConfiguration() throws ConfigurationLoadingException {
        if (configurationInstance == null) {
            File configurationFile = new File(CONFIGURATION_PATH);
            // Create new config if it doesn't exist
            if (!configurationFile.exists()) {
                try {
                    copyDefaultConfigs();
                } catch (IOException e) {
                    throw new ConfigurationLoadingException(e.getLocalizedMessage());
                }
            }

            FileReader configurationFileReader = null;
            try {
                configurationFileReader = new FileReader(configurationFile);
            } catch (FileNotFoundException e) {
                // This catch should be unreachable because the file is created before
                e.printStackTrace();
                System.exit(1);
            }

            Properties properties = new Properties();
            try {
                properties.load(configurationFileReader);
            } catch (IOException e) {
                throw new ConfigurationLoadingException(e.getLocalizedMessage());
            }
            configurationInstance = new Configuration(properties);
        }

        return configurationInstance;
    }

    /**
     * Copies the default config from resources/configuration/default_config.properties to configuration/
     * @throws IOException The file could not be copied
     */
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
