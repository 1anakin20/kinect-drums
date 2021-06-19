package com.tomas.kinect.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.tomas.kinect.Kinect;

public class KinectHandControl extends AbstractControl {
	private Kinect kinect;
	private Hand handDirection;

	public KinectHandControl(Kinect kinect, Hand handDirection) {
		this.kinect = kinect;
		this.handDirection = handDirection;
	}

	@Override
	protected void controlUpdate(float tpf) {
		Vector3f translation = null;
		switch (handDirection) {
			case LEFT_HAND:
				if (kinect.getLeftHand() != null) {
					translation = kinect.getLeftHand().getTranslation();
				}
				break;
			case RIGHT_HAND:
				if (kinect.getRightHand() != null) {
				translation = kinect.getRightHand().getTranslation();
			}
				break;
		}

		if (translation != null) {
			spatial.setLocalTranslation(translation);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
}
