package com.tomas.configuration;

/**
 * When the configuration could not be saved
 */
public class ConfigurationSavingException extends RuntimeException {
    public ConfigurationSavingException(String message) {
        super(message);
    }
}
