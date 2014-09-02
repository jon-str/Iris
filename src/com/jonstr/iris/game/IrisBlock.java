package com.jonstr.iris.game;

import java.util.Random;

public class IrisBlock {

	enum Tetrominoe {
			NoShape,
			ZShape,
			SShape,
			LineShape,
			TShape,
			SquareShape,
			LShape,
			MirroredLShape,
			
			NumTetrominoes
	};
	
	private Tetrominoe blockShape;
	private static int[] colorTable;
	
	private int[][] blockCoords;
	private int[][][] blockCoordsTable;
	
	public IrisBlock() {
		initBlockCoordsTable();
		this.setBlockShape(Tetrominoe.NoShape);
		
		initColorTable();
		
	}
	
	public IrisBlock(IrisBlock block) {
		initBlockCoordsTable();
		this.setBlockShape(block.getBlockShape());
		
		for(int i = 0; i < 4; ++i) {
			this.setX(i, block.getX(i));
			this.setY(i, block.getY(i));
		}
		
		initColorTable();
	}
	
	public IrisBlock copy(IrisBlock block) {
		this.setBlockShape(block.getBlockShape());
		
		for(int i = 0; i < 4; ++i) {
			this.setX(i, block.getX(i));
			this.setY(i, block.getY(i));
		}
		
		return this;
	}
	
	private void initBlockCoordsTable() {
		blockCoords = new int[4][2];
		
		blockCoordsTable = new int [][][] {
				{ {0, 0}, {0, 0}, {0, 0}, {0, 0} },
				{ {0, -1}, {0, 0}, {-1, 0}, {-1, 1} },
				{ {0, -1}, {0, 0}, {1, 0}, {1, 1} },
				{ {0, -1}, {0, 0}, {0, 1}, {0, 2} },
				{ {-1, 0}, {0, 0}, {1, 0}, {0, 1} },
				{ {0, 0}, {1, 0}, {0, 1}, {1, 1} },
				{ {-1, -1}, {0, -1}, {0, 0}, {0, 1} },
				{ {1, -1}, {0, -1}, {0, 0}, {0, 1} }
				
		};
	}
	
	private void initColorTable() {
		colorTable = new int[(Tetrominoe.NumTetrominoes).ordinal()];
		
		colorTable[Tetrominoe.NoShape.ordinal()] = 0xFF000000;
		colorTable[Tetrominoe.ZShape.ordinal()] = 0xFF408020;
		colorTable[Tetrominoe.SShape.ordinal()] = 0xFF80FF40;
		colorTable[Tetrominoe.LineShape.ordinal()] = 0xFF8020FF;
		colorTable[Tetrominoe.TShape.ordinal()] = 0xFF80FFFF;
		colorTable[Tetrominoe.SquareShape.ordinal()] = 0xFF40FF00;
		colorTable[Tetrominoe.LShape.ordinal()] = 0xFF800040;
		colorTable[Tetrominoe.MirroredLShape.ordinal()] = 0xFF40FF40;
		
		colorTable[Tetrominoe.MirroredLShape.ordinal()] = colorTable[Tetrominoe.NoShape.ordinal()];
	}
	
	public IrisBlock testRot(int curX, int curY) {
		
		if(curX == 1 || curX == 7 || curY == 14 || curY == 1) return this;
		
		IrisBlock tempBlock = new IrisBlock(this);
		
		if(blockShape != Tetrominoe.SquareShape) {
			for(int i = 0; i < 4; ++i) {
				int x = tempBlock.getX(i);
				int y = tempBlock.getY(i);
				
				int yAsX = (-y) + curX;
				int xAsY = (x) + curY;
				
				if(yAsX < 0 || xAsY < 0 || yAsX > 7 || xAsY > 15) return this;
				
				/*** TODO ***/ //ADD COUNTER ROTATION
				tempBlock.setX(i, -y); 
				tempBlock.setY(i, x);
			}
		}
		
		this.copy(tempBlock);
		
		return this;
	}
	
	public int getX(int index) { return this.blockCoords[index][0]; }
	public int getY(int index) { return this.blockCoords[index][1]; }
	
	public Tetrominoe getBlockShape() { return this.blockShape; }
	
	public static int getBlockColor(Tetrominoe shape) {	return colorTable[shape.ordinal()]; }
	
	public void setX(int index, int x) { this.blockCoords[index][0] = x; }
	public void setY(int index, int y) { this.blockCoords[index][1] = y; }
	
	public int getMinX() {
		int min = blockCoords[0][0];
		for(int i = 0; i < 4; ++i) {
			min = Math.min(min, blockCoords[i][0]);
		}
		return min;
	}
	
	public int getMaxX() {
		int max = blockCoords[0][0];
		for(int i = 0; i < 4; ++i) {
			max = Math.max(max, blockCoords[i][0]);
		}
		return max;
	}
	
	public int getMinY() {
		int min = blockCoords[0][1];
		for(int i = 0; i < 4; ++i) {
			min = Math.min(min, blockCoords[i][1]);
		}
		return min;
	}
	
	public int getMaxY() {
		int max = blockCoords[0][1];
		for(int i = 0; i < 4; ++i) {
			max = Math.max(max, blockCoords[i][1]);
		}
		return max;
	}
	
	public void setBlockShape(Tetrominoe shape) {
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 2; ++j) {
				blockCoords[i][j] = blockCoordsTable[shape.ordinal()][i][j];
			}
		}
		
		this.blockShape = shape;
	}
	
	public void setBlockShape(IrisBlock block) {
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 2; ++j) {
				blockCoords[i][j] = block.blockCoords[i][j];
			}
		}
		
		this.blockShape = block.getBlockShape();
	}
	
	public IrisBlock setRandomBlockShape() {
		Random random = new Random();
		int i = Math.abs(random.nextInt()) % 7 + 1;
		Tetrominoe[] values = Tetrominoe.values();
		this.setBlockShape(values[i]);
		
		return this;
	}
	
}
