package com.github.fishio.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.fishio.SinglePlayerPlayingField;

/**
 * This class is an Observer. It attaches to the PlayerFish class and gets an
 * update when an enemy fish dies due to the player fish (kill).
 *
 */
public class EnemyKillObserver implements AchievementObserver {
	private List<Achievement> achievements = new ArrayList<>();
	private Subject playerFish;
	private int enemykillcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public EnemyKillObserver(SinglePlayerPlayingField subject) {
		enemykillcounter = AchievementIO.get("enemyKill");
		this.playerFish = subject.getPlayer();
		this.playerFish.attach(this);
		
		registerPlayerListener(subject);
		
		attachAchievement(AchievementManager.ENEMY_KILL);
	}
	
	/**
	 * Registers a listener for the playerProperty, so that when the player
	 * changes, this Observer keeps working.
	 * 
	 * @param sppf
	 *            the SinglePlayerPlayingField to register for.
	 */
	protected final void registerPlayerListener(SinglePlayerPlayingField sppf) {
		sppf.playerProperty().addListener((observable, oldFish, newFish) ->
		{
			// Change our subject when the player changes.
			playerFish = newFish;
			playerFish.attach(EnemyKillObserver.this);
		});
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("EnemyKill")) {
			return;
		}
		
		enemykillcounter++;
		notifyAchievements();
	}
	
	/**
	 * Gives the class whom asked for it the status of the kill counter of the
	 * enemy fish.
	 * 
	 * @return The amount of times the player has died.
	 */
	public int getCounter() {
		return enemykillcounter;
	}
	
	/**
	 * Sets the enemy fish kill counter of the player fish. For testing purposes
	 * only.
	 * 
	 * @param counter
	 *            Number which sets the enemykillcounter to a specific value for
	 *            testing.
	 */
	public void setCounter(int counter) {
		enemykillcounter = counter;
		notifyAchievements();
	}

	@Override
	public List<Achievement> getAchievements() {
		return achievements;
	}

}
