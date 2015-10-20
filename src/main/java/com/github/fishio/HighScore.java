package com.github.fishio;

import java.util.HashMap;

/**
 * Data class for high scores.
 */
public final class HighScore {

	private HighScore() { }
	
	private static final HashMap<String, Integer> HIGHSCORES = loadHighScores();
	
	/**
	 * Add a score for the specified player name.
	 * This score will be added when the existing score is lower or non existent.
	 * @param score
	 * 		The new score.
	 * @param name
	 * 		The name for the score
	 */
	public static void addScore(int score, String name) {
		Integer temp = HIGHSCORES.get(name);
		if (temp == null || temp.intValue() < score) {
			HIGHSCORES.put(name, score);
			save();
		}
	}
	
	/**
	 * @return
	 * 		The hashmap containing all the highscores.
	 */
	public static HashMap<String, Integer> getAll() {
		return HIGHSCORES;
	}
	
	/**
	 * Save the highscores to 'highscores.yml'.
	 */
	private static void save() {
		// TODO save		
	}

	/**
	 * load the highscores from 'highscores.yml'.
	 */
	private static HashMap<String, Integer> loadHighScores() {
		//TODO read
		return new HashMap<String, Integer>();
	}

}
