package com.jonstr.iris.game;

import com.jonstr.iris.rendering.Bitmap;

public class IrisScoreComponent extends GridComponent {

	public IrisScoreComponent(int xAbs, int yAbs) { super(5, 1, 1, 0xFF40FF00, xAbs, yAbs); }

	@Override
	public void render() {
		//this.prepare();
		//this.fill(0xFF101010);
		this.fill(0xFFFFFFFF);
		this.draw("ABC", 1, 1, 0xFF000000);
	}
	
	public Bitmap getGraw() {
		return this.scaleCopy(2);
	}

}
