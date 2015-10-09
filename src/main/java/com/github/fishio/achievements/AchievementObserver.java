package com.github.fishio.achievements;

import java.util.Collection;
import java.util.List;

/**
 * Represents an object that wants to observe a subject class.
 *
 */
public interface AchievementObserver {
	/**
	 * Updates the observer with a change to the subject.
	 * 
	 * @param oldState
	 *            the previous state.
	 * @param newState
	 *            the new state.
	 * @param properties
	 *            the properties that were changed.
	 */
	void update(State oldState, State newState, Collection<String> properties);
	
	/**
	 * @return
	 * 		a list of achievements that are attached.
	 */
	List<Achievement> getAchievements();
	
	/**
	 * Attach an achievement to this observer.<br>
	 * Attached achievements will get an update when our value changes.
	 * 
	 * @param achievement
	 * 		the achievement to attach.
	 */
	default void attachAchievement(Achievement achievement) {
		if (!getAchievements().contains(achievement)) {
			getAchievements().add(achievement);
		}
	}
	
	/**
	 * Unattach an achievement from this Observer.
	 * 
	 * @param achievement
	 * 		the achievement to remove.
	 */
	default void unattachAchievement(Achievement achievement) {
		getAchievements().remove(achievement);
	}
	
	/**
	 * Notify all attached achievements of the update.
	 */
	default void notifyAchievements() {
		for (Achievement a : getAchievements()) {
			a.updateAchievement(this);
		}
	}
}
