package com.torpill.game.block;

import java.awt.Color;

public class VoidBlock extends Block {

	protected VoidBlock() {

		super(0, 0);
	}
	
	public Color getColor() {
		
		return new Color(0x00000000, true);
	}
}
