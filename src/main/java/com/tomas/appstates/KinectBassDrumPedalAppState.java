package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.math.Vector3f;
import com.tomas.kinect.Joint;
import com.tomas.kinect.Kinect;
import com.tomas.utils.SoundManager;
import com.tomas.utils.Velocity;

/**
 * Bass pedal actioned by the right knee
 */
public class KinectBassDrumPedalAppState extends BaseAppState implements PhysicsTickListener {
	private AssetManager assetManager;
	private final Kinect kinect = Kinect.getInstance();
	private final Velocity bassPedalVelocity = new Velocity();
	private boolean isBassPedalEnabled = false;
	private boolean canHitBass = false;

	@Override
	protected void initialize(Application app) {
		assetManager = app.getAssetManager();
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}

	@Override
	public void prePhysicsTick(PhysicsSpace physicsSpace, float v) {

	}

	@Override
	public void physicsTick(PhysicsSpace physicsSpace, float v) {
		// Play the bass drum
		if (kinect.isInitialPose()) {
			Joint rightKneeJoint = kinect.getRightKnee();
			if (rightKneeJoint != null) {
				Vector3f rightKneeTranslation = rightKneeJoint.getTranslation();
				if (rightKneeTranslation.getY() > kinect.getInitialRightKnee().getTranslation().getY() + 0.05) {
					isBassPedalEnabled = true;
					System.out.println("rightKneeTranslation = " + rightKneeTranslation);
				} else {
					if (isBassPedalEnabled) {
						isBassPedalEnabled = false;
						canHitBass = true;
					}
				}

				Vector3f bassPedalVelocityVector = bassPedalVelocity.calculateVelocity(rightKneeTranslation);
				if (bassPedalVelocityVector.getY() < 0 && canHitBass) {
					canHitBass = false;
					float bassIntensity = SoundManager.calculateHitForce(bassPedalVelocityVector);
					SoundManager.playDrum("bass_drum", bassIntensity, assetManager);
				}
			}
		}
	}
}