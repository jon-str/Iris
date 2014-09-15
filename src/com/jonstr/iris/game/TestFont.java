package com.jonstr.iris.game;

import com.jonstr.iris.rendering.Bitmap;

public class TestFont extends Bitmap {
	
	public TestFont() {
		super("res/font.png");
	}
	
	public static int getStringWidth(String msg) { return msg.length() * 8; }
	
	public void scribe(Bitmap bitmap, String msg, int x, int y) {
		msg = msg.toUpperCase();
		for(int i = 0; i < msg.length(); ++i) {
			int c = chars.indexOf(msg.charAt(i));
			if(c < 0) continue;
		}
	}
	
}
