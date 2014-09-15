package com.jonstr.iris.game;

import com.jonstr.iris.rendering.Bitmap;

public class Art {
	public static Bitmap font = new Bitmap("res/font.png");

	public static int getCol(int c) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = (c) & 0xff;
		r = r * 0x55 / 0xff;
		g = g * 0x55 / 0xff;
		b = b * 0x55 / 0xff;
		return r << 16 | g << 8 | b;
	}
}
