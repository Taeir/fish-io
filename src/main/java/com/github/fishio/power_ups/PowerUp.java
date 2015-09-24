package com.github.fishio.power_ups;

import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.ICollisionArea;
import com.github.fishio.IMovable;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;

/**
 * A PowerUp is an entity that, when colliding with a player fish,
 * executes a certain (positive) effect. 
 */
public abstract class PowerUp extends Entity implements IMovable {

	private PlayingField pfield;
	
	private Vec2d speedVector;
	
	public static final double DEFAULT_VX = 0;
	public static final double DEFAULT_VY = -2;
	
	/**
	 * Creates a new PowerUp.
	 * 
	 * @param ba
	 * 		The CollisionArea of the Power-Up
	 * @param pfield
	 * 		The PlayingField this PowerUp is located in
	 */
	public PowerUp(ICollisionArea ba, PlayingField pfield) {
		super(ba);
		
		this.pfield = pfield;
		
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
	
	@Override
	public void onCollide(ICollidable other) {
		if (other instanceof PlayerFish) {
			executeEffect((PlayerFish) other);
		}
	}
	
	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(speedVector);
	}

	@Override
	public void setSpeedVector(Vec2d vector) {
		this.speedVector = vector;
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void hitWall() { 
		setDead();
	}

	@Override
	public void preMove() { }
	
}
