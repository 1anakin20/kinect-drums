package com.tomas.configuration;

/**
 * Keys for the configurations
 */
public enum ConfigurationKeys {
    WIIMOTE_RUMBLE("wiimote.rumble");

    private final String value;

    ConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
