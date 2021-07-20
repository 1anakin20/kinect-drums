package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.tomas.properties.DrumData;
import com.tomas.properties.StickData;
import com.tomas.utils.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class HandControllerAppState extends BaseAppState implements PhysicsCollisionListener, PhysicsTickListener {
	private AssetManager assetManager;
	private SticksAppState sticksAppState;

	public HandControllerAppState(SticksAppState sticksAppState) {
		this.sticksAppState = sticksAppState;
	}

	@Override
	protected void initialize(Application app) {
		assetManager = getApplication().getAssetManager();
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
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
		PhysicsCollisionObject a = physicsCollisionEvent.getObjectA();
		PhysicsCollisionObject b = physicsCollisionEvent.getObjectB();

		String aName = ((GhostControl) a).getSpatial().getName();
		String bName = ((GhostControl) b).getSpatial().getName();

		Spatial stick = null;
		PhysicsCollisionObject drumGhost = null;
		if (aName.equals("right_stick") || aName.equals("left_stick")) {
			stick = ((GhostControl) a).getSpatial();
			drumGhost = b;
		} else if (bName.equals("right_stick") || bName.equals(("left_stick"))) {
			stick = ((GhostControl) b).getSpatial();
			drumGhost = a;
		}

		if (stick != null) {
			boolean previouslyCollided = checkStickCollisions(stick, drumGhost);
			float handVelocityY = ((Vector3f) stick.getUserData(StickData.VELOCITY.getKey())).getY();
			if (!previouslyCollided && handVelocityY < 0) {
				addToCollided(stick, drumGhost);
				String audioName = ((GhostControl) drumGhost).getSpatial().getUserData(DrumData.AUDIO_NAME.getKey());
				float hitForce = spatialHitForce(stick);
				SoundManager.playDrum(audioName, hitForce, assetManager);
			}
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace physicsSpace, float v) {
	}

	@Override
	public void physicsTick(PhysicsSpace physicsSpace, float v) {
		removeStickClearedCollisions(sticksAppState.getRightStick(), sticksAppState.getRightStickGhost());
		removeStickClearedCollisions(sticksAppState.getLeftStick(), sticksAppState.getLeftStickGhost());
	}

	private boolean checkStickCollisions(Spatial stick, PhysicsCollisionObject collided) {
		List<GhostControl> collisions = stick.getUserData(StickData.COLLIDED.getKey());
		return collisions.contains(collided);
	}

	private void addToCollided(Spatial stick, PhysicsCollisionObject collided) {
		List<PhysicsCollisionObject> collisions = stick.getUserData(StickData.COLLIDED.getKey());
		collisions.add(collided);
	}

	private void removeStickClearedCollisions(Spatial stick, GhostControl stickGhost) {
		List<PhysicsCollisionObject> overlapping = stickGhost.getOverlappingObjects();
		List<PhysicsCollisionObject> stickCollided = stick.getUserData(StickData.COLLIDED.getKey());
		List<PhysicsCollisionObject> activeCollisions = new ArrayList<>();
		for (PhysicsCollisionObject collisionObject : stickCollided) {
			if (overlapping.contains(collisionObject)) {
				activeCollisions.add(collisionObject);
			}
		}
		stick.setUserData(StickData.COLLIDED.getKey(), activeCollisions);
	}

	private float spatialHitForce(Spatial stick) {
		Vector3f velocity = stick.getUserData(StickData.VELOCITY.getKey());
		return SoundManager.calculateHitForce(velocity);
	}
}
