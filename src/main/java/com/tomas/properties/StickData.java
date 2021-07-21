package com.tomas.properties;

/**
 * Stick spatial user data keys
 */
public enum StickData {
	CLEAR("clear"),
	VELOCITY("velocity"),
	COLLIDED("collided");

	private final String key;

	StickData(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
