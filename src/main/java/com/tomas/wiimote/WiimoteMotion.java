package com.tomas.wiimote;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.tomas.properties.DrumData;
import com.tomas.utils.SoundManager;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class WiimoteMotion implements WiimoteEventsAdapter {
	private Application app;
	private GhostControl stick;
	private AssetManager assetManager;
	private int buffer = 0;

	public WiimoteMotion(Application app, AssetManager assetManager, GhostControl stick) {
		this.app = app;
		this.stick = stick;
		this.assetManager = assetManager;
	}

	@Override
	public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {
		// TODO calculate volume from downwards force
		float volume;
		float wiimoteAxisZ = motionSensingEvent.getGforce().getZ();
		if (buffer > -1) buffer -= 1;
		if (buffer < 0 && wiimoteAxisZ > 3.0) {
			Vector3f velocity = new Vector3f(0, wiimoteAxisZ, 0);
			volume = SoundManager.calculateHitForce(velocity);
//			if (wiimoteAxisZ > 3.5) {
//				volume = 0;
//			} else {
//				volume = -1500 - (int)(wiimoteAxisZ - 3.0) * 3000;
//			}

			System.out.println(volume);

			for (PhysicsCollisionObject collisionObject : stick.getOverlappingObjects()) {
				GhostControl collidedGhost = (GhostControl) collisionObject;
				String drumName = collidedGhost.getSpatial().getUserData(DrumData.AUDIO_NAME.getKey());
				// TODO JME complaims about playing the sound not on the render thread
				Runnable playSound = () -> SoundManager.playDrum(drumName, volume, assetManager);
				app.enqueue(playSound);
			}
		}
	}
}