package iris;

import iris.IrisBlock.Tetrominoe;

import java.awt.event.KeyEvent;


public class IrisGrid extends Bitmap {

	private static final int COLUMNS = 8; // width
	private static final int ROWS = 16; // height
	
	private int blockSize;
	private int blockSizeSquared;
	private boolean[] blocks;
	
	boolean blockFinishedFalling;
	boolean blockStartedFalling;
	
	boolean lR = true;
	
	
	
	private IrisBlock currentBlock;
	private IrisBlock oldBlock;
	
	/* TESTING */
	final static int gridWidth = 8;
	final static int gridHeight = 16;
	
	boolean isFinishedFalling = false;
	
	int curX = 0;
	int curY = 0;
	
	int oldX = -1;
	int oldY = -1;
	
	int numLinesRemoved = 0; //score
	IrisBlock curPeice;
	Tetrominoe[] grid;
	
	public IrisGrid(boolean value) {
		super(((6 * 6) * gridWidth) + 1, ((6 * 6) * gridHeight) + 1);
		this.blockSize = 6;
		this.blockSizeSquared = blockSize * blockSize;
		
		curPeice = new IrisBlock();
		grid = new Tetrominoe[gridWidth * gridHeight];
		
		clearGrid();
		
		newShape();
	}
	
	public void paintGrid() {
		drawGrid();
			
		if(curPeice.getBlockShape() != Tetrominoe.NoShape) {
			for(int i = 0; i < 4; ++i) {
				int x = curX + curPeice.getX(i);
				int y = curY + curPeice.getY(i);
				if(y >= 0 && y < 15 & x >= 0 && x < 8) {
					setShapeAt(curPeice.getBlockShape(), x, y);
				}
				else
				{
					isFinishedFalling = true;
					newShape();
				}
			}		
		}

		drawShapes();
		if(!isFinishedFalling) {
			dropDownOneLine();
		}
	}
	
	public void dropDownOneLine() {
		for(int i = 0; i < 4; ++i) {
			int x = curX + curPeice.getX(i);
			int y = curY + curPeice.getY(i);
			if(y < 16) {
				setShapeAt(Tetrominoe.NoShape, x, y);
			}
		}
		
		curY++;
	}
	
	public void drawShapes() {
			
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(!isGridLine(x, y)) {
					int xx = x / blockSizeSquared;
					int yy = y / blockSizeSquared;
					
					Tetrominoe shape = shapeAt(xx, yy);
					
					if(shape != Tetrominoe.NoShape){
						this.setPixel(x, y, IrisBlock.getBlockColor(shape));
					} else {
						this.setPixel(x, y, 0xFF000000);
					}
				}
			}
		}
	}
	
	public void newShape() {
		curPeice.setRandomBlockShape();
		curX = gridWidth / 2 - 1;
		curY = 0 + curPeice.getMaxY();
		isFinishedFalling = false;
	}
	
	public Tetrominoe shapeAt(int x, int y) { return grid[(y * gridWidth) + x]; }
	public void setShapeAt(Tetrominoe shape, int x, int y) { grid[(y * gridWidth) + x] = shape; }
	
	public void update() {
		paintGrid();
	}
	
	public void clearGrid() { 
		for(int i = 0; i < gridWidth * gridHeight; ++i) {
			grid[i] = Tetrominoe.NoShape;
		}
	}
	/*END TESTING SHIT*/
	
	public IrisGrid(int blockSize) {
		super(((blockSize * blockSize) * COLUMNS) + 1, ((blockSize * blockSize) * ROWS) + 1);
		this.blockSize = blockSize;
		this.blockSizeSquared = blockSize * blockSize;
		this.blocks = new boolean[COLUMNS * ROWS];
		for(int i = 0; i < COLUMNS * ROWS; ++i) {
			this.blocks[i] = false;
		}
		
		currentBlock = new IrisBlock();
		currentBlock.setRandomBlockShape();
		for(int i = 0; i < 4; ++i) {
			currentBlock.setX(i, currentBlock.getX(i) + 3);
		}
		
		this.initAsBlack();
		this.drawGrid();
	}
	
	public void drawGrid() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(isGridLine(x, y)) {
					this.setPixel(x, y, 0xFF0080FF);
				}
			}
		}
	}
	
	public void drawBlocks() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(!isGridLine(x, y)) {
					int xx = x / blockSizeSquared;
					int yy = y / blockSizeSquared;
					
					if(isBlock(xx, yy)){
						this.setPixel(x, y, 0xFF80FF40);
					} else if(!isBlock(xx, yy)){
						this.setPixel(x, y, 0xFF000000);
					}
				}
			}
		}
	}
	
	public boolean isBlock(int x, int y) {
		return this.blocks[x + y * COLUMNS];
	}
	
	public void setBlock(int x, int y, boolean value) {
		this.blocks[x + y * COLUMNS] = value;
	}
	
	public boolean isGridLine(int x, int y) {
		if(x % (blockSize * blockSize) == 0 || y % (blockSize * blockSize) == 0) return true;

		return false;
	}
	
	int[] ox = new int[4];
	int[] oy = new int[4];
	boolean firstDraw = true;
	
	public void update(Engine engine) {
		if(oldBlock != null && !blockFinishedFalling) {
			for(int i = 0; i < 4; ++i) {
				int x = oldBlock.getX(i);
				int y = oldBlock.getY(i);
				if(x >= 0 && x < COLUMNS && y >= 0 && y < ROWS) {
					this.setBlock(x, y, false);
				}
			}
		}
		
		for(int i = 0; i < 4; ++i) {
			int x = currentBlock.getX(i);
			int y = currentBlock.getY(i);
			if(x >= 0 && x < COLUMNS && y >= 0 && y < ROWS) {
				this.setBlock(x, y, true);
			}
		}
		
		for(int i = 0; i < 16; ++i) {
			if(rowUsed(i)) {
				clearRow(i);
				shiftBlocksDown();
			}
		}
		
		drawBlocks();
		
		oldBlock = new IrisBlock(currentBlock);
		
		if(currentBlock.getMaxY() < 15) {
			//System.out.println(currentBlock.getMaxY());
			for(int i = 0; i < 4; ++i) {
				currentBlock.setY(i, currentBlock.getY(i) + 1);
			}
			blockFinishedFalling = false;
		} else {
			currentBlock = new IrisBlock().setRandomBlockShape();
			for(int i = 0; i < 4; i++) {
				//currentBlock.setX(i, currentBlock.getX(i) - 1);
			}
			blockFinishedFalling = true;
		}
	}
	
	public void clearBlocks() {
		for(int i = 0; i < COLUMNS * ROWS; ++i) {
			this.blocks[i] = false;
		}
	}
	
	public void clearBlocks(boolean[] blocks) {
		for(int i = 0; i < COLUMNS * ROWS; ++i) {
			blocks[i] = false;
		}
	}
	
	public void clearRow(int y) {
		for(int i = 0; i < COLUMNS; ++i) {
			this.blocks[i + y * COLUMNS] = false;
		}
	}
	
	public void clearRow(boolean[] blocks, int y) {
		for(int i = 0; i < COLUMNS; ++i) {
			blocks[i + y * COLUMNS] = false;
		}
	}
	
	public void clearLastRow() {
		clearRow(15);
	}
	
	public boolean rowUsed(int y) {
		for(int i = 0; i < COLUMNS; i++) {
			if(this.blocks[i + y * COLUMNS] == false) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean lastRowUsed() {
		return rowUsed(15);
	}
	
	public void shiftBlocksDown() {
		boolean[] tempBlocks = new boolean[COLUMNS * ROWS];
		for(int y = ROWS - 1; y >= 0; --y) {
			for(int x = COLUMNS - 1; x >= 0; --x) {
				
				if(y + 1 < 16) {
					tempBlocks[x + (y + 1) * COLUMNS] = blocks[x + y * COLUMNS];
				}
			}
		}
		clearRow(tempBlocks, 1);
		clearBlocks();
		System.arraycopy(tempBlocks, 0, blocks, 0, COLUMNS * ROWS);
	}
	
	public boolean allBlocksUsed() {
		for(int i = 0; i < COLUMNS * ROWS; ++i) {
			if(this.blocks[i] == false) {
				return false;
			}
		}
		
		return true;
	}

	public boolean handleInput(boolean[] keys) {
		if(keys[KeyEvent.VK_LEFT]) {
			//System.out.println("LEFT");
			for(int i = 0; i < 4; i++) {
				currentBlock.setX(i, currentBlock.getX(i) - 1);
			}
			return true;
		} else if(keys[KeyEvent.VK_RIGHT]) {
			//System.out.println("RIGHT");
			for(int i = 0; i < 4; i++) {
				currentBlock.setX(i, currentBlock.getX(i) + 1);
			}
			return true;
		} else if(keys[KeyEvent.VK_DOWN]) {
			if(lR) {
				currentBlock.testRot(true);
				lR = false;
			} else {
				currentBlock.testRot();
				lR = true;
			}
			return true;
		} else if(keys[KeyEvent.VK_SPACE]) {

			return true;
		}
		
		return false;
	}
	
	public boolean tryMove() { return true; }

	public boolean handleInput(boolean value, boolean[] keys) {
		if(keys[KeyEvent.VK_LEFT]) {
			//System.out.println("LEFT");
			for(int i = 0; i < 4; i++) {
				curPeice.setX(i, curPeice.getX(i) - 1);
			}
			return true;
		} else if(keys[KeyEvent.VK_RIGHT]) {
			//System.out.println("RIGHT");
			for(int i = 0; i < 4; i++) {
				curPeice.setX(i, curPeice.getX(i) + 1);
			}
			return true;
		} else if(keys[KeyEvent.VK_DOWN]) {
			if(lR) {
				curPeice.testRot(true);
				lR = false;
			} else {
				curPeice.testRot();
				lR = true;
			}
			return true;
		} else if(keys[KeyEvent.VK_SPACE]) {
	
			return true;
		}
		
		return false;
	}

}



