package com.github.fishio;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * EnemyFish class. This class contains all methods concerning non-player or
 * enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable, IEatable {


	private static final double FISH_EAT_THRESHOLD = 1.2;
	private static final double DIRECTION_CHANGE_CHANCE = 0.1;

	private Log logger = Log.getLogger();
	
	private double vx;
	private double vy;
	private Image sprite;

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
		logger.log(LogLevel.TRACE, "Created Enemfish: Properties{[position = " + ca.getCenterX() 
				+ ", " + ca.getCenterY() + "],[height = " + ca.getHeight() + "],[width = "
				+ ca.getWidth() + "],[Vx = " + vx + "],[Vy = " + vy + "]}.");
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
		logger.log(LogLevel.TRACE, "EnemyFish collided with wall.");
		kill();
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

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void onCollide(ICollidable other) { }

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (other.getSize() > getSize() * FISH_EAT_THRESHOLD) {
			return true;
		}
		return false;
	}

	@Override
	public void eat() {
		kill();
	}

	@Override
	public double getSize() {
		return getBoundingArea().getSize();
	}

}
