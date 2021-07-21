package com.tomas.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.tools.javac.Main;
import com.tomas.properties.CollisionGroups;
import com.tomas.properties.DrumData;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrumPlayingAppState extends BaseAppState {
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private Node rootNode;

	@Override
	protected void initialize(Application app) {
		assetManager = app.getAssetManager();
		bulletAppState = app.getStateManager().getState(BulletAppState.class);
		rootNode = ((SimpleApplication) app).getRootNode();

		BinaryImporter importer = BinaryImporter.getInstance();
		importer.setAssetManager(assetManager);
		File file = new File("assets/Scenes/drums.j3o");
		try {
			Node loadedNode = (Node) importer.load(file);
			loadedNode.setName("drum");
			rootNode.attachChild(loadedNode);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
		}

		setGameObjects();
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

	private void setGameObjects() {
		// Drum
		String[] drumNames = new String[] {
				"floor_tom",
				"ride_cymbal",
				"mid_tom",
				"high_tom",
				"crash_cymbal",
				"hi_hat",
				"snare_drum"
		};
		for (String name : drumNames) {
			createDrum(name, name);
		}
	}

	private void createDrum(String drumName, String audioName) {
		Spatial spatial = rootNode.getChild(drumName);
		spatial.setUserData(DrumData.AUDIO_NAME.getKey(), audioName);

		CollisionShape collisionShape = CollisionShapeFactory.createDynamicMeshShape(spatial);
		collisionShape.setMargin(0);
		GhostControl ghostControl = new GhostControl(collisionShape);
		ghostControl.setCollisionGroup(CollisionGroups.DRUMS.getCollisionGroup());
		spatial.addControl(ghostControl);
		bulletAppState.getPhysicsSpace().add(ghostControl);
	}
}
