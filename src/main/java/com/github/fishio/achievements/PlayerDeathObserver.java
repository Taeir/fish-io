package com.github.fishio.achievements;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player dies in single player mode.
 *
 */
class PlayerDeathObserver implements Observer {
	
	private Subject singlePlayerPlayingField;
	private int playerdeathcounter = 0;
	
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
	public void update() {
		playerdeathcounter++;
	}
	
	/**
	 * Gives the class whom asked for it the status of the death counter of the
	 * player fish.
	 * 
	 * @return The amount of times the player has died.
	 */
	public int getPlayerDeathCounter() {
		return playerdeathcounter;
	}
	

}
