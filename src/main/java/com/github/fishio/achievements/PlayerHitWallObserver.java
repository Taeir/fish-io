package com.github.fishio.achievements;

import java.util.Collection;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player dies in single player mode.
 *
 */
class HitWallObserver implements Observer {
	
	private Subject playerFish;
	private int playerhitwallcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public HitWallObserver(Subject subject) {
		this.playerFish = subject;
		this.playerFish.attach(this);
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("HitWall")) {
			return;
		}
		
		playerhitwallcounter++;
	}
	
	/**
	 * Gives the class whom asked for it the status of the death counter of the
	 * player fish.
	 * 
	 * @return The amount of times the player has died.
	 */
	public int getPlayerDeathCounter() {
		return playerhitwallcounter;
	}

}

