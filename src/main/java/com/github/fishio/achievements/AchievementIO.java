package com.github.fishio.achievements;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

public class AchievementIO {
	
	private static File achievementFile = new File("achievements.yml");
	private static Log logger = Log.getLogger();
	private static HashMap<String, Integer> achievemap = new HashMap<>();
	
	public static void addObserverCounter(String name, int value) {
		achievemap.put(name, value);
	}
	
	/**
	 * Save the high scores to 'highscores.yml'.
	 */
	public static void save() {
		try (FileWriter bw = new FileWriter(achievementFile)) {
			YamlWriter yw = new YamlWriter(bw);			
			
			yw.write(achievemap);
			yw.close();
		} catch (IOException e) {
			logger.log(LogLevel.ERROR, "Error writing file " + achievementFile.getAbsolutePath());
			logger.log(LogLevel.DEBUG, e);
		} 
	}

	/**
	 * load the achievements from 'achievemap.yml'.
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		try (FileReader fr = new FileReader(achievementFile)) {
			YamlReader reader = new YamlReader(fr);
			HashMap<String, String> temp = (HashMap<String, String>) reader.read();
			reader.close();
			if (temp == null) {
				return;
			}
			for (String key : temp.keySet()) {
				achievemap.put(key, Integer.valueOf(temp.get(key)));
			}
		} catch (Exception e) {
			logger.log(LogLevel.ERROR, "Error loading file " + achievementFile.getAbsolutePath());
			logger.log(LogLevel.DEBUG, e);
		}
	}

	public static int get(String key) {
		Integer val = achievemap.get(key);
		if (val == null) {
			load();
			 val = achievemap.get(key);
		}
		return val.intValue();
	}
}
