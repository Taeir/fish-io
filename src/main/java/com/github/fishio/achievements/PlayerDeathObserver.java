package com.github.fishio.achievements;

import java.util.Collection;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player dies in single player mode.
 *
 */
class PlayerDeathObserver implements Observer {
	
	private Subject singlePlayerPlayingField;
	private static int playerdeathcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public PlayerDeathObserver(Subject subject) {
		this.singlePlayerPlayingField = subject;
		this.singlePlayerPlayingField.attach(this);
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("PlayerDeath")) {
			return;
		}
		playerdeathcounter++;
	}
	
	/**
	 * A method which return the death counter of the player fish.
	 * 
	 * @return the death counter of the player fish.
	 */
	public static int getCounter() {
		return playerdeathcounter;
	}
	
	/**
	 * Sets the death counter of the player fish. For testing purposes only.
	 * 
	 * @param counter
	 *            Number which sets the deathcounter to a specific value for
	 *            testing.
	 */
	public static void setCounter(int counter) {
		playerdeathcounter = counter;
	}
}
