package com.jonstr.iris.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	public boolean[] keys;
	private boolean keyDelay = false;
	
	private boolean keyHeld;
	
	public InputHandler() {
		keys = new boolean[65536];
		keyDelay = true;
		keyHeld = false;
	}
	
	public void setKeyDelay(boolean value) { this.keyDelay = value; }
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(keyDelay) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		if(!keyHeld) {
			keyHeld = true;
			int code = e.getKeyCode();
			if(code > 0 && code  < keys.length) {
				keys[code] = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code > 0 && code  < keys.length) {
			keys[code] = false;
		}
		
		keyHeld = false;
	}

}
