package com.github.fishio.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.fishio.SinglePlayerPlayingField;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player hits the boundary of the playing field.
 *
 */
public class HitWallObserver implements AchievementObserver {
	private List<Achievement> achievements = new ArrayList<>();
	private Subject playerFish;
	private int hitwallcounter = 0;
	
	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public HitWallObserver(SinglePlayerPlayingField subject) {
		setCounter(AchievementIO.get("hitWall"));
		this.playerFish = subject.getPlayer();
		this.playerFish.attach(this);

		registerPlayerListener(subject);

		attachAchievement(AchievementManager.HIT_WALL);
		notifyAchievements();
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
			playerFish.attach(HitWallObserver.this);
		});
	}
	
	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("HitWall")) {
			return;
		}

		hitwallcounter++;
		notifyAchievements();
	}

	/**
	 * Gives the class whom asked for it the status of the hitwall counter of
	 * the player fish.
	 * 
	 * @return The amount of times the player has hit the wall.
	 */
	public int getCounter() {
		return hitwallcounter;
	}

	/**
	 * Sets the hitwall counter of the player fish. For testing purposes only.
	 * 
	 * @param counter
	 *            Number which sets the hitwallcounter to a specific value for
	 *            testing.
	 */
	public void setCounter(int counter) {
		hitwallcounter = counter;
		notifyAchievements();
	}

	@Override
	public List<Achievement> getAchievements() {
		return achievements;
	}

}

