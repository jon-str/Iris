package iris;

public class Bitmap {
	
	private int width;
	private int height;
	
	private int totalPixels;
	private int pixels[];

	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.totalPixels = this.width * this.height;
		
		pixels = new int[this.totalPixels];
	}
	
	public Bitmap(Bitmap bitmap) {
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.pixels = bitmap.getPixels();
		this.totalPixels = bitmap.getTotalPixels();
	}
	
	public Bitmap initAsSolid(int color) {
		for(int i = 0; i < this.totalPixels; ++i) {
			this.setPixel(i, color);
		}
		
		return this;
	}
	
	public Bitmap initAsWhite() {
		return this.initAsSolid(0xFFFFFFFF);
	}
	
	public Bitmap initAsBlack() {
		return this.initAsSolid(0xFF000000);
	}
	
	public void blit(Bitmap bitmap, int xOffs, int yOffs) {
		for(int y = 0; y < bitmap.getHeight(); ++y) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= this.getHeight()) continue;
				
			for(int x = 0; x < bitmap.getWidth(); ++x) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= this.getWidth()) continue;
				
				int src = bitmap.pixels[x + y * bitmap.getWidth()];
				this.pixels[xPix + yPix * this.getWidth()] = src;
			}
		}
	}
	
	public void fill(int xMin, int yMin, int xMax, int yMax, int color) {
		for(int y = yMin; y < yMax; ++y) {
			for(int x = xMin; x < xMax; ++x) {
				this.pixels[x + y * this.getWidth()] = color;
			}
		}
	}
	
	public void fill(int color) {
		this.fill(0, 0, this.getWidth(), this.getHeight(), color);
	}
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	
	public int[] getPixels() { return this.pixels; }
	public int getTotalPixels() { return this.totalPixels; }
	
	public int getPixel(int i) { return this.pixels[i]; }
	public int getPixel(int x, int y) { return this.getPixel(x + y * this.getWidth()); }
	
	public void setPixel(int i, int value) { this.pixels[i] = value; }
	public void setPixel(int x, int y, int value) { this.setPixel(x + y * this.getWidth(), value); }
}

