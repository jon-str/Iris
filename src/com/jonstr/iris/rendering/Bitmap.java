package com.jonstr.iris.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Bitmap {
	
	protected int width;
	protected int height;
	protected int totalPixels;
	protected int pixels[];
	
	public static Bitmap loadBitmap(String fileName) { return new Bitmap(fileName); }

	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		totalPixels = this.width * this.height;
		pixels = new int[totalPixels];
	}
	
	public Bitmap(Bitmap bitmap) {
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		pixels = bitmap.getPixels();
		totalPixels = bitmap.getTotalPixels();
	}
	
	public Bitmap(String fileName) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
			width = img.getWidth();
			height = img.getHeight();
			totalPixels = width * height;
			pixels = new int[totalPixels];
			img.getRGB(0, 0, width, height, pixels, 0, width);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bitmap initAsSolid(int color) {
		Arrays.fill(pixels, color);
		return this;
	}
	
	public Bitmap initAsWhite() { return initAsSolid(0xFFFFFFFF); }
	public Bitmap initAsBlack() { return initAsSolid(0xFF000000); }
	
	public Bitmap initAsImage(String fileName) { return Bitmap.loadBitmap(fileName); } 
	
	public void blit(Bitmap bitmap, int xOffs, int yOffs) {
		for(int y = 0; y < bitmap.getHeight(); ++y) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			
			for(int x = 0; x < bitmap.getWidth(); ++x) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				
				int src = bitmap.getPixel(x + y * bitmap.getWidth());
				pixels[xPix + yPix * width] = src;
			}
		}
	}
	
	public void fill(int color) { Arrays.fill(pixels, color); }
	
	public void fill(int xMin, int yMin, int xMax, int yMax, int color) {
		yMax -= 1;
		int from = (xMin + yMin * width);
		int to = (xMax + yMax * width);	
		Arrays.fill(pixels, from, to, color);
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int[] getPixels() { return pixels; }
	public int getTotalPixels() { return totalPixels; }

	public int getPixel(int i) { return pixels[i]; }
	public int getPixel(int x, int y) { return getPixel(x + y * width); }
	
	public void setPixel(int i, int value) { pixels[i] = value; }
	public void setPixel(int x, int y, int value) { setPixel(x + y * width, value); }
	
}

