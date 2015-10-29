package com.github.fishio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Data class for high scores.
 */
public final class HighScore {

	private static File scoreFile = new File("highScores.yml");
	private static HashMap<String, Integer> highscores = new HashMap<>();
	private static Log logger = Log.getLogger();

	private HighScore() { }

	/**
	 * Add a score for the specified player name.
	 * This score will be added when the existing score is lower or non existent.
	 * @param score
	 * 		The new score.
	 * @param name
	 * 		The name for the score
	 */
	public static void addScore(int score, String name) {
		if (name.equals("")) {
			return;
		}
		Integer temp = highscores.get(name);
		if (temp == null || temp.intValue() < score) {
			highscores.put(name, score);
			logger.log(LogLevel.TRACE, "New high score added: " + score + " points for " + name);
		}
	}

	/**
	 * @return
	 * 		The hashmap containing all the highscores.
	 */
	public static HashMap<String, Integer> getAll() {
		return highscores;
	}

	/**
	 * Save the high scores to 'highscores.yml'.
	 */
	protected static void save() {
		try (FileWriter bw = new FileWriter(scoreFile)) {
			YamlWriter yw = new YamlWriter(bw);			
			yw.write(highscores);
			yw.close();
		} catch (IOException e) {
			logger.log(LogLevel.ERROR, "Error writing file " + scoreFile.getAbsolutePath());
			logger.log(LogLevel.DEBUG, e);
		} 
	}

	/**
	 * load the highscores from 'highscores.yml'.
	 */
	@SuppressWarnings("unchecked")
	protected static void loadHighScores() {
		try (FileReader fr = new FileReader(scoreFile)) {
			YamlReader reader = new YamlReader(fr);
			HashMap<String, String> temp = (HashMap<String, String>) reader.read();
			reader.close();
			if (temp == null) {
				return;
			}
			for (String key : temp.keySet()) {
				highscores.put(key, Integer.valueOf(temp.get(key)));
			}
		} catch (Exception e) {
			logger.log(LogLevel.ERROR, "Error loading file " + scoreFile.getAbsolutePath());
			logger.log(LogLevel.DEBUG, e);
		}
	}

	/**
	 * Initialize the high scores.
	 * This will load the existing scores or create a new file. 
	 */
	public static void init() { 
		if (!scoreFile.exists()) {
			try {
				scoreFile.createNewFile();
			} catch (IOException e) {
				logger.log(LogLevel.ERROR, "Error creating file " + scoreFile.getAbsolutePath());
				logger.log(LogLevel.DEBUG, e);
			}
		}
		loadHighScores();
	}
	
	/**
	 * Set the file for the highscores. This method should only be used for
	 * testing.
	 * 
	 * @param newFile
	 *            The new file.
	 */
	protected static void setScoreFile(File newFile) {
		scoreFile = newFile;
	}
}
