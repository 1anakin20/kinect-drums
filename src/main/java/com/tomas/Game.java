package com.tomas;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.tools.javac.Main;
import com.tomas.appstates.HandControllerAppState;
import com.tomas.appstates.SticksAppState;
import com.tomas.gui.KinectStatusController;
import com.tomas.kinect.Joint;
import com.tomas.kinect.Kinect;
import com.tomas.kinect.control.Velocity;
import com.tomas.properties.CollisionGroups;
import com.tomas.properties.DrumData;
import com.tomas.utils.SoundManager;
import de.lessvoid.nifty.Nifty;
import wiiusej.Wiimote;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends SimpleApplication implements PhysicsCollisionListener, PhysicsTickListener {
	private final BulletAppState bulletAppState;
	private final SticksAppState sticksAppState;
	private final HandControllerAppState handControllerAppState;

	private final Kinect kinect;
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	private final Velocity bassPedalVelocity = new Velocity();
	private boolean isBassPedalEnabled = false;
	private boolean canHitBass = false;

	Game() {
		super(new StatsAppState(),
				new AudioListenerState(),
				new DebugKeysAppState(),
				new ConstantVerifierState(),
				// TODO Change fly cam to static non-movable camera
				new FlyCamAppState());

		kinect = Kinect.getInstance();
		bulletAppState = new BulletAppState();
	}

	public void simpleInitApp() {
		// Debug options
		bulletAppState.setDebugEnabled(true);

		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);

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

		// GUI setup
		NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
				getAssetManager(),
				getInputManager(),
				getAudioRenderer(),
				getGuiViewPort()
		);

		Nifty nifty = niftyDisplay.getNifty();
		getGuiViewPort().addProcessor(niftyDisplay);
		KinectStatusController kinectStatusController = new KinectStatusController();
		nifty.fromXml("Interface/GUI/kinectStatusGUI.xml", "start", kinectStatusController);
//		nifty.setDebugOptionPanelColors(true);
		nifty.gotoScreen("start");

		// Kinect setup
		// TODO load it in a background thread and notify when it's loaded. If it fails show error
		kinect.loadKinect(true, Kinect.NUI_IMAGE_RESOLUTION_640x480, Kinect.NUI_IMAGE_RESOLUTION_640x480, false);
		kinect.registerListener(kinectStatusController);

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
		sticksAppState = new SticksAppState();
		stateManager.attach(sticksAppState);

		handControllerAppState = new HandControllerAppState(sticksAppState);
		stateManager.attach(handControllerAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(handControllerAppState);
		bulletAppState.getPhysicsSpace().addTickListener(handControllerAppState);

		setGameObjects();
	}

	@Override
	public void simpleUpdate(float tpf) {

	}

	private void setGameObjects() {
		// Drum
		String[] drumNames = new String[] {
				"floor_tom",
				"ride_cymbal",
				"mid_tom",
				"high_tom",
				"crash_cymbal",
				"hi_hat",
				"snare_drum"
		};
		for (String name : drumNames) {
			createDrum(name, name);
		}
	}

	private void createDrum(String drumName, String audioName) {
		Spatial spatial = rootNode.getChild(drumName);
		spatial.setUserData(DrumData.AUDIO_NAME.getKey(), audioName);

		CollisionShape collisionShape = CollisionShapeFactory.createDynamicMeshShape(spatial);
		collisionShape.setMargin(0);
		GhostControl ghostControl = new GhostControl(collisionShape);
		ghostControl.setCollisionGroup(CollisionGroups.DRUMS.getCollisionGroup());
		spatial.addControl(ghostControl);
		bulletAppState.getPhysicsSpace().add(ghostControl);
	}

	@Override
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {

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