package com.tomas.configuration;

enum ConfigurationPaths {
    CONFIGURATION_FOLDER("configuration"),
    CONFIGURATION_PATH(CONFIGURATION_FOLDER.value + "/config.properties"),
    DEFAULT_CONFIGURATION_PATH(CONFIGURATION_FOLDER.value + "/default_config.properties");

    private final String value;

     ConfigurationPaths(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
