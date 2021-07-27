package com.tomas.wiimote;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.tomas.properties.DrumData;
import com.tomas.utils.SoundManager;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class WiimoteMotion implements WiimoteEventsAdapter {
	private Application app;
	private GhostControl stick;
	private AssetManager assetManager;
	private final int TRIGGER_BUFFER = 10;
	private int buffer = 0;

	public WiimoteMotion(Application app, AssetManager assetManager, GhostControl stick) {
		this.app = app;
		this.stick = stick;
		this.assetManager = assetManager;
	}

	@Override
	public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {
		float wiimoteAxisZ = motionSensingEvent.getGforce().getZ();
		if (buffer > -1) buffer -= 1;
		if (buffer < 0 && wiimoteAxisZ > 2.2) {
			double log = Math.log10(wiimoteAxisZ);
			float volume = Math.min((float) log, 1);

			for (PhysicsCollisionObject collisionObject : stick.getOverlappingObjects()) {
				GhostControl collidedGhost = (GhostControl) collisionObject;
				String drumName = collidedGhost.getSpatial().getUserData(DrumData.AUDIO_NAME.getKey());
				Runnable playSound = () -> SoundManager.playDrum(drumName, volume, assetManager);
				app.enqueue(playSound);
			}

			buffer = TRIGGER_BUFFER;
		}
	}
}
