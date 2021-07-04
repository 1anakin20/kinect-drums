package com.tomas.wiimote;

import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class WiimoteMotion extends WiimoteEventsAdapter {
	private float zForce;

	@Override
	public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {
		zForce = motionSensingEvent.getGforce().getZ();
	}

	public float getzForce() {
		return zForce;
	}
}