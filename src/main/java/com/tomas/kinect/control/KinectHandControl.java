package com.tomas.kinect.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.tomas.kinect.Kinect;

public class KinectHandControl extends AbstractControl {
	private Kinect kinect;
	private Hand handDirection;
	private long previousTime;
	private Vector3f previousHandLocation;
	private Vector3f handVelocity;
	private boolean handClear = true;

	public KinectHandControl(Kinect kinect, Hand handDirection) {
		this.kinect = kinect;
		this.handDirection = handDirection;
		previousTime = 0;
		previousHandLocation = new Vector3f();
	}

	@Override
	protected void controlUpdate(float tpf) {
		Vector3f translation = null;
		if (handDirection == Hand.LEFT_HAND) {
			if (kinect.getLeftHand() != null) {
				translation = kinect.getLeftHand().getTranslation();
			}
		} else if (handDirection == Hand.RIGHT_HAND) {
			if (kinect.getRightHand() != null) {
				translation = kinect.getRightHand().getTranslation();
			}
		}

		if (translation == null) {
			return;
		}

		spatial.setLocalTranslation(translation);

		if (previousTime == 0) {
			previousHandLocation = translation;
		}

		// Velocity
		long currentTime = System.currentTimeMillis();
		long timeDifference = currentTime - previousTime;
		handVelocity = translation.subtract(previousHandLocation).divide(timeDifference);

		// Update previous values
		previousHandLocation = translation;
		previousTime = currentTime;

		spatial.setUserData("handVelocity", handVelocity);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	private void checkHandClear() {
		if (handVelocity.getY() > 0) {
			handClear = true;
		}
	}
}
