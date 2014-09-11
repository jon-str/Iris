package com.jonstr.iris.game;

public class IrisNextShapeComponent extends GridComponent {

	private IrisMainComponent parent;

	public IrisNextShapeComponent(IrisMainComponent imc, int blockSize, int gridWidth, int gridHeight, int gridLineColor) {
		super(blockSize, gridWidth, gridHeight, gridLineColor);
		parent = imc;
	}

	@Override
	public void render() {
		this.fill(0xFF000000);
		this.drawGridLines();

		for (int i = 0; i < 4; ++i) {
			int x = 2 + parent.getNextShape().getX(i);
			int y = 2 + parent.getNextShape().getY(i);
			
			if (x < 0 || x >= this.gridWidth || y < 0 || y >= this.gridHeight) continue;
		
			this.drawBlock(x, y, IrisBlock.getBlockColor(parent.getNextShape().getBlockShape()));
		}
	}

}
