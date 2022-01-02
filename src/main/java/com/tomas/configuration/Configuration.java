package com.tomas.configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Configuration of the app
 */
public class Configuration {
    private final Properties properties;

    /**
     * Constructor
     * @param properties Loaded properties
     */
    protected Configuration(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets a configuration value
     * @param key {@link ConfigurationKeys} configuration key
     * @return value of the configuration
     */
    public String getValue(ConfigurationKeys key) {
        return properties.getProperty(key.getValue());
    }

    /**
     * Sets a configuration to a value
     * @param key {@link ConfigurationKeys} key to change value
     * @param value new value
     */
    public void setValue(ConfigurationKeys key, String value) {
        properties.setProperty(key.getValue(), value);

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(ConfigurationPaths.CONFIGURATION_PATH.getValue());
        } catch (FileNotFoundException e) {
            // This catch block should not be reachable because the configuration is created before loading this class
            e.printStackTrace();
            System.exit(1);
            return;
        }

        try {
            properties.store(outputStream, null);
        } catch (IOException e) {
            throw new ConfigurationSavingException(e.getLocalizedMessage());
        }
    }
}
