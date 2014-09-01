/* JON-STR */
/* THIS IS PURE SLOP
 * 	- WILL BE CLEANING IN THE MORNING
 */

package com.jonstr.iris.game;

import java.awt.event.KeyEvent;

import com.jonstr.iris.engine.Engine;
import com.jonstr.iris.game.IrisBlock.Tetrominoe;
import com.jonstr.iris.rendering.Bitmap;

public class IrisTesting extends Bitmap {	
	
	private int blockSize;
	private int blockSizeSquared;
	
	private boolean lR = true;
	
	/* TESTING */
	final static int gridWidth = 8;
	final static int gridHeight = 16;
	
	private float time;
	
	boolean isFinishedFalling = false;
	
	int curX = 0;
	int curY = 2;
	
	int numLinesRemoved = 0; //score
	IrisBlock curPeice;
	Tetrominoe[] grid;
	
	public IrisTesting() {
		super(((6 * 6) * gridWidth) + 1, ((6 * 6) * gridHeight) + 1);
		this.blockSize = 6;
		this.blockSizeSquared = blockSize * blockSize;
		
		time = System.nanoTime();
		
		curPeice = new IrisBlock();
		grid = new Tetrominoe[gridWidth * gridHeight];
		
		clearGrid();
		newShape();
		curY = 2;
	}
	
	public void test(Engine engine) {
		this.fill(0xFF000000);
		drawGrid();
		drawShapes();
		
		if(canMove(curX, curY + 1)) {
			curY++;
			drawShape();
		} else {
			drawShape();
			finish(engine);
			removeLines();
			drawShapes();
			newShape();
			return;
		}
		
	}
	
	public void removeLines() {
		for(int y = 0; y < gridHeight; ++y) {
			if(rowUsed(y)) {
				numLinesRemoved++;
				System.out.println("ROW " + y + " USED!!!");
				shiftRowDown(y);
			}
		}
	}
	
	private int gridSize = gridWidth * gridHeight;
	
	public void shiftRowDown(int row) {
		Tetrominoe[] tempGrid = new Tetrominoe[gridSize];
		for(int i = 0; i < gridSize; ++i) {
			tempGrid[i] = grid[i];
		}
		
		for(int i = 0; i < gridWidth; ++i) {
			grid[i + (row * gridWidth)] = Tetrominoe.NoShape;
		}
		
		int len = 0;
		for(int rw = row; rw > 0; rw--) {
			System.arraycopy(tempGrid, (rw - 1) * gridWidth, grid, rw * gridWidth, gridWidth);
			len += gridWidth;
		
		}
	}
	
	public Tetrominoe noShape() { return Tetrominoe.NoShape; }
	
	public boolean rowUsed(int row) {
		for(int x = 0; x < gridWidth; x++) {
			if(shapeAt(x, row) == Tetrominoe.NoShape) {
				return false;
			}
		}
		
		return true;
	}
	
	public void clearRow(Tetrominoe[] grid, int row) {
		for(int i = 0; i < gridWidth; ++i) {
			grid[(row * gridWidth)] = Tetrominoe.NoShape;
		}
	}
	
	public void drawShape() {
		for(int i = 0; i < 4; ++i) {
			int x = curX + curPeice.getX(i);
			int y = curY + curPeice.getY(i);
			if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				continue;
			}
			drawBlock(x, y);
		}
	}
	
	public boolean canMove(int newX, int newY) {
		if(curPeice.getMaxY() + newY > 15) {
			return false;
		}
		
		for(int i = 0; i < 4; ++i) {
			int x = curPeice.getX(i) + newX;
			int y = curPeice.getY(i) + newY;
			
			if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				return false;
			}
			
			if(shapeAt(x, y) != Tetrominoe.NoShape) {
				return false;
			}
		}
		
		return true;
	}
	
	public void finish(Engine engine) {
		if(!canMove(curX, curY)) {
			System.out.println("Game over!!!");
			System.out.println("Score: " + numLinesRemoved);
			engine.stop();
		}
		for(int i = 0; i < 4; ++i) {
			int x = curPeice.getX(i) + curX;
			int y = curPeice.getY(i) + curY;
			setShapeAt(curPeice.getBlockShape(), x, y);
		}
	}
	
	public void drawBlock(int column, int row) {
		drawGrid();
		//int column = 1;
		//int row = 0;
		
		int xPix = ((column * blockSizeSquared));
		int yPix = ((row * blockSizeSquared) + 1);
		
		for(int y = yPix; y < yPix + 36 - 1; ++y) {
			for(int x = xPix; x < xPix + 36; ++x) {
				if(!isGridLine(x, y)) {
					this.setPixel(x, y, IrisBlock.getBlockColor(curPeice.getBlockShape()));
				}
			}
		}
	}
	
	public void paintGrid() {
		drawGrid();
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
		curY = 0;
		isFinishedFalling = false;
	}
	
	public Tetrominoe shapeAt(int x, int y) { return grid[(y * gridWidth) + x]; }
	public void setShapeAt(Tetrominoe shape, int x, int y) { grid[(y * gridWidth) + x] = shape; }
	
	public void update(Engine engine) {
		//paintGrid();
		double prevTime = time;
		double currTime = System.nanoTime() - prevTime;
		
		test(engine);
	}
	
	public void clearGrid() { 
		for(int i = 0; i < gridWidth * gridHeight; ++i) {
			grid[i] = Tetrominoe.NoShape;
		}
	}
	/*END TESTING SHIT*/
	
	public void drawGrid() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(isGridLine(x, y)) {
					this.setPixel(x, y, 0xFF0080FF);
				}
			}
		}
	}
	public boolean isGridLine(int x, int y) {
		if(x % (blockSize * blockSize) == 0 || y % (blockSize * blockSize) == 0) return true;

		return false;
	}

	public boolean handleInput(boolean[] keys) {
		if(keys[KeyEvent.VK_LEFT]) {
			if(canMove(curX - 1, curY)) {
				curX--;
			}
			return true;
			
		} else if(keys[KeyEvent.VK_RIGHT]) {
			if(canMove(curX + 1, curY)) {
				curX++;
			}
			return true;
			
		} else if(keys[KeyEvent.VK_DOWN]) {
			if(canMove(curX, curY + 1)) {
				curY++;
				return true;
			}
			return false;
			
		} else if(keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_UP]) {
			
			if(lR) {
				curPeice.testRot(true);
				lR = false;
			} else {
				curPeice.testRot();
				lR = true;
			}
			
			//curPeice.testRot();
			
			return true;
			
		} else if(keys[KeyEvent.VK_CONTROL]) {
			return true;
		}
		
		return false;
	}

}