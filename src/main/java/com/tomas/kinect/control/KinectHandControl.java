package com.tomas.kinect.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.tomas.kinect.Kinect;
import com.tomas.properties.Hand;
import com.tomas.properties.StickData;

public class KinectHandControl extends AbstractControl {
	private Kinect kinect;
	private Hand handDirection;
	private long previousTime;
	private Vector3f previousHandLocation;
	private Vector3f handVelocity;
	private Velocity velocity;

	public KinectHandControl(Kinect kinect, Hand handDirection) {
		this.kinect = kinect;
		this.handDirection = handDirection;
		previousTime = 0;
		previousHandLocation = new Vector3f();
		velocity = new Velocity();
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
		Vector3f handVelocity = velocity.calculateVelocity(translation);
		spatial.setUserData(StickData.VELOCITY.getKey(), handVelocity);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
}
