package com.github.fishio.power_ups;

import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * PuSuperSpeed is a PowerUp where the effect is
 * drastically increasing the acceleration and speed
 * of the PlayerFish.
 */
public class PuSuperSpeed extends DurationPowerUp {

	private static final int DURATION = 10;
	public static final double ACCELERATION_FACTOR = 2.5;
	public static final double MAX_SPEED_FACTOR = 3;
	
	/**
	 * Creates a new PowerUp of the SuperSpeed type.
	 * 
	 * @param ba
	 * 		The CollisonArea of the PowerUp.
	 * @param pfield
	 * 		The PlayingField in which this PowerUp is located.
	 */
	public PuSuperSpeed(ICollisionArea ba, PlayingField pfield) {
		super(ba, pfield);
	}

	@Override
	public int getDuration() {
		return DURATION;
	}

	/** 
	 * Multiplied the acceleration and the maxSpeed of the
	 * given PlayerFish by a certain factor.
	 * 
	 * @param pf
	 * 		The PlayerFish this PowerUp collides with.
	 */
	@Override
	public void startEffect() {
		PlayerFish pf = super.getTarget();
		
		pf.setAcceleration(pf.getAcceleration() * ACCELERATION_FACTOR);
		pf.setMaxSpeed(pf.getMaxSpeed() * MAX_SPEED_FACTOR);
	}

	@Override
	public void preTickEffect() { }

	@Override
	public void postTickEffect() { }

	/** 
	 * Restores the acceleration and the MaxSpeed of
	 * the PlayerFish we just drugged.
	 */
	@Override
	public void endEffect() {
		PlayerFish pf = super.getTarget();
		
		pf.setAcceleration(pf.getAcceleration() / ACCELERATION_FACTOR);
		pf.setMaxSpeed(pf.getMaxSpeed() / MAX_SPEED_FACTOR);
	}

}
