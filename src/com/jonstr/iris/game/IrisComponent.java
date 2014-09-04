/* JON-STR */
/* THIS IS PURE SLOP
 * 	- WILL BE CLEANING IN THE MORNING
 */

package com.jonstr.iris.game;

import java.awt.event.KeyEvent;

import com.jonstr.iris.engine.Display;
import com.jonstr.iris.engine.Engine;
import com.jonstr.iris.engine.InputHandler.IrisEvent;
import com.jonstr.iris.game.IrisBlock.Tetrominoe;
import com.jonstr.iris.rendering.Bitmap;

public class IrisComponent extends Bitmap {	
	
	private final static int blockSize = 6;
	private final static int blockSizeSquared = blockSize * blockSize;
	
	private final static int gridWidth = 8;
	private final static int gridHeight = 16;
	private final static int gridSize = gridWidth * gridHeight;
	private final static int gridLineColor = 0xFF0080FF;
	
	private Tetrominoe[] grid;
	private IrisBlock currShape;
	
	private int curX;
	private int curY;
	
	private int numLinesRemoved;
	
	public IrisComponent() {
		super(((blockSizeSquared) * gridWidth) + 1, ((blockSizeSquared) * gridHeight) + 1);
		
		grid = new Tetrominoe[gridWidth * gridHeight];
		currShape = new IrisBlock();
		
		numLinesRemoved = 0;
		
		clearGrid();
		newShape();
	}
	
	public void update(Engine engine, boolean dropFlag, boolean inputDraw) { /*** TODO ***/ //THIS REALLY SUCKS
		//ADD SOME DELTA FOR THE KEY REPEAT TIME ADJUSTMENT, ALLOW
		//RENDERER TO DRAW FIXED FRAME RATE AND REDRAW SHAPE WHEN ROTATED BUT DONT ADVANCE CLOCK
		this.fill(0xFF000000);
		drawGridLines();
		drawFallenShapes();
		drawShape();
		
		//System.out.println(curX + " " + curY);
		System.out.println(currShape.getBlockShape());
		
		if(inputDraw) {
			drawShape();
		}
		if(dropFlag) {
			if(canMove(curX, curY + 1)) {
				curY++;
				drawShape();
			} else {
				drawShape();
				finishFalling(engine);
				drawFallenShapes();
				removeLines();
				newShape();
				return;
			}
		}
	}
	
	private void removeLines() {
		for(int y = 0; y < gridHeight; ++y) {
			if(rowUsed(y)) {
				numLinesRemoved++;
				System.out.println("ROW " + y + " USED!!!");
				shiftRowDown(y);
			}
		}
	}
	
	private void shiftRowDown(int row) {
		Tetrominoe[] tempGrid = new Tetrominoe[gridSize];
		for(int i = 0; i < gridSize; ++i) {
			tempGrid[i] = grid[i];
		}
		
		for(int i = 0; i < gridWidth; ++i) {
			grid[i + (row * gridWidth)] = noShape();
		}
		
		/* I REALLY DO NOT LIKE THIS */
		for(int rw = row; rw > 0; rw--) {
			System.arraycopy(tempGrid, (rw - 1) * gridWidth, grid, rw * gridWidth, gridWidth);		
		}
	}
	
	private Tetrominoe noShape() { return Tetrominoe.NoShape; }
	
	private boolean rowUsed(int row) {
		for(int x = 0; x < gridWidth; x++) {
			if(shapeAt(x, row) == noShape()) {
				return false;
			}
		}
		
		return true;
	}
	
	private void drawShape() {
		for(int i = 0; i < 4; ++i) {
			int x = curX + currShape.getX(i);
			int y = curY + currShape.getY(i);
			if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				continue;
			}
			drawCurrShape(x, y);
		}
	}
	
	private boolean canMove(int newX, int newY) {
		if(currShape.getMaxY() + newY > 15) {
			System.out.println("If bug, this is bad... 1");
			return false;
		}
		
		for(int i = 0; i < 4; ++i) {
			int x = currShape.getX(i) + newX;
			int y = currShape.getY(i) + newY;
			
			if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				System.out.println("If bug, this is bad... 2");
				return false;
			}
			
			if(shapeAt(x, y) != noShape()) {
				return false;
			}
		}
		
		return true;
	}
	
	private void finishFalling(Engine engine) {
		if(!canMove(curX, curY)) {
			System.out.println("Game over!!!");
			System.out.println("Score: " + numLinesRemoved);
			engine.stop();
		}
		for(int i = 0; i < 4; ++i) {
			int x = currShape.getX(i) + curX;
			int y = currShape.getY(i) + curY;
			setShapeAt(currShape.getBlockShape(), x, y);
		}
	}
	
	private void newShape() {
		currShape.setRandomBlockShape();
		curX = gridWidth / 2 - 1;
		curY = currShape.getMaxX();
	}
	
	private void drawCurrShape(int column, int row) {
		drawGridLines();
		
		int xPix = ((column * blockSizeSquared));
		int yPix = ((row * blockSizeSquared) + 1);
		
		for(int y = yPix; y < yPix + blockSizeSquared - 1; ++y) {
			for(int x = xPix; x < xPix + blockSizeSquared; ++x) {
				if(!isGridLine(x, y)) {
					this.setPixel(x, y, IrisBlock.getBlockColor(currShape.getBlockShape()));
				}
			}
		}
	}
	
	private void drawBlock(int shapeX, int shapeY) {
		for(int y = 0; y < blockSizeSquared; ++y) {
			for(int x = 0; x < blockSizeSquared; ++x) {
				
			}
		}
	}
	
	private void drawFallenShapes() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(!isGridLine(x, y)) {
					int xx = x / blockSizeSquared;
					int yy = y / blockSizeSquared;
					
					Tetrominoe shape = shapeAt(xx, yy);
					
					if(shape != noShape()){
						this.setPixel(x, y, IrisBlock.getBlockColor(shape));
					} else {
						this.setPixel(x, y, 0xFF000000);
					}
				}
			}
		}
	}
	
	private Tetrominoe shapeAt(int x, int y) { return grid[(y * gridWidth) + x]; }
	private void setShapeAt(Tetrominoe shape, int x, int y) { grid[(y * gridWidth) + x] = shape; }
	
	/*
	public void update(Engine engine) {
		//test(engine);
	}
	*/
	
	private void clearGrid() { 
		for(int i = 0; i < gridWidth * gridHeight; ++i) {
			grid[i] = noShape();
		}
	}
	
	private void drawGridLines() {
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				if(isGridLine(x, y)) {
					this.setPixel(x, y, gridLineColor);
				}
			}
		}
	}
	
	private boolean isGridLine(int x, int y) {
		if(x % (blockSize * blockSize) == 0 || y % (blockSize * blockSize) == 0) return true;

		return false;
	}

public boolean handleInput(boolean[] keys) {
		IrisEvent lastEvent = Display.input.getLastEvent();
		IrisEvent thisEvent = IrisEvent.NoEvent;
		
		if(keys[KeyEvent.VK_LEFT]) {
			thisEvent = IrisEvent.Left;
			if(canMove(curX - 1, curY)) {
				if(thisEvent != lastEvent) {
					curX--;
				}
			}
			Display.input.setLastEvent(thisEvent);
			return true;
			
		} else if(keys[KeyEvent.VK_RIGHT]) {
			thisEvent = IrisEvent.Right;
			if(canMove(curX + 1, curY)) {
				if(thisEvent != lastEvent) {
					curX++;
				}
			}
			Display.input.setLastEvent(thisEvent);
			return true;
		} else if(keys[KeyEvent.VK_DOWN]) {
			thisEvent = IrisEvent.Down;
			if(canMove(curX, curY + 1)) {
				if(thisEvent != lastEvent) {
					curY++;
				}
			}
			Display.input.setLastEvent(thisEvent);
			return true;
		} else if(keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_UP]) {
			thisEvent = IrisEvent.Rotate;
			if(thisEvent != lastEvent) {
				currShape.testRot(curX, curY);
			}
			
			Display.input.setLastEvent(thisEvent);
			return true;
		}
		
		return false;
	}
}