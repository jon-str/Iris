package com.jonstr.iris.engine;

import com.jonstr.iris.game.Game;
import com.jonstr.iris.rendering.Bitmap;

public class Engine implements Runnable {
	
	public static InputHandler input;

	private boolean running;

	private Thread thread;
	private Display display;
	private Bitmap frameBuffer;

	private Game game;

	public Engine(int window_width, int window_height, String window_title) {
		display = new Display(window_width, window_height, window_title);
		input = new InputHandler();
		display.addKeyListener(input);
		frameBuffer = display.getFrameBuffer();

		game = new Game();
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		display.requestFocus();
		
		// int frames = 0;
		long unprocFrames = 0;

		while (running) {
			long startTime = System.nanoTime();

			update();
			render();

			long endTime = System.nanoTime();
			long frameTime = endTime - startTime;
			unprocFrames += frameTime;
			if (unprocFrames >= 1000000000L / 2) { // ENCAP (GAME SPEED FOR LEVELS)
				// System.out.println(unprocFrames + " nanos, " + frames + " fps");

				game.setDropFlag(true);
				// frames = 0;
				unprocFrames = 0;
			} else {
				game.setDropFlag(false);
				// frames++;
			}
		}
	}

	private void update() { 
		game.update(this, input.keys);
	}
	
	private void render() {
		if (!running) return;

		game.render();

		display.clear();
		{
			frameBuffer.fill(0xFF000000);
			for (int i = 0; i < game.getDrawList().length; ++i) {
				frameBuffer.blit(game.getBitmapToDraw(i), 
								 game.getDrawListAt(i).getXAbsolute(), 
								 game.getDrawListAt(i).getYAbsolute());
			}
		}
		display.swapBuffers(game.getScore());
	}

}