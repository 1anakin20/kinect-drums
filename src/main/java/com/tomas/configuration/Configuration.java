package com.tomas.configuration;

import java.util.Properties;

public class Configuration {
    private boolean wiimoteRumbleOn;

    protected Configuration(Properties properties) {
        wiimoteRumbleOn = Boolean.parseBoolean(properties.getProperty("wiimote.rumble"));
    }

    public boolean isWiimoteRumbleOn() {
        return wiimoteRumbleOn;
    }

    public void setWiimoteRumbleOn(boolean wiimoteRumbleOn) {
        this.wiimoteRumbleOn = wiimoteRumbleOn;
    }
}
