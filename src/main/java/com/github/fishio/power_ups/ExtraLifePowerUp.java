package com.github.fishio.power_ups;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

import javafx.scene.image.Image;

/**
 * A PowerUp with the effect that it instantly increases the PlayerFish' life
 * (if it isn't maxed out already).
 */
public class ExtraLifePowerUp extends PowerUp {

	private static final String NAME = "Extra Life";
	
	/**
	 * Creates a new PowerUp of the Extra Life type.
	 * 
	 * @param collisionMask
	 *            the CollisonMask of the PowerUp.
	 * @param playingField
	 *            the PlayingField in which this PowerUp is located.
	 * @param sprite
	 *            the sprite of this PowerUp.
	 */

	public ExtraLifePowerUp(CollisionMask collisionMask, PlayingField playingField, Image sprite) {
		super(collisionMask, playingField, sprite);
	}

	@Override
	public void executeEffect(PlayerFish pfish) {
		pfish.addLife();
	}

	@Override
	public String getName() {
		return NAME;
	}

}
