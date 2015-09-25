package com.github.fishio.power_ups;

import javafx.scene.image.Image;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayingField;

/**
 * A PowerUp with the effect that it freezes all
 * current EnemyFishes in the PlayingField for 10 seconds. 
 */
public class PuFreeze extends DurationPowerUp {

	private static final String NAME = "Freeze";
	
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
	 * @param sprite
	 * 		The sprite of this PowerUp
	 */
	public PuFreeze(ICollisionArea ba, PlayingField pfield, Image sprite) {
		super(ba, pfield, sprite);
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

	@Override
	public String getName() {
		return NAME;
	}

}