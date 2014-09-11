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
	
	/* WORKS */
	//Bitmap bmp_img = Bitmap.loadBitmap("res/bricks.jpg");
	/* OR */
	//Bitmap bmp_img = new Bitmap("res/bricks.jpg");

	public Engine(int window_width, int window_height, String window_title) {
		display = new Display(window_width, window_height, window_title);
		input = new InputHandler();
		display.addKeyListener(input);
		frameBuffer = display.getFrameBuffer();
		
		irisGame = new IrisMainComponent(6, 8, 16, 0xFFFF00FF);
		irisNextShapeGrid = new IrisNextShapeComponent(irisGame, irisGame.getBlockSize(), 5, 5, irisGame.getGridLineColor());
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
		
		dropFlag = false; // ENCAP
		
		display.requestFocus();
		
		while(isRunning()) {
			long startTime = System.nanoTime();
			
			update();
			render();
			
			long endTime = System.nanoTime();
			long frameTime = endTime - startTime;
			unprocFrames += frameTime;
			if(unprocFrames >= 1000000000L) { // ENCAP (GAME SPEED FOR LEVELS)
				//System.out.println(unprocFrames + " nanos, " + frames + " fps");
				
				dropFlag = true; // ENCAP
				unprocFrames = 0;
//				frames = 0;
			} else {
				dropFlag = false; // ENCAP
//				frames++;
			}
		}
	}
	
	
	
	private void update() {
		irisGame.update(this, false, irisGame.handleInput(getKeys())); // ENCAP
		irisGame.update(this, dropFlag, false); // ENCAP
	}
	
	// ENCAP
	int xPadding = 8 + (6 * (6 * 8)) + 8;
	int yPadding = 8 + (6 * (6 * 11));
	
	private void render() {
		if (!isRunning()) return;
		
		irisGame.render(); // ENCAP
		irisNextShapeGrid.render(); // ENCAP
		
		display.clear();
		{
			frameBuffer.fill(0xFF000000);
			frameBuffer.blit(irisGame, 8, 8);
			frameBuffer.blit(irisNextShapeGrid, xPadding, yPadding);
			
			//frameBuffer.blit(bmp_img, 0, 0);
		}
		display.swapBuffers();
	}

	public boolean isRunning() { return running; }
	
	public static boolean[] getKeys() { return input.keys; }
}