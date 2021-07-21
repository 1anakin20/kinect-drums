package com.tomas.utils;

import com.jme3.math.Vector3f;

public class Velocity {
	private long previousTime = 0;
	private Vector3f previousTranslation;

	public Vector3f calculateVelocity(Vector3f currentTranslation) {
		if (previousTime == 0) {
			previousTranslation = currentTranslation;
		}

		// Velocity
		long currentTime = System.currentTimeMillis();
		long timeDifference = currentTime - previousTime;
		Vector3f velocity = currentTranslation.subtract(previousTranslation).divide(timeDifference);
		previousTranslation = currentTranslation;
		previousTime = currentTime;
		return velocity;
	}
}
