package com.tomas;

import com.github.awvalenti.wiiusej.WiiusejNativeLibraryLoadingException;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioListenerState;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.tools.javac.Main;
import com.tomas.kinect.Kinect;
import com.tomas.kinect.control.Hand;
import com.tomas.kinect.control.KinectHandControl;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends SimpleApplication implements PhysicsCollisionListener {
	private final String scenesPath = "assets/Scenes/";
	private Kinect kinect;
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	// Game objects
	// Player
	private Spatial rightStick;
	private Spatial leftStick;
	// Drum
	private Spatial snareDrum;
	private Spatial floorTom;

	// Collisions
	// Player
	private GhostControl rightStickGhost;
	private GhostControl leftStickGhost;
	// Drum
	private GhostControl snareDrumGhost;
	private GhostControl floorTomGhost;

	Game() {
		super(new StatsAppState(),
				new AudioListenerState(),
				new DebugKeysAppState(),
				new ConstantVerifierState(),
				// TODO Change fly cam to static non-movable camera
				new FlyCamAppState());

		kinect = new Kinect();
	}

	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class);
		BinaryImporter importer = BinaryImporter.getInstance();
		importer.setAssetManager(assetManager);
		File file = new File(scenesPath + "drums.j3o");
		try {
			Node loadedNode = (Node)importer.load(file);
			loadedNode.setName("drum");
			rootNode.attachChild(loadedNode);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
		}

		cam.setLocation(new Vector3f(0, 1.5f, 0));
		cam.setRotation(new Quaternion());

		// Kinect setup
		// TODO load it in a background thread and notify when it's loaded. If it fails show error
		if (kinect.start(true, Kinect.NUI_IMAGE_RESOLUTION_640x480, Kinect.NUI_IMAGE_RESOLUTION_640x480) == 1) {
			System.out.println("Kinect loaded");
		}
		kinect.startSkeletonTracking(true);


		// Wiimote setup
		WiiUseApiManager wiiUseApiManager;
		try {
			wiiUseApiManager = new WiiUseApiManager();
		} catch (WiiusejNativeLibraryLoadingException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		// 2 wiimotes need to be used
		// TODO Allow to start the app without the 2 wiimotes
//		Wiimote[] wiimotes = wiiUseApiManager.getWiimotes(2);
//		leftWiimote = wiimotes[0];
//		rightWiimote = wiimotes[1];
//
//		leftWiimote.setLeds(true, false, false, false);
//		rightWiimote.setLeds(false, true, false, false);
//
//		leftWiimote.activateMotionSensing();
//		leftWiimote.activateSmoothing();
//		leftWiimote.setAlphaSmoothingValue(0.8f);
//		leftWiimote.activateContinuous();
//		rightWiimote.activateMotionSensing();
//		rightWiimote.activateSmoothing();
//		rightWiimote.activateContinuous();
//
//		WiimoteMotion leftWiimoteMotion = new WiimoteMotion();
//		leftWiimote.addWiiMoteEventListeners(leftWiimoteMotion);
//
//		WiimoteMotion rightWiimoteMotion = new WiimoteMotion();
//		rightWiimote.addWiiMoteEventListeners(rightWiimoteMotion);

		setGameObjects();

		leftStick.addControl(new KinectHandControl(kinect, Hand.LEFT_HAND));

		rightStick.addControl(new KinectHandControl(kinect, Hand.RIGHT_HAND));

		setCollisions();
	}

	@Override
	public void simpleUpdate(float tpf) {

	}

	private void setGameObjects() {
		// Player
		leftStick = rootNode.getChild("left_stick");
		rightStick = rootNode.getChild("right_stick");

		// Drum
		snareDrum = rootNode.getChild("snare_drum");
		floorTom = rootNode.getChild("floor_tom");
	}

	private void setCollisions() {
		// Collisions
		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		CollisionShape rightStickCollision = CollisionShapeFactory.createBoxShape(rightStick);
		rightStickGhost = new GhostControl(rightStickCollision);
		bulletAppState.getPhysicsSpace().add(rightStickGhost);
		rightStick.addControl(rightStickGhost);

		CollisionShape leftStickCollision = CollisionShapeFactory.createBoxShape(leftStick);
		leftStickGhost = new GhostControl(leftStickCollision);
		bulletAppState.getPhysicsSpace().add(leftStickGhost);
		leftStick.addControl(leftStickGhost);

		// TODO add collisions to drum parts
		snareDrumGhost = new GhostControl(CollisionShapeFactory.createMeshShape(snareDrum));
		snareDrum.addControl(snareDrumGhost);
		bulletAppState.getPhysicsSpace().add(snareDrumGhost);

		floorTomGhost = new GhostControl(CollisionShapeFactory.createMeshShape(snareDrum));
		floorTom.addControl(floorTomGhost);
		bulletAppState.getPhysicsSpace().add(floorTomGhost);

		bulletAppState.getPhysicsSpace().addCollisionListener(this);
	}

	@Override
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
		PhysicsCollisionObject a = physicsCollisionEvent.getObjectA();
		PhysicsCollisionObject b = physicsCollisionEvent.getObjectB();

		String aName = ((GhostControl) a).getSpatial().getName();
		String bName = ((GhostControl) b).getSpatial().getName();

		PhysicsCollisionObject stick = null;
		PhysicsCollisionObject drum = null;
		if (aName.equals("right_stick") || aName.equals("left_stick")) {
			stick = a;
			drum = b;
		} else if (bName.equals("right_stick") || bName.equals(("left_stick"))) {
			stick = b;
			drum = a;
		}

		// TODO play appropriate sound when collided with drum parts
		String drumName = ((GhostControl) drum).getSpatial().getName();
		if (!drumName.contains("stick")) {
			playDrum(drumName);
		}
	}

	private void playDrum(String drumName) {
		AudioNode sound = new AudioNode(assetManager, "Sounds/" + drumName + ".wav", AudioData.DataType.Buffer);
		sound.setPositional(false);
		sound.playInstance();
	}
}
