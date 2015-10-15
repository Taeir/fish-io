package com.github.fishio.power_ups;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.KeyListenerBehaviour;

import javafx.scene.image.Image;

/**
 * PuSuperSpeed is a PowerUp where the effect is
 * drastically increasing the acceleration and speed
 * of the PlayerFish.
 */
public class PuSuperSpeed extends DurationPowerUp {

	private static final int DURATION = 10;
	public static final double ACCELERATION_FACTOR = 2.5;
	public static final double MAX_SPEED_FACTOR = 3;
	
	private static final String NAME = "Super Speed";
	
	/**
	 * Creates a new PowerUp of the SuperSpeed type.
	 * 
	 * @param ba
	 *            The CollisonMask of the PowerUp.
	 * @param pfield
	 *            The PlayingField in which this PowerUp is located.
	 * @param sprite
	 *            The sprite of this PowerUp.
	 */
	public PuSuperSpeed(CollisionMask ba, PlayingField pfield, Image sprite) {
		super(ba, pfield, sprite);
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
		
		IMoveBehaviour behaviour = pf.getBehaviour();
		if (!(behaviour instanceof KeyListenerBehaviour)) {
			return;
		}
		KeyListenerBehaviour keyBehaviour = (KeyListenerBehaviour) behaviour;
		
		keyBehaviour.setAcceleration(keyBehaviour.getAcceleration() * ACCELERATION_FACTOR);
		keyBehaviour.setMaxSpeed(keyBehaviour.getMaxSpeed() * MAX_SPEED_FACTOR);
	}

	/** 
	 * Restores the acceleration and the MaxSpeed of
	 * the PlayerFish we just drugged.
	 */
	@Override
	public void endEffect() {
		PlayerFish pf = super.getTarget();
		
		IMoveBehaviour behaviour = pf.getBehaviour();
		if (!(behaviour instanceof KeyListenerBehaviour)) {
			return;
		}
		KeyListenerBehaviour keyBehaviour = (KeyListenerBehaviour) behaviour;
		
		keyBehaviour.setAcceleration(keyBehaviour.getAcceleration() / ACCELERATION_FACTOR);
		keyBehaviour.setMaxSpeed(keyBehaviour.getMaxSpeed() / MAX_SPEED_FACTOR);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
