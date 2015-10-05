package com.github.fishio.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javafx.beans.property.SimpleDoubleProperty;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Data class with all the settings.
 * 
 * When the game stops working after an addition to the settings, simply remove the settings.cfg file
 */
public final class Settings {
	private static final Settings INSTANCE = new Settings();
	
	private File settingsFile;
	private Log log = Log.getLogger();
	private HashMap<String, SimpleDoubleProperty> settings = new HashMap<String, SimpleDoubleProperty>();
	
	private Settings() {
		settingsFile = new File("settings.cfg");
		// If file doesn't exists, then create it
		try {
			if (!settingsFile.exists()) {
				createSettingsFile();				
			}
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error creating setting file settings.cfg!");
		}
		loadSettings();
	}
	
	/**
	 * Method called when the settings file does not exist.
	 * Creates the file and writes standard settings to it.
	 * @throws IOException
	 * 		when something goes wrong with creating the files.
	 */
	private void createSettingsFile() throws IOException {
		settingsFile.getAbsoluteFile().getParentFile().mkdirs();
		settingsFile.createNewFile();
		
		// doubles
		settings.put("FISH_EAT_THRESHOLD", new SimpleDoubleProperty(1.2));
		settings.put("DIRECTION_CHANGE_CHANCE", new SimpleDoubleProperty(0.1));
		settings.put("MIN_EFISH_SPEED", new SimpleDoubleProperty(1));
		settings.put("MAX_EFISH_SPEED", new SimpleDoubleProperty(4));
		settings.put("MAX_PLAYER_SPEED", new SimpleDoubleProperty(4.0));
		settings.put("GROWTH_SPEED", new SimpleDoubleProperty(500));
		settings.put("FISH_EAT_THRESHOLD", new SimpleDoubleProperty(1.2));
		settings.put("GAME_TPS", new SimpleDoubleProperty(60));

		// booleans
		settings.put("DEBUG_DRAW", new SimpleDoubleProperty(0));
		settings.put("PIXEL_PERFECT_COLLISIONS", new SimpleDoubleProperty(1));
		
		// integers
		settings.put("SCREEN_WIDTH", new SimpleDoubleProperty(1280));
		settings.put("SCREEN_HEIGHT", new SimpleDoubleProperty(720));
		settings.put("LOG_LEVEL", new SimpleDoubleProperty(2));
		settings.put("START_LIVES", new SimpleDoubleProperty(3));
		settings.put("MAX_LIVES", new SimpleDoubleProperty(5));
		settings.put("FISH_SPRITES", new SimpleDoubleProperty(28));
		settings.put("POWERUP_SPAWN_INTERVAL", new SimpleDoubleProperty(30));
		
		save();		
	}

	/**
	 * @return
	 * 		the singleton instance of this class.
	 */
	public static Settings getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Get the SimpleDoubleProperty of the specified setting.
	 * @param setting
	 * 		setting to get the value for.
	 * @return
	 * 		a SimpleDoubleProperty with the value of the setting or NaN if the setting is not found.
	 */
	public SimpleDoubleProperty getSimpleProperty(String setting) {
		SimpleDoubleProperty res = settings.get(setting);
		if (res == null) {
			log.log(LogLevel.ERROR, "Setting '" + setting + "' not found!");
			return new SimpleDoubleProperty(Double.NaN);
		}
		return res;
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting or NaN if the setting is not found.
	 */
	public double get(String setting) {
		return getSimpleProperty(setting).doubleValue();
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void set(String setting, double newValue) {
		getSimpleProperty(setting).set(newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue);
	}
	
	/**
	 * Method that loads the settings from the settings file.
	 */
	public void loadSettings() {
		try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
			String line = br.readLine();
			while (line != null) {
				String[] pair = line.split(": ");
				double temp;
				try {
					temp = Double.valueOf(pair[1]);
				} catch (NumberFormatException nfo) {
					log.log(LogLevel.ERROR, pair[1] + " is no double value!");
					continue;
				}
				SimpleDoubleProperty value = new SimpleDoubleProperty(temp);
				settings.put(pair[0], value);
				line = br.readLine();
			}
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error reading settings.cfg!");
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current settings to a file.
	 */
	public void save() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile))) {
			for (String key : settings.keySet()) {
				SimpleDoubleProperty value = settings.get(key);
				bw.write(key + ": " + value.doubleValue());
				bw.newLine();
			}
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error saving settings file!");
		}
	}
}
