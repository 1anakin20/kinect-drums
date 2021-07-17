package com.tomas.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.tomas.kinect.KinectEvents;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

public class KinectStatusController extends BaseAppState implements ScreenController, KinectEvents {
	private Nifty nifty;
	private Screen screen;

	@Override
	public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onStartScreen() {

	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void kinectLoaded() {
		Element statusIcon = nifty.getCurrentScreen().findElementById("kinect_status");
		statusIcon.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#008000"));
	}

	@Override
	public void kinectCouldNotLoad() {

	}

	@Override
	protected void initialize(Application app) {

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
