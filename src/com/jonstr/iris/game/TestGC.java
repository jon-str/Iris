package com.jonstr.iris.game;

public class TestGC extends GridComponent {

	public int blockCol = 0xFF000000;
	
	public TestGC(int blockSize, int gridWidth, int gridHeight,int gridLineColor) {
		super(blockSize, gridWidth, gridHeight, gridLineColor);
		
		this.fill(0xFF000000); //background color
		this.drawGridLines();
		
		
	}
	
	@Override
	public void update() {
		blockCol += 255;
		//if(blockCol > 0xFFFFFFFF) blockCol = 0;
		
		for(int row = 0; row < gridHeight; ++row) {
			for(int col = 0; col < this.gridWidth; ++col) {
				if(row % 2 == 0 || col % 2 == 0) {
					this.drawBlock(col, row, blockCol);
				}
			}
		}
	}
	
}
