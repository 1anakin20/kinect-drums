package com.tomas.wiimote;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.tomas.properties.DrumData;
import com.tomas.utils.SoundManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WiimoteMotion extends WiimoteEventsAdapter {
	private Wiimote wiimote;
	private Application app;
	private GhostControl stick;
	private AssetManager assetManager;
	private final int TRIGGER_BUFFER = 10;
	private int buffer = 0;
	private ExecutorService rumbleExecutorService;

	public WiimoteMotion(Wiimote wiimote, Application app, AssetManager assetManager, GhostControl stick) {
		this.wiimote = wiimote;
		this.app = app;
		this.stick = stick;
		this.assetManager = assetManager;
		rumbleExecutorService = Executors.newSingleThreadExecutor();
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
				rumbleExecutorService.execute(() -> hitRumble(50));
			}

			buffer = TRIGGER_BUFFER;
		}
	}

	private void hitRumble(int duration) {
			wiimote.activateRumble();
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			wiimote.deactivateRumble();
	}
}
