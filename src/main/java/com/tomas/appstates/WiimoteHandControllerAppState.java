package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.tomas.wiimote.WiimoteLifeCycleEvents;
import com.tomas.wiimote.WiimoteManager;
import com.tomas.wiimote.WiimoteMotion;
import wiiusej.Wiimote;

/**
 * Register the hits to the drum by stroking down the wiimote
 */
// TODO Register the hits with the wiimote accelerometer
public class WiimoteHandControllerAppState extends BaseAppState implements WiimoteLifeCycleEvents {
	private AssetManager assetManager;
	private Application app;
	private Wiimote rightWiimote;
	private Wiimote leftWiimote;
	private SticksAppState sticksAppState;

	public WiimoteHandControllerAppState(SticksAppState sticksAppState) {
		this.sticksAppState = sticksAppState;
	}

	@Override
	protected void initialize(Application app) {
		this.app = app;
		assetManager = app.getAssetManager();
		WiimoteManager wiimoteManager = new WiimoteManager();
		wiimoteManager.registerListener(this);
		wiimoteManager.startLookingForWiimotes(2);
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
	public void connected(Wiimote[] wiimotes) {
		rightWiimote = wiimotes[0];
		leftWiimote = wiimotes[1];

		leftWiimote.setLeds(true, false, false, false);
		rightWiimote.setLeds(false, true, false, false);

		rightWiimote.activateMotionSensing();
		rightWiimote.activateSmoothing();
//		rightWiimote.activateContinuous();
		leftWiimote.activateMotionSensing();
		leftWiimote.activateSmoothing();
//		leftWiimote.activateContinuous();

		WiimoteMotion rightWiimoteMotion = new WiimoteMotion(app, assetManager, sticksAppState.getRightStickGhost());
		rightWiimote.addWiiMoteEventListeners(rightWiimoteMotion);

		WiimoteMotion leftWiimoteMotion = new WiimoteMotion(app, assetManager, sticksAppState.getLeftStickGhost());
		leftWiimote.addWiiMoteEventListeners(leftWiimoteMotion);
	}

	@Override
	public void disconnected() {

	}
}
