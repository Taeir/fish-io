package com.github.fishio.achievements;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class defines an achievement. The achievement has a state where it's
 * rules are not met and one where the rules have been met.
 *
 */
public abstract class Achievement {
	
	private String achievement;
	private SimpleIntegerProperty levelProperty = new SimpleIntegerProperty(0);

	/**
	 * Constructor for an Achievement.
	 * 
	 * @param name
	 *            The name or title of the Achievement.
	 * @param level
	 *            The level of achievement that is obtained. Level 0 means not
	 *            obtained yet.
	 */
	public Achievement(String name, int level) {
		this.achievement = name;
		this.levelProperty.set(level);
	}
	
	/**
	 * Constructor for an Achievement Object without input whether it has been
	 * achieved already.
	 * 
	 * @param name
	 *            The name or title of the Achievement
	 */
	public Achievement(String name) {
		this.achievement = name;
		this.levelProperty.set(0);
	}
	
	/**
	 * Method which returns the name or title of the achievement.
	 * 
	 * @return the achievement's name.
	 */
	public String getName() {
		return achievement;
	}
	
	/**
	 * Method which returns the level of the achievement. Level 0 means not
	 * achieved yet. In total there are 3 or 5 levels, depending on the
	 * achievement.
	 * 
	 * @return a value in the range from 0 up until 5.
	 */
	public int getLevel() {
		return levelProperty.get();
	}
	
	/**
	 * Method which set the whether the rules of the achievement are met to true
	 * or false.
	 * 
	 * @param levelachieved
	 *            sets the achieved level of the achievement.
	 */
	public void setLevel(int levelachieved) {
		levelProperty.set(levelachieved);
	}
	
	/**
	 * @return
	 * 		the level property.
	 */
	public SimpleIntegerProperty getLevelProperty() {
		return levelProperty;
	}
	
	/**
	 * Called when an observer required for this achievement changes.
	 * 
	 * @param observer
	 * 		the observer whose value was changed.
	 */
	public abstract void updateAchievement(AchievementObserver observer);
	
	@Override
	public int hashCode() {
		return achievement.hashCode() * 31 + getLevel();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Achievement) {
			Achievement that = (Achievement) other;
			return (that.getName().equals(this.getName()) && that.getLevel() == this.getLevel());
		}
		return false;
	}
}
