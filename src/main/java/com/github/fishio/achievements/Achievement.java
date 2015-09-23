package com.github.fishio.achievements;

/**
 * This class defines an achievement. The achievement has a state where it's
 * rules are not met and one where the rules have been met.
 *
 */
public class Achievement {
	
	private String achievement;
	private boolean rulesmet = false;

	/**
	 * Constructor for an Achievement
	 * 
	 * @param name
	 *            The name or title of the Achievement
	 * @param achieved
	 *            Whether the user has obtained this achievement or not
	 */
	public Achievement(String name, boolean achieved) {
		this.achievement = name;
		this.rulesmet = achieved;
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
		rulesmet = false;
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
	 * Method which returns whether the achievement's rules have been met.
	 * 
	 * @return true if the rules have been met, false if they have not been met.
	 */
	public boolean getAchieved() {
		return rulesmet;
	}
	
	/**
	 * Method which set the whether the rules of the achievement are met to true
	 * or false.
	 * 
	 * @param achieved
	 *            sets the rulesmet field to true or false.
	 */
	public void setAchieved(boolean achieved) {
		rulesmet = achieved;
	}
}
