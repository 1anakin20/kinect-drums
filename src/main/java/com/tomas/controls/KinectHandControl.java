package com.tomas.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.tomas.kinect.Kinect;
import com.tomas.properties.Hand;
import com.tomas.properties.StickData;
import com.tomas.utils.Velocity;

/**
 * Control by moving the arms using the Kinect
 */
public class KinectHandControl extends AbstractControl {
	private final Kinect kinect;
	private final Hand handDirection;
	private final Velocity velocity;

	/**
	 * @param handDirection Left or right hand who will be used. See {@link Hand}
	 */
	public KinectHandControl(Hand handDirection) {
		this.kinect = Kinect.getInstance();
		this.handDirection = handDirection;
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
