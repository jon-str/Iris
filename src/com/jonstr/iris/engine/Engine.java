package com.jonstr.iris.engine;

import com.jonstr.iris.game.IrisComponent;
import com.jonstr.iris.rendering.Bitmap;

public class Engine implements Runnable {
	private boolean running;

	private double frameRate;
	
	private Thread thread;
	private Display display;
	
	private Bitmap frameBuffer;
	
	private IrisComponent irisComponent;

	public Engine(int window_width, int window_height, String window_title,
			double frameRate) {
		this.display = new Display(window_width, window_height, window_title);
		this.frameRate = frameRate;
		frameBuffer = display.getFrameBuffer();

		
		irisComponent = new IrisComponent();
	}

	public void start() {
		if (running) return;
		running = true;
		thread = new Thread(this); // ?
		thread.start();
	}

	public void stop() {
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
				this.update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					double frameTimeMilis = 1000.0 / frames;
					System.out.println(frames + " fps, " + frameTimeMilis
							+ " ms");
					lastTime += 1000;
					frames = 0;
					secondFlag = true;
				}
				if (tickCount % 1 == 0) {
					halfSecondFlag = true;
					if (irisComponent.handleInput(Display.getKeys())) {
						irisComponent.update(this);
					}
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

	public void update() {
		// irisTesting.update();
		// irisTesting.handleInput(display.getKeys());
		if (secondFlag) {
			irisComponent.update(this);
			secondFlag = false;
		}
	}

	public void render() {
		if (!isRunning())
			return;
		display.clear();
		{
			// frameBuffer.fill(0xFF000000);
			frameBuffer.blit(irisComponent, 8, 8);
			// frameBuffer.blit(irisGrid, 8, 8);
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