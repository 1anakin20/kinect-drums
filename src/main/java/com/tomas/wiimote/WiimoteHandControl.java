package com.tomas.wiimote;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import wiiusej.values.GForce;
import wiiusej.values.Orientation;
import wiiusej.values.RawAcceleration;

public class WiimoteHandControl extends AbstractControl {
	private WiimoteMotion wiimoteMotion;

	public WiimoteHandControl(WiimoteMotion wiimoteMotion) {
		this.wiimoteMotion = wiimoteMotion;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.setLocalRotation(wiimoteMotion.getRotation());
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
