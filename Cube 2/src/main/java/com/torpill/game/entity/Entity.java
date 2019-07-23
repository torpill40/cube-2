package com.torpill.game.entity;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import com.torpill.game.Game;
import com.torpill.game.block.Block;
import com.torpill.game.block.damager.DamagerBlock;
import com.torpill.game.level.Level;

public abstract class Entity {

	protected Entity(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public void addTo(Game game) {

		this.game = game;
		this.game.register(this);
	}

	public void addTo(Level level) {

		this.level = level;
		this.level.register(this);
	}

	public void update() {

		if (this.dead && this.deathtick == 0) {

			return;
		}

		ArrayList<Block> blocks = this.game.getBlocks();
		boolean flag = true;
		boolean flag1 = false;
		int i1;
		byte b1;
		byte col;

		for (i1 = 0; i1 < Math.abs(this.motionX); i1++) {

			this.x += this.motionX / Math.abs(this.motionX);

			for (Block block : blocks) {

				col = block.collide(this);

				if (block instanceof DamagerBlock && col != 0) {

					this.dead = true;
				}

				if (block.isDecoration() || block.isFluid()) {

					continue;
				}

				for (b1 = 0; b1 < colsides.length; b1++) {

					colsides[b1] = (col & (byte) (Math.pow(2, b1))) == (byte) (Math.pow(2, b1));

					if (colsides[b1] || this.x < 0) {

						this.x -= this.motionX / Math.abs(this.motionX);
						this.motionX = 0;

						break;
					}
				}

				if (block.collideX(this) != 0) {

					flag = false;
				}
			}
		}

		for (i1 = 0; i1 < Math.abs(this.motionY); i1++) {

			this.y += this.motionY / Math.abs(this.motionY);

			for (Block block : blocks) {

				col = block.collide(this);

				if (block instanceof DamagerBlock && col != 0) {

					this.dead = true;
				}

				if (block.isDecoration() || block.isFluid()) {

					continue;
				}

				for (b1 = 0; b1 < colsides.length; b1++) {

					colsides[b1] = (col & (byte) (Math.pow(2, b1))) == (byte) (Math.pow(2, b1));

					if (colsides[b1] && flag) {

						this.y -= this.motionY / Math.abs(this.motionY);
						this.motionY = 0;
						flag1 = true;

						break;
					}
				}

				if (block.collideY(this) != 0) {

					flag = false;

// this.y -= this.motionY / Math.abs(this.motionY);
					this.motionY = 0;

					break;
				}

				if (flag1) {

					break;
				}
			}
		}

		if (this.y < 0) {

			this.y = 0;
			this.motionY = 0;

			this.onGround = true;

		} else if (this.colsides[0] && flag) {

			this.motionY = 0;

			this.onGround = true;

		} else {

			this.onGround = false;
		}

		if (this.dead) {

			this.deathtick--;
		}

		this.applyGravity();
	}

	public void applyGravity() {

		if (!this.onGround) {

			this.motionY--;
		}
	}

	public int getX() {

		return this.x;
	}

	public int getY() {

		return this.y;
	}

	public int getWidth() {

		return this.width;
	}

	public int getHeight() {

		return this.height;
	}

	public int getXonScreen() {

		return this.x * this.game.getWindow().getUnit();
	}

	public int getYonScreen() {

		return this.game.getWindow().getGlassPaneHeigth() - this.y * this.game.getWindow().getUnit() - this.game.getWindow().getUnit() * this.height;
	}

	public Color getColor() {

		return Color.BLACK;
	}

	public void setAlive() {

		this.dead = false;
		this.deathtick = 5;
	}

	public boolean isAlive() {

		return !this.dead;
	}

	public boolean isDisappear() {

		return this.dead && this.deathtick == 0;
	}

	public abstract Image getImage();

	protected Game game;
	protected Level level;
	protected int x, y, width = 1, height = 1;
	protected int motionX, motionY;
	protected boolean onGround = true;
	public boolean colsides[] = new boolean[4];
	protected boolean dead;
	private int deathtick = 5;
}
