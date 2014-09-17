package com.jonstr.iris.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.jonstr.iris.game.Art;

public class Bitmap {

	protected int width;
	protected int height;
	protected int totalPixels;
	protected int pixels[];

	public static final String chars = "" + //
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}" + //
			"abcdefghijklmnopqrstuvwxyz_ " + //
			"0123456789+-=*:;ÖÅÄå " + //
			"";

	public static Bitmap loadBitmap(String fileName) {
		return new Bitmap(fileName);
	}

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
	
	public Bitmap copy(Bitmap bitmap) {
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		pixels = bitmap.getPixels();
		totalPixels = bitmap.getTotalPixels();
		
		return this;
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
	
	public Bitmap scaleCopy(int factor) {
		Bitmap result = new Bitmap(width * factor, height * factor);
		//result.fill(0x00FF00FF);
		for(int y = 0; y < this.height; ++y) {
			for(int x  = 0; x < this.width; ++x) {
				int color = this.getPixel(x, y);
				drawScalePixel(result, factor, x, y, color);
			}
		}
		
		return result;
	}
	
	private void drawScalePixel(Bitmap bitmap, int factor, int x, int y, int value) {
		for(int yy = 0; yy < factor; ++yy) {
			for(int xx = 0; xx < factor; ++xx) {
				bitmap.setPixel(x + xx, y + yy, value);
			}
		}
	}

	public Bitmap initAsSolid(int color) {
		Arrays.fill(pixels, color);
		return this;
	}

	public Bitmap initAsWhite() {
		return initAsSolid(0xFFFFFFFF);
	}

	public Bitmap initAsBlack() {
		return initAsSolid(0xFF000000);
	}

	public Bitmap initAsImage(String fileName) {
		return Bitmap.loadBitmap(fileName);
	}

	public void blit(Bitmap bitmap, int xOffs, int yOffs) {
		for (int y = 0; y < bitmap.getHeight(); ++y) {
			int yPix = y + yOffs;
			if (yPix < 0 || yPix >= height)
				continue;

			for (int x = 0; x < bitmap.getWidth(); ++x) {
				int xPix = x + xOffs;
				if (xPix < 0 || xPix >= width)
					continue;

				int src = bitmap.getPixel(x + y * bitmap.getWidth());
				//System.out.println(bitmap.totalPixels);
				pixels[xPix + yPix * width] = src;
			}
		}
	}

	public void fill(int color) {
		Arrays.fill(pixels, color);
	}
	
	public void scaleBlock(int factor, int x, int y, int color) {
		for(int yy = 0; yy < factor; ++yy) {
			for(int xx = 0; xx < factor; ++xx) {
				pixels[(x + xx) + (y + yy) * width] = color;
			}
		}
	}
	
	public void scaleBlock(int arr[], int factor, int x, int y, int color) {
		for(int yy = 0; yy < factor; ++yy) {
			for(int xx = 0; xx < factor; ++xx) {
				arr[(x + xx) + (y + yy) * width] = color;
			}
		}
	}
	
	public void scale(int factor) {
		Bitmap temp = new Bitmap(width * factor, height * factor);
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				int color = pixels[x + y * width];
				drawScalePixel(temp, factor, x, y, color);
			}
		}
		this.copy(temp);
	}

	public void fill(int xMin, int yMin, int xMax, int yMax, int color) {
		yMax -= 1;
		int from = (xMin + yMin * width);
		int to = (xMax + yMax * width);
		Arrays.fill(pixels, from, to, color);
	}

	public void draw(Bitmap bitmap, int xOffs, int yOffs, int xo, int yo,
			int w, int h, int col) {
		for (int y = 0; y < h; y++) {
			int yPix = y + yOffs;
			if (yPix < 0 || yPix >= height)
				continue;

			for (int x = 0; x < w; x++) {
				int xPix = x + xOffs;
				if (xPix < 0 || xPix >= width)
					continue;

				int src = bitmap.pixels[(x + xo) + (y + yo) * bitmap.width];
				pixels[xPix + yPix * width] = src * col;
			}
		}
	}

	public void scaleDraw(Bitmap bitmap, int scale, int xOffs, int yOffs, int xo, int yo, int w, int h, int col) {
		for (int y = 0; y < h * scale; y++) {
			int yPix = y + yOffs;
			if (yPix < 0 || yPix >= height) continue;
			
			for (int x = 0; x < w * scale; x++) {
				int xPix = x + xOffs;
				if (xPix < 0 || xPix >= width) continue;
				
				int src = bitmap.pixels[(x / scale + xo) + (y / scale + yo) * bitmap.width];
				if (src >= 0) {
					pixels[xPix + yPix * width] = src * col;
				}
			}
		}
	}

	public void draw(String string, int x, int y, int col) {
		for (int i = 0; i < string.length(); i++) {
			int ch = chars.indexOf(string.charAt(i));
			if (ch < 0) continue;
			int xx = ch % 42;
			int yy = ch / 42;
			draw(Art.font, x + i * 6, y, (xx * 6), (yy * 8), 5, 8, col * 2);	
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getTotalPixels() {
		return totalPixels;
	}

	public int getPixel(int i) {
		return pixels[i];
	}

	public int getPixel(int x, int y) {
		return getPixel(x + y * width);
	}

	public void setPixel(int i, int value) {
		pixels[i] = value;
	}

	public void setPixel(int x, int y, int value) {
		setPixel(x + y * width, value);
	}

}
