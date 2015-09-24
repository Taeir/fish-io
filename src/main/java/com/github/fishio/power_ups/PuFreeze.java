package com.github.fishio.power_ups;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;

/**
 * A PowerUp with the effect that it freezes all
 * current EnemyFishes in the PlayingField for 10 seconds. 
 */
public class PuFreeze extends DurationPowerUp {

	/**
	 * The duration of the effect in seconds.
	 */
	private static final int DURATION = 10;
	
	/**
	 * Creates a new PowerUp of the Freeze type.
	 * 
	 * @param ba
	 * 		The CollisonArea of the PowerUp.
	 * @param pfield
	 * 		The PlayingField in which this PowerUp is located.
	 */
	public PuFreeze(ICollisionArea ba, PlayingField pfield) {
		super(ba, pfield);
	}

	@Override
	public int getDuration() {
		return DURATION;
	}

	/**
	 * Freezes all EnemyFishes on the field.
	 */
	@Override
	public void startEffect() {
		for (Entity e : getPField().getEntities()) {
			if (e instanceof EnemyFish) {
				((EnemyFish) e).setFrozen(true);
			}
		}
	}

	@Override
	public void preTickEffect() { }

	@Override
	public void postTickEffect() { }

	@Override
	public void endEffect() {
		for (Entity e : getPField().getEntities()) {
			if (e instanceof EnemyFish) {
				((EnemyFish) e).setFrozen(false);
			}
		}
	}

}
