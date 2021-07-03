package com.tomas;

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
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
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
import com.tomas.drumkit.StickData;
import com.tomas.game_objects.DrumData;
import com.tomas.game_objects.Hand;
import com.tomas.kinect.Kinect;
import com.tomas.kinect.control.KinectHandControl;
import wiiusej.Wiimote;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends SimpleApplication implements PhysicsCollisionListener, PhysicsTickListener {
	private BulletAppState bulletAppState;

	private Kinect kinect;
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	// Player
	private Spatial rightStick;
	private Spatial leftStick;
	// Collisions
	private GhostControl rightStickGhost;
	private GhostControl leftStickGhost;

	Game() {
		super(new StatsAppState(),
				new AudioListenerState(),
				new DebugKeysAppState(),
				new ConstantVerifierState(),
				// TODO Change fly cam to static non-movable camera
				new FlyCamAppState());

		kinect = new Kinect();
		bulletAppState = new BulletAppState();
	}

	public void simpleInitApp() {
		// Debug options
		bulletAppState.setDebugEnabled(true);

		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		bulletAppState.getPhysicsSpace().addTickListener(this);

		assetManager.registerLocator("assets/", FileLocator.class);
		BinaryImporter importer = BinaryImporter.getInstance();
		importer.setAssetManager(assetManager);
		File file = new File("assets/Scenes/drums.j3o");
		try {
			Node loadedNode = (Node) importer.load(file);
			loadedNode.setName("drum");
			rootNode.attachChild(loadedNode);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
		}

		cam.setLocation(new Vector3f(0, 1.5f, -1));
		cam.setRotation(new Quaternion());

		// Kinect setup
		// TODO load it in a background thread and notify when it's loaded. If it fails show error
		if (kinect.start(true, Kinect.NUI_IMAGE_RESOLUTION_640x480, Kinect.NUI_IMAGE_RESOLUTION_640x480) == 1) {
			System.out.println("Kinect loaded");
		}
		kinect.startSkeletonTracking(true);


		// Wiimote setup
//		WiiUseApiManager wiiUseApiManager;
//		try {
//			wiiUseApiManager = new WiiUseApiManager();
//		} catch (WiiusejNativeLibraryLoadingException e) {
//			e.printStackTrace();
//			System.exit(1);
//			return;
//		}

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
	}

	@Override
	public void simpleUpdate(float tpf) {

	}

	private void setGameObjects() {
		// Player
		leftStick = createStick("left_stick", Hand.LEFT_HAND);
		leftStickGhost = addStickCollision(leftStick);

		rightStick = createStick("right_stick", Hand.RIGHT_HAND);
		rightStickGhost = addStickCollision(rightStick);

		// Drum
		String[] drumNames = new String[] {
				"floor_tom",
				"ride_cymbal",
				"mid_tom",
				"bass_drum",
				"high_tom",
				"crash_cymbal",
				"hi_hat",
				"snare_drum"
		};
		for (String name : drumNames) {
			createDrum(name, name);
		}
	}

	private Spatial createStick(String name, Hand handDirection) {
		Spatial spatial = rootNode.getChild(name);
		spatial.setUserData(StickData.CLEAR.getKey(), true);
		spatial.setUserData(StickData.VELOCITY.getKey(), Vector3f.ZERO);
		spatial.addControl(new KinectHandControl(kinect, handDirection));
		return spatial;
	}

	private GhostControl addStickCollision(Spatial stick) {
		CollisionShape collisionShape = CollisionShapeFactory.createBoxShape(stick);
		GhostControl ghostControl = new GhostControl(collisionShape);
		bulletAppState.getPhysicsSpace().add(ghostControl);
		stick.addControl(ghostControl);
		return ghostControl;
	}

	private void createDrum(String drumName, String audioName) {
		Spatial spatial = rootNode.getChild(drumName);
		spatial.setUserData(DrumData.AUDIO_NAME.getKey(), audioName);

		CollisionShape collisionShape = CollisionShapeFactory.createDynamicMeshShape(spatial);
		collisionShape.setMargin(0);
		GhostControl ghostControl = new GhostControl(collisionShape);
		spatial.addControl(ghostControl);
		bulletAppState.getPhysicsSpace().add(ghostControl);
	}

	@Override
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
		PhysicsCollisionObject a = physicsCollisionEvent.getObjectA();
		PhysicsCollisionObject b = physicsCollisionEvent.getObjectB();

		String aName = ((GhostControl) a).getSpatial().getName();
		String bName = ((GhostControl) b).getSpatial().getName();

		Spatial stick = null;
		Spatial drum = null;
		if (aName.equals("right_stick") || aName.equals("left_stick")) {
			stick = ((GhostControl) a).getSpatial();
			drum = ((GhostControl) b).getSpatial();
		} else if (bName.equals("right_stick") || bName.equals(("left_stick"))) {
			stick = ((GhostControl) b).getSpatial();
			drum = ((GhostControl) a).getSpatial();
		}

		if (stick != null) {
			boolean handClear = stick.getUserData(StickData.CLEAR.getKey());
			float handVelocityY = ((Vector3f) stick.getUserData(StickData.VELOCITY.getKey())).getY();
			// TODO Only hit drum with downward strokes. i.e stick y velocity is negative
			if (handClear && handVelocityY < 0) {
				stick.setUserData(StickData.CLEAR.getKey(), false);
				String audioName = drum.getUserData(DrumData.AUDIO_NAME.getKey());
				// TODO use collision groups so sticks can't collide with each other
				playDrum(audioName, Math.abs(handVelocityY * 100));
			}
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace physicsSpace, float v) {
	}

	@Override
	public void physicsTick(PhysicsSpace physicsSpace, float v) {
		if (rightStickGhost.getOverlappingObjects().isEmpty()) {
			rightStick.setUserData(StickData.CLEAR.getKey(), true);
		}

		if (leftStickGhost.getOverlappingObjects().isEmpty()) {
			leftStick.setUserData(StickData.CLEAR.getKey(), true);
		}
	}

	/**Plays the sound of the drum
	 * @param audioName name of the sound file. Must be a wav and end as ".wav"
	 * @param volume The volume is proportional the hit force
	 */
	private void playDrum(String audioName, float volume) {
		AudioNode sound = new AudioNode(assetManager, "Sounds/" + audioName + ".wav", AudioData.DataType.Buffer);
		sound.setPositional(false);
		sound.setVolume(volume);
		sound.playInstance();
	}
}
