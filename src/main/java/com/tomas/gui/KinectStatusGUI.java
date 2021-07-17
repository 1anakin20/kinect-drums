package com.tomas.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class KinectStatusGUI extends BaseAppState {
	private ScreenController controller;

	public KinectStatusGUI(ScreenController controller) {
		this.controller = controller;
	}

	@Override
	protected void initialize(Application app) {

	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {
		NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
				getApplication().getAssetManager(),
				getApplication().getInputManager(),
				getApplication().getAudioRenderer(),
				getApplication().getGuiViewPort()
		);

		Nifty nifty = niftyDisplay.getNifty();
		getApplication().getGuiViewPort().addProcessor(niftyDisplay);
		Screen start = new ScreenBuilder("start") {{
			controller(controller);
			layer(new LayerBuilder("background") {{
				childLayoutCenter();
				childLayoutVertical();

				panel(new PanelBuilder("kinect_status") {{
					alignCenter();
					valignTop();
					backgroundColor("#FF0000");
					height("32");
					width("32");
					image(new ImageBuilder() {{
						filename("Interface/kinect.png");
						height("32");
						width("32");
					}});

					text(new TextBuilder("message") {{
						alignCenter();
						valignTop();
						font("aurulent-sans-16.fnt");
						color("#FFFF00");
						text("messages will appear here");
					}});
				}});
			}});
		}}.build(nifty);

		nifty.addScreen("start" ,start);
		nifty.gotoScreen("start");
	}

	@Override
	protected void onDisable() {

	}
}
