package com.tomas.properties;

public enum DrumData {
	AUDIO_NAME("audioName");

	private String key;

	DrumData(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
