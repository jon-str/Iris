package iris;

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
	
	private int[][] coords;
	private int[][][] coordsTable;
	
	public IrisBlock() {
		coords = new int[4][2];
		this.setBlockShape(Tetrominoe.NoShape);
		
		colorTable = new int[(Tetrominoe.NumTetrominoes).ordinal()];
		initColorTable();
	}
	
	public IrisBlock(IrisBlock block) {
		coords = new int[4][2];
		this.setBlockShape(Tetrominoe.NoShape);
		
		for(int i = 0; i < 4; ++i) {
			this.setX(i, block.getX(i));
			this.setY(i, block.getY(i));
		}
		
		this.blockShape = block.blockShape;
	}
	
	private void initColorTable() {
		colorTable[Tetrominoe.NoShape.ordinal()] = 0xFF000000;
		colorTable[Tetrominoe.ZShape.ordinal()] = 0xFF408020;
		colorTable[Tetrominoe.SShape.ordinal()] = 0xFF80FF40;
		colorTable[Tetrominoe.LineShape.ordinal()] = 0xFF8020FF;
		colorTable[Tetrominoe.TShape.ordinal()] = 0xFF80FFFF;
		colorTable[Tetrominoe.SquareShape.ordinal()] = 0xFF40FF00;
		colorTable[Tetrominoe.LShape.ordinal()] = 0xFF800040;
		colorTable[Tetrominoe.MirroredLShape.ordinal()] = 0xFF40FF40;
	}
	
	public IrisBlock testRot() {
		if(blockShape != Tetrominoe.SquareShape) {
			for(int i = 0; i < 4; ++i) {
				int x = this.getX(i);
				int y = this.getY(i);
				this.setX(i, -y); 
				this.setY(i, -x);
				
				System.out.println("i: " + i + ", x: " + -x + ", y: " + y);
			}
		}
		
		return this;
	}
	
	public IrisBlock testRot(boolean value) {
		if(blockShape != Tetrominoe.SquareShape) {
			for(int i = 0; i < 4; ++i) {
				int x = this.getX(i);
				int y = this.getY(i);
				this.setX(i, y); 
				this.setY(i, x);
				
				System.out.println("i: " + i + ", x: " + -x + ", y: " + y);
			}
		}
		
		return this;
	}
	
	public IrisBlock rotateLeft() {
		if(blockShape == Tetrominoe.SquareShape) {
			return this;
		}
		
		IrisBlock result = new IrisBlock();
		result.blockShape = this.blockShape;
		
		for(int i = 0; i < 4; ++i) {
			result.setX(i, -this.getY(i));
			result.setY(i, this.getX(i));
		}
		
		return result;
	}
	
	public IrisBlock rotateRight() {
		if(blockShape == Tetrominoe.SquareShape) {
			return this;
		}
		
		IrisBlock result = new IrisBlock();
		result.blockShape = this.blockShape;
		
		for(int i = 0; i < 4; ++i) {
			result.setX(i, -(this.getY(i)));
			result.setY(i, -(this.getX(i)));
		}
		
		return result;
	}
	
	public int getX(int index) { return this.coords[index][0]; }
	public int getY(int index) { return this.coords[index][1]; }
	
	public Tetrominoe getBlockShape() { return this.blockShape; }
	
	public void setX(int index, int x) { this.coords[index][0] = x; }
	public void setY(int index, int y) { this.coords[index][1] = y; }
	
	public void setBlockShape(Tetrominoe shape) {
		coordsTable = new int [][][] {
				{ {0, 0}, {0, 0}, {0, 0}, {0, 0} },
				{ {0, -1}, {0, 0}, {-1, 0}, {-1, 1} },
				{ {0, -1}, {0, 0}, {1, 0}, {1, 1} },
				{ {0, -1}, {0, 0}, {0, 1}, {0, 2} },
				{ {-1, 0}, {0, 0}, {1, 0}, {0, 1} },
				{ {0, 0}, {1, 0}, {0, 1}, {1, 1} },
				{ {-1, -1}, {0, -1}, {0, 0}, {0, 1} },
				{ {1, -1}, {0, -1}, {0, 0}, {0, 1} }
				
		};
		
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 2; ++j) {
				coords[i][j] = coordsTable[shape.ordinal()][i][j];
			}
		}
		
		this.blockShape = shape;
	}
	
	public IrisBlock setRandomBlockShape() {
		Random random = new Random();
		int i = Math.abs(random.nextInt()) % 7 + 1;
		Tetrominoe[] values = Tetrominoe.values();
		this.setBlockShape(values[i]);
		
		return this;
	}
	
	public int getMinX() {
		int m = coords[0][0];
		for(int i = 0; i < 4; ++i) {
			m = Math.min(m, coords[i][0]);
		}
		return m;
	}
	
	public int getMaxX() {
		int m = coords[0][0];
		for(int i = 0; i < 4; ++i) {
			m = Math.max(m, coords[i][0]);
		}
		return m;
	}
	
	public int getMinY() {
		int m = coords[0][1];
		for(int i = 0; i < 4; ++i) {
			m = Math.min(m, coords[i][1]);
		}
		return m;
	}
	
	public int getMaxY() {
		int m = coords[0][1];
		for(int i = 0; i < 4; ++i) {
			m = Math.max(m, coords[i][1]);
		}
		return m;
	}
	
	public int getBlockColor() {
		return colorTable[blockShape.ordinal()];
	}
	
	public static int getBlockColor(Tetrominoe shape) {
		return colorTable[shape.ordinal()];
	}
	
}
