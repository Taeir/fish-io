package com.github.fishio.achievements;

import java.util.Collection;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player uses a live.
 *
 */
class LivesConsumptionObserver implements Observer {
	
	private Subject playerFish;
	private int livesconsumptioncounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public LivesConsumptionObserver(Subject subject) {
		this.playerFish = subject;
		this.playerFish.attach(this);
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("HitWall")) {
			return;
		}
		
		livesconsumptioncounter++;
	}
	
	/**
	 * Gives the class whom asked for it the status of the lives consumption
	 * counter of the player fish.
	 * 
	 * @return The amount of times the player has used a live.
	 */
	public int getPlayerDeathCounter() {
		return livesconsumptioncounter;
	}

}