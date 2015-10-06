package com.github.fishio.power_ups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.ICollisionArea;
import com.github.fishio.IEatable;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;
import com.github.fishio.behaviours.IBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * A PowerUp is an entity that, when colliding with a player fish,
 * executes a certain (positive) effect. 
 */
public abstract class PowerUp extends Entity implements IBehaviour, IEatable {

	private PlayingField pfield;
	
	private Vec2d speedVector;
	
	public static final double DEFAULT_VX = 0;
	public static final double DEFAULT_VY = -2;
	
	private final Log log = Log.getLogger();
	
	private Image sprite;
	
	/**
	 * Creates a new PowerUp.
	 * 
	 * @param ba
	 * 		The CollisionArea of the Power-Up
	 * @param pfield
	 * 		The PlayingField this PowerUp is located in
	 * @param image
	 * 		The sprite of this PowerUp
	 */
	public PowerUp(ICollisionArea ba, PlayingField pfield, Image image) {
		super(ba);
		
		this.pfield = pfield;
		this.sprite = image;
		
		this.speedVector = new Vec2d(DEFAULT_VX, DEFAULT_VY);
	}
	
	/**
	 * @return
	 * 		The PlayingField this PowerUp is located in.
	 */
	public PlayingField getPField() {
		return pfield;
	}
	
	/**
	 * Executes the effect the power-up should do
	 * when a collision happens with a PlayerFish.
	 * 
	 * @param pfish
	 * 		The PlayerFish this PowerUp collided with
	 */
	public abstract void executeEffect(PlayerFish pfish);
	
	/**
	 * @return
	 * 		A name for this PowerUp.
	 */
	public abstract String getName();
	
	@Override
	public void onCollide(ICollidable other) { 
		executeEffect((PlayerFish) other);
		log.log(LogLevel.DEBUG, "Effect of \"" + getName() + "\" started");
	}
	
	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(speedVector);
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void hitWall() { 
		kill();
	}

	@Override
	public void preMove() { }
	

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (other instanceof PlayerFish) {
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
		return 0; // A PowerUp shouldn't have a "size"
	}
	
	@Override
	public void render(GraphicsContext gc) {
		drawRotatedImage(gc, sprite, getBoundingArea(), false);
	}
	
}
