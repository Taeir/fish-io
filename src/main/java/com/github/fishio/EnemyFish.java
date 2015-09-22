package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * EnemyFish class. This class contains all methods concerning non-player or
 * enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable {

	private static final double DIRECTION_CHANGE_CHANCE = 0.1;

	private double vx;
	private double vy;
	private Image sprite;
	
	private boolean frozen;
	private boolean selfControlling;

	/**
	 * Main constructor of the enemy fish.
	 * 
	 * @param ca
	 *            ICollisionArea of enemy fish object.
	 * @param sprite
	 *            Sprite of the enemy fish object.
	 * @param startvx
	 *            Starting speed of the enemy fish object in the x direction.
	 * @param startvy
	 *            Starting speed of the enemy fish object in the y direction.
	 */
	public EnemyFish(ICollisionArea ca, Image sprite, double startvx, double startvy) {
		super(ca);
		this.sprite = sprite;
		vx = startvx;
		vy = startvy;
		
		this.selfControlling = true;
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		getBoundingArea().setRotation(this);	//update rotation
		if (vx > 0) {
			drawRotatedImage(gc, sprite, getBoundingArea(), false);
		} else {
			drawRotatedImage(gc, sprite, getBoundingArea(), true);
		}
	}

	@Override
	public Vec2d getSpeedVector() {
		if (frozen) {
			return new Vec2d();
		}
		
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
		
		if (frozen || !selfControlling) {
			return;
		}
		
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
	 * Limits the horizontal (x-directional) speed of the fish to a minimum and
	 * maximum value. These values are retrieved from the LevelBuilder class.
	 */
	public void limitVx() {
		if (vx > 0) {
			vx = Math.max(LevelBuilder.MIN_EFISH_SPEED, Math.min(vx, LevelBuilder.MAX_EFISH_SPEED));
		} else {
			vx = Math.min(-LevelBuilder.MIN_EFISH_SPEED, Math.max(vx, -LevelBuilder.MAX_EFISH_SPEED));
		}
	}
	
	/**
	 * Limits the vertical (y-directional) speed of the fish to a minimum and
	 * maximum value. These values are retrieved from the LevelBuilder class.
	 */
	public void limitVy() {
		if (vy > 0) {
			vy = Math.max(LevelBuilder.MIN_EFISH_SPEED, Math.min(vy, LevelBuilder.MAX_EFISH_SPEED));
		} else {
			vy = Math.min(-LevelBuilder.MIN_EFISH_SPEED, Math.max(vy, -LevelBuilder.MAX_EFISH_SPEED));
		}
	}
	
	/**
	 * Limits the speed of the fish in both horizontal and vertical direction by
	 * calling the limiter methods for each seperate direction.
	 */
	public void limitSpeed() {
		limitVx();
		limitVy();
	}
	
	/**
	 * Sets whether this EnemyFish should stand still or not.
	 * 
	 * @param frozen
	 * 		Whether this EnemyFish should stand still or not.
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	/**
	 * Sets whether this EnemyFish is controlling it's speed vector
	 * all by itself. By disabling this option, other classes are
	 * able to use the setSpeedVector method in order to control this
	 * fish.
	 * 
	 * @param selfControlling
	 * 		Whether this fish can control its own speed vector.
	 */
	public void setSelfControlling(boolean selfControlling) {
		this.selfControlling = selfControlling;
	}
	
	/**
	 * @return
	 * 		Whether this fish is able to adjust its own speed vector.
	 */
	public boolean isSelfControlling() {
		return selfControlling;
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void onCollide(ICollidable other) { }

}
