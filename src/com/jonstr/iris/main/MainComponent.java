package com.jonstr.iris.main;

import com.jonstr.iris.engine.Engine;

public class MainComponent {
	
	private static final int WINDOW_WIDTH = 480;
	private static final int WINDOW_HEIGHT = 594;
	private static final String WINDOW_TITLE = "IRIS_0_0.001";
	
	private Engine engine;
	
	public MainComponent() {
		engine = new Engine(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, 30.0);
	}
	
	public static void main(String args[]) {
		new MainComponent().engine.start();
	}
}
