package com.tomas.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
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
		nifty.addScreen("start", new ScreenBuilder("start") {{
					controller(controller);
					layer(new LayerBuilder("background") {{
						childLayoutCenter();

						panel(new PanelBuilder("kinect_status") {{
							childLayoutCenter();
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
						}});
					}});
				}}.build(nifty)
		);

		nifty.gotoScreen("start");
	}

	@Override
	protected void onDisable() {

	}
}
