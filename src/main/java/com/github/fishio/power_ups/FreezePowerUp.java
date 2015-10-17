package com.github.fishio.power_ups;

import java.util.HashMap;
import java.util.Map.Entry;

import com.github.fishio.CollisionMask;
import com.github.fishio.EnemyFish;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.IMoveBehaviour;

import javafx.scene.image.Image;

/**
 * A PowerUp with the effect that it freezes all current EnemyFishes in the
 * PlayingField for 10 seconds.
 */
public class FreezePowerUp extends PowerUpDuration {

	private static final long serialVersionUID = 3059292845171507960L;
	private static final String NAME = "Freeze";
	
	/**
	 * The duration of the effect in seconds.
	 */
	private static final int DURATION = 10;
	
	private HashMap<EnemyFish, IMoveBehaviour> oldBehaviours = new HashMap<>();
	
	/**
	 * Creates a new PowerUp of the Freeze type.
	 * 
	 * @param collisionMask
	 * 		The CollisonMask of the PowerUp.
	 * @param playingField
	 * 		The PlayingField in which this PowerUp is located.
	 * @param sprite
	 * 		The sprite of this PowerUp
	 */

	public FreezePowerUp(CollisionMask collisionMask, PlayingField playingField, Image sprite) {
		super(collisionMask, playingField, sprite);
	}

	@Override
	public int getDuration() {
		return DURATION;
	}

	/**
	 * Gives all the EnemyFishes on the field a FrozenBehaviour
	 * and also remembers what their old behaviour was.
	 */
	@Override
	public void startEffect() {
		getPlayingField().getEntities().parallelStream()
			.filter(e -> e instanceof EnemyFish)
			.forEach((e) -> {
				EnemyFish fish = (EnemyFish) e;
				oldBehaviours.put(fish, fish.getBehaviour());
				fish.setBehaviour(new FrozenBehaviour());
			});
	}

	/** 
	 * Resets the behaviour of the EnemyFishes.
	 */
	@Override
	public void endEffect() {
		for (Entry<EnemyFish, IMoveBehaviour> entry : oldBehaviours.entrySet()) {
			entry.getKey().setBehaviour(entry.getValue());
		}

		oldBehaviours.clear();
	}

	@Override
	public String getName() {
		return NAME;
	}

}
