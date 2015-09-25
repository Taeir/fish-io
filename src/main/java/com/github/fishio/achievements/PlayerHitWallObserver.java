package com.github.fishio.achievements;

import java.util.Collection;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player hits the boundary of the playing field.
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
	 * Gives the class whom asked for it the status of the hitwall counter of
	 * the player fish.
	 * 
	 * @return The amount of times the player has hit the wall.
	 */
	public int getPlayerDeathCounter() {
		return playerhitwallcounter;
	}

}

