package com.github.fishio.power_ups;

import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * A PowerUp is an entity that, when colliding with a player fish,
 * executes a certain (positive) effect. 
 */
public abstract class PowerUp extends Entity {

	private PlayingField pfield;
	
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
	
}
