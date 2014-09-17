package com.jonstr.iris.game;

public class IrisScoreComponent extends GridComponent {

	public IrisScoreComponent(int xAbs, int yAbs) { super(5, 1, 1, 0xFF40FF00, xAbs, yAbs); }

	@Override
	public void render() {
		this.fill(0xFFFFFFFF);
	}

}
