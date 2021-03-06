package com.jonstr.iris.game;

import com.jonstr.iris.engine.Engine;
import com.jonstr.iris.rendering.Bitmap;

public class Game {
	
	private boolean dropFlag;
	
	/* VISUAL LAYOUT SCRATCH
		xPadding = 8 + (6 * (6 * 8)) + 8;
		yPadding = 8 + (6 * (6 * 11));
	*/
	
	private IrisMainComponent irisGame;
	private IrisNextShapeComponent irisNextShapeGrid;
	
	private GridComponent[] drawList;
	
	public Game() {
		dropFlag = false;
		irisGame = new IrisMainComponent(6, 8, 16, 0xFFFF00FF, 8, 8);
		irisNextShapeGrid = new IrisNextShapeComponent(irisGame, irisGame.getBlockSize(), 5, 5, 
				                                       irisGame.getGridLineColor(), 304, 404);
			
		drawList = new GridComponent[2];
		drawList[0] = irisGame;
		drawList[1] = irisNextShapeGrid;
	}
	
	public void update(Engine engine, boolean[] keys) {
		irisGame.update(engine, false, irisGame.handleInput(keys));
		irisGame.update(engine, dropFlag, false);
	}
	
	public void render() {
		for(int i = 0; i < drawList.length; ++i) {
			drawList[i].render();
		}
	}
	
	public void setDropFlag(boolean flag) { dropFlag = flag; }
	
	public int getScore() { return irisGame.getNumLinesRemoved(); }
	
	public GridComponent[] getDrawList() { return drawList; }
	public GridComponent getDrawListAt(int i) { return drawList[i]; }
	public Bitmap getBitmapToDraw(int i) { return (Bitmap) drawList[i]; }
}
