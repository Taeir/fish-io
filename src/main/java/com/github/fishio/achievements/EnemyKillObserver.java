package com.github.fishio.achievements;

import java.util.Collection;

/**
 * This class is an Observer. It attaches to the PlayerFish class and gets an
 * update when an enemy fish dies due to the player fish (kill).
 *
 */
class EnemyKillObserver implements Observer {
	
	private Subject playerFish;
	private int enemydeathcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public EnemyKillObserver(Subject subject) {
		this.playerFish = subject;
		this.playerFish.attach(this);
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("death")) {
			return;
		}
		
		enemydeathcounter++;
	}
	
	/**
	 * Gives the class whom asked for it the status of the kill counter of the
	 * enemy fish.
	 * 
	 * @return The amount of times the player has died.
	 */
	public int getPlayerDeathCounter() {
		return enemydeathcounter;
	}

}
