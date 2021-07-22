package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.tomas.wiimote.WiimoteEventsAdapter;
import com.tomas.wiimote.WiimoteLifeCycleEvents;
import com.tomas.wiimote.WiimoteManager;
import com.tomas.wiimote.WiimoteMotion;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

/**
 * Register the hits to the drum by stroking down the wiimote
 */
// TODO Register the hits with the wiimote accelerometer
public class WiimoteHandControllerAppState extends BaseAppState implements WiimoteLifeCycleEvents, WiimoteEventsAdapter {
	private Wiimote leftWiimote;
	private Wiimote rightWiimote;

	@Override
	protected void initialize(Application app) {
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
	public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {

	}

	@Override
	public void connected(Wiimote[] wiimotes) {
		leftWiimote = wiimotes[0];
		rightWiimote = wiimotes[1];

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
	}

	@Override
	public void disconnected() {

	}
}
