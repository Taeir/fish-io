package com.github.fishio.power_ups;

import java.util.HashMap;

import com.github.fishio.CollisionMask;
import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.IMoveBehaviour;

import javafx.scene.image.Image;

/**
 * A PowerUp with the effect that it freezes all
 * current EnemyFishes in the PlayingField for 10 seconds. 
 */
public class FreezePowerUp extends PowerUpDuration {

	private static final String NAME = "Freeze";
	
	/**
	 * The duration of the effect in seconds.
	 */
	private static final int DURATION = 10;
	
	private HashMap<EnemyFish, IMoveBehaviour> oldBehaviours = new HashMap<>();;
	
	/**
	 * Creates a new PowerUp of the Freeze type.
	 * 
	 * @param ba
	 * 		The CollisonMask of the PowerUp.
	 * @param pfield
	 * 		The PlayingField in which this PowerUp is located.
	 * @param sprite
	 * 		The sprite of this PowerUp
	 */

	public FreezePowerUp(CollisionMask ba, PlayingField pfield, Image sprite) {
		super(ba, pfield, sprite);
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
		
		for (Entity e : getPField().getEntities()) {
			if (e instanceof EnemyFish) {
				EnemyFish fish = (EnemyFish) e;
				oldBehaviours.put(fish, fish.getBehaviour());
				fish.setBehaviour(new FrozenBehaviour());
			}
		}
	}

	/** 
	 * Resets the behaviour of the EnemyFishes.
	 */
	@Override
	public void endEffect() {
		for (Entity e : oldBehaviours.keySet()) {
			if (e instanceof EnemyFish) {
				EnemyFish fish = (EnemyFish) e;
				fish.setBehaviour(oldBehaviours.get(fish));
			}
		}
		oldBehaviours.clear();
	}

	@Override
	public String getName() {
		return NAME;
	}

}
