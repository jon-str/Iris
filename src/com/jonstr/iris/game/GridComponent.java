package com.jonstr.iris.game;

import com.jonstr.iris.rendering.Bitmap;

public abstract class GridComponent extends Bitmap {
	
	protected final int blockSize;
	protected final int blockSizeSquared;
	
	protected final int gridWidth; //8
	protected final int gridHeight; //16
	protected final int gridSize; //gridWidth * gridHeight;
	protected final int gridLineColor; //0xFF0080FF;
	
	protected final int xAbsolute;
	protected final int yAbsolute;
	
	protected int blockDefaultColor;
	
	public GridComponent(int blockSize, int gridWidth, int gridHeight, int gridLineColor, int xAbs, int yAbs) {
		super(((blockSize * blockSize) * gridWidth) + 1, ((blockSize * blockSize) * gridHeight) + 1);
		
		this.blockSize = blockSize;
		this.blockSizeSquared = (blockSize * blockSize);
		
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.gridSize = gridWidth * gridHeight;
		this.gridLineColor = gridLineColor;
		
		this.xAbsolute = xAbs;
		this.yAbsolute = yAbs;
		
		//not arg
		blockDefaultColor = 0xFF00FFFF;
	}
	
	private boolean isGridLine(int x, int y) {
		if(x % (blockSize * blockSize) == 0 || y % (blockSize * blockSize) == 0) return true;

		return false;
	}
	
	public void drawGridLines() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(isGridLine(x, y)) {
					this.setPixel(x, y, gridLineColor);
				}
			}
		}
	}
	
	public void drawBlock(int col, int row, int blockColor) {
		int xPix = ((col * blockSizeSquared));
		int yPix = ((row * blockSizeSquared) + 1);
		
		for(int y = yPix; y < yPix + blockSizeSquared - 1; ++y) {
			for(int x = xPix; x < xPix + blockSizeSquared; ++x) {
				if(!isGridLine(x, y)) {
					this.setPixel(x, y, blockColor);
				}
			}
		}
	}
	
	public abstract void render();
	
	public int getBlockSize() { return blockSize; }
	public int getBlockSizeSquared() { return blockSizeSquared; }
	
	public int getGridWidth() { return gridWidth; }
	public int getGridHeight() { return gridHeight; }
	public int getGridSize() { return gridSize; }
	public int getGridLineColor() { return gridLineColor; }
	
	public int getXAbsolute() { return xAbsolute; }
	public int getYAbsolute() { return yAbsolute; }

}
