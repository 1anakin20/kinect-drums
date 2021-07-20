package com.tomas.gui;

import com.tomas.kinect.KinectEvents;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

public class KinectStatusController implements ScreenController, KinectEvents {
	private Nifty nifty;

	@Override
	public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
		this.nifty = nifty;
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
		Element message = nifty.getCurrentScreen().findElementById("kinect_message");
		message.getRenderer(TextRenderer.class).setText("Kinect could not load. Make sure there isn't another application using it.");
	}
}
