package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.tomas.controls.KinectHandControl;
import com.tomas.kinect.Kinect;
import com.tomas.properties.CollisionGroups;
import com.tomas.properties.Hand;
import com.tomas.properties.StickData;

import java.util.ArrayList;

/**
 * Sticks moved by moving the arms
 */
public class SticksAppState extends BaseAppState {
	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;

	// Player
	private Spatial rightStick;
	private Spatial leftStick;
	// Collisions
	private GhostControl rightStickGhost;
	private GhostControl leftStickGhost;

	@Override
	protected void initialize(Application app) {
		SimpleApplication simpleApplication = (SimpleApplication) app;
		rootNode = simpleApplication.getRootNode();
		assetManager = simpleApplication.getAssetManager();
		bulletAppState = simpleApplication.getStateManager().getState(BulletAppState.class);

		// Create stick mesh
		Spatial stick = assetManager.loadModel("Models/stick.j3o");
		rightStick = createStick(stick.deepClone(), Hand.RIGHT_HAND);
		rightStick.setName("right_stick");
		leftStick = createStick(stick.deepClone(), Hand.LEFT_HAND);
		leftStick.setName("left_stick");

		rightStickGhost = addStickCollision(rightStick);
		leftStickGhost = addStickCollision(leftStick);
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

	private Spatial createStick(Spatial stick, Hand handDirection) {
		stick.setUserData(StickData.CLEAR.getKey(), true);
		stick.setUserData(StickData.VELOCITY.getKey(), Vector3f.ZERO);
		stick.addControl(new KinectHandControl(Kinect.getInstance(), handDirection));
		rootNode.attachChild(stick);
		LightList lights = rootNode.getWorldLightList();
		for (Light light : lights) {
			stick.addLight(light);
		}
		return stick;
	}

	private GhostControl addStickCollision(Spatial stick) {
		CollisionShape collisionShape = CollisionShapeFactory.createBoxShape(stick);
		GhostControl ghostControl = new GhostControl(collisionShape);
		ghostControl.setCollisionGroup(CollisionGroups.STICKS.getCollisionGroup());
		ghostControl.setCollideWithGroups(CollisionGroups.DRUMS.getCollisionGroup());
		bulletAppState.getPhysicsSpace().add(ghostControl);
		stick.addControl(ghostControl);
		stick.setUserData(StickData.COLLIDED.getKey(), new ArrayList<PhysicsCollisionObject>());
		return ghostControl;
	}

	public Spatial getRightStick() {
		return rightStick;
	}

	public Spatial getLeftStick() {
		return leftStick;
	}

	public GhostControl getRightStickGhost() {
		return rightStickGhost;
	}

	public GhostControl getLeftStickGhost() {
		return leftStickGhost;
	}
}
