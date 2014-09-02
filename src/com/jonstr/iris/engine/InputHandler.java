package com.jonstr.iris.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	public enum IrisEvent {
		NoEvent,
		Left,
		Right,
		Down,
		Rotate
	};
	
	public boolean[] keys;
	
	IrisEvent lastEvent;
	
	public InputHandler() {
		keys = new boolean[65536];
		lastEvent = IrisEvent.NoEvent;
	}
	
	public void setLastEvent(IrisEvent lastEvent) {
		this.lastEvent = lastEvent;
	}
	
	public IrisEvent getLastEvent() {
		return this.lastEvent;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code > 0 && code  < keys.length) {
			keys[code] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code > 0 && code  < keys.length) {
			keys[code] = false;
		}
		
		lastEvent = IrisEvent.NoEvent;
	}
	
	public boolean useKey(int keyCode) {
		boolean temp = keys[keyCode];
		keys[keyCode] = false;
		return temp;
	}

}
