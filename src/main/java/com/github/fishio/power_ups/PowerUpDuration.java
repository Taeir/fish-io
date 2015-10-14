package com.github.fishio.power_ups;

import javafx.scene.image.Image;

import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.listeners.TickListener;

/**
 * A PowerUp where the effect takes time.
 */
public abstract class PowerUpDuration extends PowerUp implements TickListener {

	private final int timeSeconds;
	private final int timeTicks;
	
	private boolean active;
	private int tickCounter;
	
	private PlayerFish target;
	
	/**
	 * Creates a new DurationPowerUp.
	 * 
	 * @param ba
	 * 		The CollisionArea of the Power-Up
	 * @param pfield
	 * 		The PlayingField this PowerUp is located in
	 * @param sprite
	 * 		The sprite of this PowerUp
	 */
	public PowerUpDuration(ICollisionArea ba, PlayingField pfield, Image sprite) {
		super(ba, pfield, sprite);
		
		this.timeSeconds = getDuration();
		this.timeTicks = timeSeconds * pfield.getFPS();
		this.tickCounter = 0;
		
		pfield.getGameThread().registerListener(this);
	}
	
	@Override
	public void executeEffect(PlayerFish pf) {
		//This PowerUp is now active
		this.active = true;
		
		//The target PlayerFish is the one we collided with
		this.target = pf;
		
		//Begin the startEffect
		startEffect();
	}
	
	/**
	 * @return
	 * 		Whether the effect of this PowerUp is currently active or not.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @return
	 * 		The duration of the PowerUp in seconds.
	 */
	public abstract int getDuration();
	
	/**
	 * The effect that should happen right after the collision
	 * with a PlayerFish happens.
	 */
	public abstract void startEffect();
	
	/**
	 * The effect that should happen after the duration of the PowerUp.
	 */
	public abstract void endEffect();

	@Override
	public void preTick() {
		
		if (!active) {
			return;
		}
	}
	
	@Override
	public void postTick() {
		
		if (!active) {
			return;
		}

		tickCounter++;
		
		if (tickCounter >= timeTicks) {
			getPField().getGameThread().unregisterListener(this);
			endEffect();
			active = false;
			return;
		}
	}
	
	/**
	 * Returns the amount of ticks this PowerUp has processed.
	 * Used only for testing purposes.
	 * 
	 * @return
	 * 		The amount of ticks this PowerUp has processed.
	 */
	public int getTickCounter() {
		return tickCounter;
	}
	
	/**
	 * Sets the tickCounter.
	 * Used only for testing purposes.
	 * 
	 * @param amount
	 * 		The amount to set
	 */
	public void setTickCounter(int amount) {
		tickCounter = amount;
	}
	
	/**
	 * Returns the duration of the effect of this PowerUp in ticks.
	 * Used only for testing purposes.
	 * 
	 * @return
	 * 		The duration of the effect of this PowerUp in ticks
	 */
	public int getTimeTicks() {
		return timeTicks;
	}
	
	/**
	 * Sets whether this PowerUp is active or not.
	 * 
	 * @param active
	 * 		True if the effect should start. False if it should stop.
	 * 		
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * @return
	 * 		The PlayerFish this PowerUp collided with.
	 */
	public PlayerFish getTarget() {
		return target;
	}
	
	/**
	 * Sets the PlayerFish this PowerUp should target.
	 * Should mainly be used for testing purposes.
	 * 
	 * @param target
	 * 		The target that should be set.
	 */
	public void setTarget(PlayerFish target) {
		this.target = target;
	}
	
}
