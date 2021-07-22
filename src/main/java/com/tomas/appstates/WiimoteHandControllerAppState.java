package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import wiiusej.Wiimote;

/**
 * Register the hits to the drum by stroking down the wiimote
 */
// TODO Register the hits with the wiimote accelerometer
public class WiimoteHandControllerAppState extends BaseAppState {
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	@Override
	protected void initialize(Application app) {
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
}
