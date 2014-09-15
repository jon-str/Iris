package com.jonstr.iris.game;

import java.awt.event.KeyEvent;

import com.jonstr.iris.engine.Engine;
import com.jonstr.iris.engine.InputHandler.IrisEvent;
import com.jonstr.iris.game.IrisBlock.Tetrominoe;

public class IrisMainComponent extends GridComponent {
	private Tetrominoe[] grid;

	private IrisBlock currShape;
	private IrisBlock nextShape;

	private int curX;
	private int curY;

	int numLinesRemoved = 0;

	public IrisMainComponent(int blockSize, int gridWidth, int gridHeight, int gridLineColor, int xAbs, int yAbs) {
		super(blockSize, gridWidth, gridHeight, gridLineColor, xAbs, yAbs);

		grid = new Tetrominoe[this.gridWidth * this.gridHeight];
		currShape = new IrisBlock();
		nextShape = new IrisBlock().setRandomBlockShape();

		numLinesRemoved = 0;

		clearTetrominoeGrid();
		newShape();

		this.fill(0xFF000000); // background color
	}

	@Override
	public void render() {
		this.prepare();

		drawFallenShapes();
		drawCurrShape();
	}

	public void update(Engine engine, boolean dropFlag, boolean inputFlag) {
		if (dropFlag) {
			if (canMove(curX, curY + 1)) {
				curY++;
			} else {
				finishFalling(engine);
				removeLines();
				newShape();
			}
		}
	}

	private boolean canMove(int newX, int newY) {
		if (currShape.getMaxY() + newY >= this.gridHeight) {
			return false;
		}

		for (int i = 0; i < 4; ++i) {
			int x = currShape.getX(i) + newX;
			int y = currShape.getY(i) + newY;

			if (x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				return false;
			}

			if (getShape(x, y) != noShape()) {
				return false;
			}
		}

		return true;
	}

	private void newShape() {
		currShape = new IrisBlock(nextShape);
		curX = 4;
		curY = 0;
		
		nextShape.setRandomBlockShape();
	}

	private void drawCurrShape() {
		for (int i = 0; i < 4; ++i) {
			int x = curX + currShape.getX(i);
			int y = curY + currShape.getY(i);
			if (x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
				continue;
			}
			this.drawBlock(x, y, IrisBlock.getBlockColor(currShape.getBlockShape()));
		}
	}

	private void drawFallenShapes() {
		for (int row = 0; row < this.gridHeight; ++row) {
			for (int col = 0; col < this.gridWidth; ++col) {
				Tetrominoe shape = getShape(col, row);

				if (shape != noShape()) {
					this.drawBlock(col, row, IrisBlock.getBlockColor(shape));
				}
			}
		}
	}

	private void finishFalling(Engine engine) {
		if (!canMove(curX, curY)) {
			System.out.println("Game over!!!");
			System.out.println("Score: " + numLinesRemoved);
			engine.stop();
		}

		for (int i = 0; i < 4; ++i) {
			int x = currShape.getX(i) + curX;
			int y = currShape.getY(i) + curY;
			setShape(currShape.getBlockShape(), x, y);
		}
	}

	private Tetrominoe noShape() { return Tetrominoe.NoShape; }
	private Tetrominoe getShape(int col, int row) { return grid[col + row * gridWidth]; }

	private void setShape(Tetrominoe shape, int col, int row) { grid[col + row * gridWidth] = shape; }
	
	public IrisBlock getNextShape() { return nextShape; }
	
	private boolean lineUsed(int row) {
		for(int col = 0; col < gridWidth; col++) {
			if(getShape(col, row) == noShape()) {
				return false;
			}
		}
		
		return true;
	}
	
	private void shiftlineDown(int row) {
		Tetrominoe[] tempGrid = new Tetrominoe[gridSize];
		for(int i = 0; i < gridSize; ++i) {
			tempGrid[i] = grid[i];
		}
		
		for(int col = 0; col < this.gridWidth; ++col) {
			grid[col + (row * this.gridWidth)] = noShape();
		}
		
		/* I REALLY DO NOT LIKE THIS */
		for(int rw = row; rw > 0; rw--) {
			System.arraycopy(tempGrid, (rw - 1) * this.gridWidth, grid, rw * this.gridWidth, this.gridWidth);		
		}
	}
	
	private void removeLines() {
		for(int y = 0; y < this.gridHeight; ++y) {
			if(lineUsed(y)) {
				numLinesRemoved++;
				System.out.println("SCORE: " + numLinesRemoved);
				shiftlineDown(y);
			}
		}
	}

	private void clearTetrominoeGrid() {
		for (int i = 0; i < this.gridWidth * gridHeight; ++i) {
			grid[i] = noShape();
		}
	}
	
	public boolean handleInput(boolean[] keys) {
		IrisEvent lastEvent = Engine.input.getLastEvent();
		IrisEvent thisEvent = IrisEvent.NoEvent;

		if (keys[KeyEvent.VK_LEFT]) {
			thisEvent = IrisEvent.Left;
			if (canMove(curX - 1, curY)) {
				if (thisEvent != lastEvent) {
					curX--;
				}
			}
			Engine.input.setLastEvent(thisEvent);
			return true;

		} else if (keys[KeyEvent.VK_RIGHT]) {
			thisEvent = IrisEvent.Right;
			if (canMove(curX + 1, curY)) {
				if (thisEvent != lastEvent) {
					curX++;
				}
			}
			Engine.input.setLastEvent(thisEvent);
			return true;

		} else if (keys[KeyEvent.VK_DOWN]) {
			thisEvent = IrisEvent.Down;
			if (canMove(curX, curY + 1)) {
				if (thisEvent != lastEvent) {
					curY++;
				}
			}

			Engine.input.setLastEvent(thisEvent);
			return true;

		} else if (keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_UP]) {
			thisEvent = IrisEvent.Rotate;
			if (thisEvent != lastEvent) {
				currShape.testRot(curX, curY);
			}

			Engine.input.setLastEvent(thisEvent);
			return true;
		}

		return false;
	}
}
