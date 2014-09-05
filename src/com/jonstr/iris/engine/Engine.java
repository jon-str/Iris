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
		
//		tgc = new TestGC(2, display.getWidth() / 2 - 1, display.getWidth() / 2 - 1, 0xFF00FFFF);
		tgc = new TestGC(6, 8, 16, 0xFFFF00FF);
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
	
//	public void update(boolean dropFlag) {
//		if(irisComponent.handleInput(Display.getKeys())) {
//			irisComponent.update(this, false, true);
//		}
//		
//		irisComponent.update(this, dropFlag, false);
//		if(this.dropFlag == true) {
//			this.dropFlag = false;
//		}
//	}
	
	@Override
	public void run() {
		long unprocFrames = 0;
		int frames = 0;
		
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
				frames = 0;
			} else {
				frames++;
				dropFlag = false;
			}
		}
	}
	
	
	
	public void update() {
		tgc.update(this, false, tgc.handleInput(Display.getKeys()));
		tgc.update(this, dropFlag, false);
	}

	public void render() {
		if (!isRunning()) return;
		//irisComponent.nextBlockGrid.fill(0xFF000000);
		
		tgc.render();
		
		display.clear();
		{
			frameBuffer.fill(0xFF000000);
			frameBuffer.blit(tgc, 8, 8);
			
//			frameBuffer.blit(irisComponent, 8, 8);
//			{
//				irisComponent.drawGridLines(irisComponent.nextBlockGrid);
//				for(int i = 0; i < 4; ++i) {
//					int x = irisComponent.nextShape.getX(i);
//					int y = irisComponent.nextShape.getY(i);
//					irisComponent.drawNextShape(irisComponent.nextBlockGrid, x, y);
//				}
//			}
//			frameBuffer.blit(irisComponent.nextBlockGrid, 51 * 6, 67 * 6 + 2);
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
}