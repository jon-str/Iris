package com.jonstr.iris.engine;

import com.jonstr.iris.game.GridComponent;
import com.jonstr.iris.game.IrisComponent;
import com.jonstr.iris.game.TestGC;
import com.jonstr.iris.rendering.Bitmap;

public class Engine implements Runnable {
	private boolean running;

	private double frameRate;
	
	private boolean dropFlag;
	
	private Thread thread;
	private Display display;
	
	private Bitmap frameBuffer;
	
	private IrisComponent irisComponent;
	
	TestGC tgc;

	public Engine(int window_width, int window_height, String window_title,
			double frameRate) {
		this.display = new Display(window_width, window_height, window_title);
		this.frameRate = frameRate;
		frameBuffer = display.getFrameBuffer();

		
		irisComponent = new IrisComponent();
		
		tgc = new TestGC(2, display.getWidth() / 2 - 1, display.getWidth() / 2 - 1, 0xFF00FFFF);
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this); // ?
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

	boolean secondFlag = false;
	boolean halfSecondFlag = false;
	
	/*
	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1.0 / this.frameRate;
		int tickCount = 0;
		boolean throttle = true;
		;
		this.display.requestFocus();
		while (isRunning()) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0)
				passedTime = 0;
			if (passedTime > 100000000)
				passedTime = 100000000;
			unprocessedSeconds += passedTime / 1000000000.0;
			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				if(irisComponent.handleInput(Display.getKeys())) {
					irisComponent.update(this, false, true);
				}
				
				this.update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 20 == 0) {
					double frameTimeMilis = 1000.0 / frames;
					System.out.println(frames + " fps, " + frameTimeMilis
							+ " ms");
					lastTime += 1000;
					frames = 0;
					secondFlag = true;
				}
				
			}
			if (ticked && throttle) {
				this.render();
				frames++;
			} else if (!throttle) {
				this.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	*/
	
	/*
	public void update(boolean dropFlag) {
		if(irisComponent.handleInput(Display.getKeys())) {
			irisComponent.update(this, false, true);
		}
		
		irisComponent.update(this, dropFlag, false);
		if(this.dropFlag == true) {
			this.dropFlag = false;
		}
	}
	*/
	@Override
	public void run() {
		long unprocFrames = 0;
		int frames = 0;
		dropFlag = false;
		
		display.requestFocus();
		
		while(isRunning()) {
			long startTime = System.nanoTime();
			
//			update(dropFlag);
			update();
			render();
			
			long endTime = System.nanoTime();
			long frameTime = endTime - startTime;
			unprocFrames += frameTime;
			if(unprocFrames >= 1000000000L) {
				//System.out.println(unprocFrames + " nanos, " + frames + " fps?");
				
				dropFlag = true;
				
				unprocFrames = 0;
				frames = 0;
			}
			else {
				frames++;
			}
		}
	}
	
	
	
	public void update() {
		tgc.update();
	}

	public void render() {
		if (!isRunning()) return;
//		irisComponent.nextBlockGrid.fill(0xFF000000);
		
		display.clear();
		{
			frameBuffer.fill(0xFF000000);
			frameBuffer.blit(tgc, 0, 0);
			/*
			frameBuffer.blit(irisComponent, 8, 8);
			{
				irisComponent.drawGridLines(irisComponent.nextBlockGrid);
				for(int i = 0; i < 4; ++i) {
					int x = irisComponent.nextShape.getX(i);
					int y = irisComponent.nextShape.getY(i);
					irisComponent.drawNextShape(irisComponent.nextBlockGrid, x, y);
				}
			}
			frameBuffer.blit(irisComponent.nextBlockGrid, 51 * 6, 67 * 6 + 2);
			*/
		}
		display.swapBuffers();
	}

	public boolean isRunning() {
		return this.running;
	}

	public boolean secondFlagIsRaised() {
		return this.secondFlag;
	}

	public void shouldQuit() {
		this.running = false;
	}

	public Display getDisplay() {
		return display;
	}
}