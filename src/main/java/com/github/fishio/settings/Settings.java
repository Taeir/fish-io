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
		BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile));
		
		bw.write("screen_width: 854");
		bw.newLine();
		bw.write("screen_height: 480");
		bw.newLine();
		bw.write("loglevel: 0");
		bw.newLine();
		bw.write("PIXEL_PERFECT_COLLISIONS: 1");
		bw.newLine();
		bw.write("FISH_EAT_THRESHOLD: 1.2");
		bw.newLine();
		bw.write("DIRECTION_CHANGE_CHANCE: 0.1");
		bw.newLine();
		bw.write("MIN_EFISH_SPEED: 1");
		bw.newLine();
		bw.write("MAX_EFISH_SPEED: 4");
		bw.newLine();
		bw.write("FISH_SPRITES: 28");
		
		bw.close();			
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
