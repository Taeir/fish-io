package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable {
	
	private static final double DIRECTION_CHANGE_CHANCE = 0.1;
	
	private double vx;
	private double vy;
	private Image sprite;

	/**
	 * Main constructor of enemy fish.
	 * @param b Bounding box of enemy fish.
	 * @param sprite sprite of the enemy fish
	 * @param startvx starting speed in x direction
	 * @param startvy starting speed in y direction
	 */
	public EnemyFish(BoundingBox b, Image sprite, double startvx, double startvy) {
		super(b);
		this.sprite = sprite;
		vx = startvx;
		vy = startvy;
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		if (vx >= 0) {
			gc.drawImage(sprite, getX(), getY(), getWidth(), getHeight());
		} else {
			gc.drawImage(sprite, getX() + getWidth(), getY(), -getWidth(), getHeight());
		}		
	}

	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(vx, vy);
	}

	@Override
	public void setSpeedVector(Vec2d vector) {
		vx = vector.x;
		vy = vector.y;
	}

	/**
	 * Enemy fish should die if they hit the wall from the inside.
	 */
	@Override
	public void hitWall() {
			setDead();
	}

	/** 
	 * Enemy fish sometimes change their movement speed.
	 * Only change one of their movement directions so the change looks more realistic.
	 */
	@Override
	public void preMove() {
		if (Math.random() < DIRECTION_CHANGE_CHANCE) {
			//Only change one direction
			if (Math.random() <= 0.5) {
				vy = vy + vy * (Math.random() - 0.5);
			} else {
				vx = vx + vx * (Math.random() - 0.5);
			}
			limitSpeed();
		}

	}

	/**
	 * Limits the speed of the fish to a minimum and maximum value.
	 */
	private void limitSpeed() {
		if (vx > 0) {
			vx = Math.max(LevelBuilder.MIN_EFISH_SPEED, Math.min(vx, LevelBuilder.MAX_EFISH_SPEED));
		} else {
			vx = Math.min(-LevelBuilder.MIN_EFISH_SPEED, Math.max(vx, -LevelBuilder.MAX_EFISH_SPEED));
		}

		if (vy > 0) {
			vy = Math.max(LevelBuilder.MIN_EFISH_SPEED, Math.min(vy, LevelBuilder.MAX_EFISH_SPEED));
		} else {
			vy = Math.min(-LevelBuilder.MIN_EFISH_SPEED, Math.max(vy, -LevelBuilder.MAX_EFISH_SPEED));
		}		
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void onCollide(ICollidable other) { }

}
