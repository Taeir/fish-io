package com.github.fishio.power_ups;

import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.listeners.TickListener;

/**
 * A PowerUp where the effect takes time.
 */
public abstract class DurationPowerUp extends PowerUp implements TickListener {

	private final int timeSeconds;
	private final int timeTicks;
	
	private boolean active;
	private int tickCounter;
	
	/**
	 * Creates a new DurationPowerUp.
	 * 
	 * @param ba
	 * 		The CollisionArea of the Power-Up
	 * @param pfield
	 * 		The PlayingField this PowerUp is located in
	 */
	public DurationPowerUp(ICollisionArea ba, PlayingField pfield) {
		super(ba, pfield);
		
		this.timeSeconds = getDuration();
		this.timeTicks = timeSeconds * pfield.getFPS();
		this.tickCounter = 0;
		
		pfield.registerGameListener(this);
	}
	
	@Override
	public void executeEffect(PlayerFish pf) {
		active = true;
		
		startEffect(pf);
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
	 * with the PlayerFish happens.
	 * 
	 * @param pf
	 * 		The PlayerFish that the PowerUp collided with
	 */
	public abstract void startEffect(PlayerFish pf);
	
	/**
	 * The effect that should happen each time before a game tick.
	 */
	public abstract void preTickEffect();
	
	/**
	 * The effect that should happen each time after a game tick.
	 */
	public abstract void postTickEffect();
	
	/**
	 * The effect that should happen after the duration of the PowerUp.
	 */
	public abstract void endEffect();

	@Override
	public void preTick() {
		
		if (!active) {
			return;
		}
		
		if (tickCounter >= 1) {
			preTickEffect();
		}
			
	}
	
	@Override
	public void postTick() {
		
		if (!active) {
			return;
		}

		tickCounter++;
		
		if (tickCounter >= timeTicks) {
			getPField().unregisterGameListener(this);
			endEffect();
			active = false;
			return;
		}
		
		postTickEffect();
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
	
}
