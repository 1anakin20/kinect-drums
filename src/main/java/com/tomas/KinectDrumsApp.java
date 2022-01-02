package com.tomas;

import com.tomas.configuration.Configuration;
import com.tomas.configuration.ConfigurationLoader;

public class KinectDrumsApp {
	public static void main(String[] args) {
		Configuration configuration = ConfigurationLoader.loadConfiguration();
		System.out.println(configuration);
		Game game = new Game();
		game.start();
	}
}
