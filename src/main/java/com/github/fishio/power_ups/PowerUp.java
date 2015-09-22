package com.github.fishio.power_ups;

import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.ICollisionArea;

/**
 * A Power-Up is an entity that, when colliding with a player fish,
 * executes a certain (positive) effect. 
 */
public abstract class PowerUp extends Entity {

	/**
	 * Creates a new Power-Up.
	 * 
	 * @param ba
	 * 		The CollisionArea of the Power-Up
	 */
	public PowerUp(ICollisionArea ba) {
		super(ba);
	}
	
	@Override
	public abstract void onCollide(ICollidable other);
	
}
