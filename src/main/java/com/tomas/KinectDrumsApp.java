package com.tomas;

import com.tomas.configuration.ConfigurationLoader;

public class KinectDrumsApp {
	public static void main(String[] args) {
		ConfigurationLoader.loadConfiguration();
		Game game = new Game();
		game.start();
	}
}
