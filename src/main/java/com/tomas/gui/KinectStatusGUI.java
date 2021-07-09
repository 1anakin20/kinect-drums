package com.tomas.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.tomas.kinect.Kinect;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;

public class KinectStatusGUI extends BaseAppState {
	private Kinect kinect;

	public KinectStatusGUI(Kinect kinect) {
		this.kinect = kinect;
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
					KinectStatusController controller = new KinectStatusController();
					kinect.registerListener(controller);
					controller(controller);
					layer(new LayerBuilder("background") {{
						childLayoutCenter();

						panel(new PanelBuilder("kinect_status") {{
							childLayoutCenter();
							alignCenter();
							valignTop();
							backgroundColor("#FF0000");
							height("20");
							width("20");
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
