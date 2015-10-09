package com.github.fishio.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player uses a live.
 *
 */
public class LivesConsumptionObserver implements AchievementObserver {
	//TODO TODO TODO
	private List<Achievement> achievements = new ArrayList<>();
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
		notifyAchievements();
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

	@Override
	public List<Achievement> getAchievements() {
		return achievements;
	}

}