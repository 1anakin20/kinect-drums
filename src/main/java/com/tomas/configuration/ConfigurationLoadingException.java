package com.tomas.configuration;

/**
 * When the configuration could not have been loaded
 */
public class ConfigurationLoadingException extends RuntimeException {
    public ConfigurationLoadingException(String message) {
        super(message);
    }
}
