package com.github.fishio.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.fishio.SinglePlayerPlayingField;

/**
 * This class is an Observer. It attaches to the SinglePlayerPlayingField and
 * gets an update when a player increases it's score in single player mode.
 *
 */
public class PlayerScoreObserver implements AchievementObserver {
	private List<Achievement> achievements = new ArrayList<>();
	private Subject playerFish;
	private int playerscorecounter = 0;

	/**
	 * Attaches the observer to the subject.
	 * 
	 * @param subject
	 *            The subject this observer has to be notified by.
	 */
	public PlayerScoreObserver(SinglePlayerPlayingField subject) {
		setCounter(AchievementIO.get("playerScore"));
		this.playerFish = subject.getPlayer();
		this.playerFish.attach(this);

		registerPlayerListener(subject);

		attachAchievement(AchievementManager.PLAYER_SCORE);
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
		sppf.playerProperty().addListener((observable, oldFish, newFish) -> {
			// Change our subject when the player changes.
			playerFish = newFish;
			playerFish.attach(PlayerScoreObserver.this);
		});
	}

	@Override
	public void update(State old, State now, Collection<String> properties) {
		if (!properties.contains("dead")) {
			return;
		}
		playerscorecounter++;
		notifyAchievements();
	}

	/**
	 * A method which return the Score counter of the player fish.
	 * 
	 * @return the Score counter of the player fish.
	 */
	public int getCounter() {
		return playerscorecounter;
	}

	/**
	 * Sets the Score counter of the player fish. For testing purposes only.
	 * 
	 * @param counter
	 *            Number which sets the Scorecounter to a specific value for
	 *            testing.
	 */
	public void setCounter(int counter) {
		playerscorecounter = counter;
		notifyAchievements();
	}

	@Override
	public List<Achievement> getAchievements() {
		return achievements;
	}
}
