package com.tomas.properties;

public enum StickData {
	CLEAR("clear"),
	VELOCITY("velocity");

	private final String key;

	StickData(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
