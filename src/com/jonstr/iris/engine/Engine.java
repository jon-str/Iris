package com.jonstr.iris.engine;

import com.jonstr.iris.game.IrisMainComponent;
import com.jonstr.iris.game.IrisNextShapeComponent;
import com.jonstr.iris.rendering.Bitmap;

public class Engine implements Runnable {
	public static InputHandler input;
	
	private boolean running;
	
	private boolean dropFlag;
	
	private Thread thread;
	private Display display;
	private Bitmap frameBuffer;
	
	IrisMainComponent irisGame;
	IrisNextShapeComponent irisNextShapeGrid;

	public Engine(int window_width, int window_height, String window_title) {
		this.display = new Display(window_width, window_height, window_title);
		input = new InputHandler();
		display.addKeyListener(input);
		frameBuffer = display.getFrameBuffer();
		
		irisGame = new IrisMainComponent(6, 8, 16, 0xFFFF00FF);
		irisNextShapeGrid = new IrisNextShapeComponent(irisGame, 6, 5, 5, 0xFFFF00FF);
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
//		int frames = 0;
		long unprocFrames = 0;
		
		dropFlag = false;
		
		display.requestFocus();
		
		while(isRunning()) {
			long startTime = System.nanoTime();
			
			update();
			//update();
			render();
			
			long endTime = System.nanoTime();
			long frameTime = endTime - startTime;
			unprocFrames += frameTime;
			if(unprocFrames >= 1000000000L) {
				//System.out.println(unprocFrames + " nanos, " + frames + " fps?");
				dropFlag = true;
				unprocFrames = 0;
//				frames = 0;
			} else {
				dropFlag = false;
//				frames++;
			}
		}
	}
	
	
	
	public void update() {
		irisGame.update(this, false, irisGame.handleInput(getKeys()));
		irisGame.update(this, dropFlag, false);
	}

	public void render() {
		if (!isRunning()) return;
		
		irisGame.render();
		irisNextShapeGrid.render();
		
		display.clear();
		{
			frameBuffer.fill(0xFF000000);
			frameBuffer.blit(irisGame, 8, 8);
			frameBuffer.blit(irisNextShapeGrid, 8 + (6 * (6 * 8)) + 8, 8 + (6 * (6 * 11)));
		}
		display.swapBuffers();
	}

	public boolean isRunning() {
		return this.running;
	}

	public void shouldQuit() {
		this.running = false;
	}

	public Display getDisplay() {
		return display;
	}
	
	public static boolean[] getKeys() { return input.keys; }
}