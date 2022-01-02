package com.tomas.configuration;

import java.util.Properties;

public class Configuration {
    private final Properties properties;

    protected Configuration(Properties properties) {
        this.properties = properties;
    }

    public String getValue(ConfigurationKeys value) {
        return properties.getProperty(value.getValue());
    }

    public void setValue(ConfigurationKeys key, String value) {
        properties.setProperty(key.getValue(), value);
        // TODO store properties on disk
    }
}
