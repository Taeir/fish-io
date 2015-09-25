package com.github.fishio.achievements;

/**
 * This class defines an achievement. The achievement has a state where it's
 * rules are not met and one where the rules have been met.
 *
 */
public class Achievement {
	
	private String achievement;
	private int level = 0;

	/**
	 * Constructor for an Achievement.
	 * 
	 * @param name
	 *            The name or title of the Achievement
	 * @param level
	 *            The level of achievement that is obtained. Level 0 means not
	 *            obtained yet.
	 */
	public Achievement(String name, int level) {
		this.achievement = name;
		this.level = level;
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
		this.level = 0;
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
		return level;
	}
	
	/**
	 * Method which set the whether the rules of the achievement are met to true
	 * or false.
	 * 
	 * @param levelachieved
	 *            sets the achieved level of the achievement.
	 */
	public void setLevel(int levelachieved) {
		level = levelachieved;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result;
		return result + achievement.hashCode();
		}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Achievement other = (Achievement) obj;
		if (!achievement.equals(other.achievement)) {
			return false;
		}
		if (level != other.level) {
			return false;
		}
		return true;
	}
}
