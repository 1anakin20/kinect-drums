package com.tomas;

import com.github.awvalenti.wiiusej.WiiusejNativeLibraryLoadingException;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.audio.AudioListenerState;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.tools.javac.Main;
import com.tomas.kinect.Kinect;
import com.tomas.kinect.control.Hand;
import com.tomas.kinect.control.KinectHandControl;
import com.tomas.wiimote.WiimoteMotion;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends SimpleApplication {
	private final String scenesPath = "assets/scenes/";
	Kinect kinect;
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

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
		Wiimote[] wiimotes = wiiUseApiManager.getWiimotes(2);
		Wiimote leftWiimote = wiimotes[0];
		Wiimote rightWiimote = wiimotes[1];

		leftWiimote.setLeds(true, false, false, false);
		rightWiimote.setLeds(false, true, false, false);

		leftWiimote.activateMotionSensing();
		leftWiimote.activateSmoothing();
		leftWiimote.setAlphaSmoothingValue(0.8f);
		leftWiimote.activateContinuous();
		rightWiimote.activateMotionSensing();
		rightWiimote.activateSmoothing();
		rightWiimote.activateContinuous();

		WiimoteMotion leftWiimoteMotion = new WiimoteMotion();
		leftWiimote.addWiiMoteEventListeners(leftWiimoteMotion);

		WiimoteMotion rightWiimoteMotion = new WiimoteMotion();
		rightWiimote.addWiiMoteEventListeners(rightWiimoteMotion);


		Spatial leftStick = rootNode.getChild("left_stick");
		leftStick.addControl(new KinectHandControl(kinect, Hand.LEFT_HAND));

		Spatial rightStick = rootNode.getChild("right_stick");
		rightStick.addControl(new KinectHandControl(kinect, Hand.RIGHT_HAND));
	}
}
