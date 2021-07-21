package com.tomas;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.tomas.appstates.DrumPlayingAppState;
import com.tomas.appstates.KinectBassDrumPedalAppState;
import com.tomas.appstates.KinectHandControllerAppState;
import com.tomas.appstates.SticksAppState;
import com.tomas.gui.KinectStatusController;
import com.tomas.kinect.Kinect;
import de.lessvoid.nifty.Nifty;
import wiiusej.Wiimote;

public class Game extends SimpleApplication {
	private BulletAppState bulletAppState;
	private SticksAppState sticksAppState;
	private KinectHandControllerAppState kinectHandControllerAppState;
	private KinectBassDrumPedalAppState kinectBassDrumPedalAppState;

	private Kinect kinect;
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	Game() {
		super(new StatsAppState(),
				new AudioListenerState(),
				new DebugKeysAppState(),
				new ConstantVerifierState(),
				// TODO Change fly cam to static non-movable camera
				new FlyCamAppState());
	}

	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class);

		kinect = Kinect.getInstance();
		bulletAppState = new BulletAppState();

		stateManager.attach(bulletAppState);

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
		nifty.gotoScreen("start");

		// Kinect setup
		kinect.registerListener(kinectStatusController);
		kinect.loadKinect(true, Kinect.NUI_IMAGE_RESOLUTION_640x480, Kinect.NUI_IMAGE_RESOLUTION_640x480, false);

		// States setup
		DrumPlayingAppState drumPlayingAppState = new DrumPlayingAppState();
		stateManager.attach(drumPlayingAppState);

		sticksAppState = new SticksAppState();
		stateManager.attach(sticksAppState);

		kinectHandControllerAppState = new KinectHandControllerAppState(sticksAppState);
		stateManager.attach(kinectHandControllerAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(kinectHandControllerAppState);
		bulletAppState.getPhysicsSpace().addTickListener(kinectHandControllerAppState);

		kinectBassDrumPedalAppState = new KinectBassDrumPedalAppState();
		stateManager.attach(kinectBassDrumPedalAppState);
		bulletAppState.getPhysicsSpace().addTickListener(kinectBassDrumPedalAppState);
	}

	@Override
	public void simpleUpdate(float tpf) {
	}
}