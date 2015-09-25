package com.github.fishio.power_ups;

import javafx.scene.image.Image;

import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * A PowerUp with the effect that it instantly increases
 * the PlayerFish' life (if it isn't maxed out already).
 */
public class PuExtraLife extends PowerUp {

	private static final String NAME = "Extra Life";
	
	/**
	 * Creates a new PowerUp of the Extra Life type.
	 * 
	 * @param ba
	 * 		The CollisonArea of the PowerUp.
	 * @param pfield
	 * 		The PlayingField in which this PowerUp is located.
	 * @param sprite
	 * 		The sprite of this PowerUp.
	 */
	public PuExtraLife(ICollisionArea ba, PlayingField pfield, Image sprite) {
		super(ba, pfield, sprite);
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