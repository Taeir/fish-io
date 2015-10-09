package com.github.fishio.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player hits the boundary of the playing field.
 *
 */
public class PlayerHitWallObserver implements AchievementObserver {
	private List<Achievement> achievements = new ArrayList<>();
	private Subject playerFish;
	private int playerhitwallcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public PlayerHitWallObserver(Subject subject) {
		this.playerFish = subject;
		this.playerFish.attach(this);
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("HitWall")) {
			return;
		}
		
		playerhitwallcounter++;
		notifyAchievements();
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

	@Override
	public List<Achievement> getAchievements() {
		return achievements;
	}

}

