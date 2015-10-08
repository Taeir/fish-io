package com.github.fishio.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Data class with all the settings.
 * 
 * When the game stops working after an addition to the settings, simply remove the settings.yml file
 */
public final class Settings {
	private static final Settings INSTANCE = new Settings();
	
	private File settingsFile;
	private Log log = Log.getLogger();
	private HashMap<String, SimpleDoubleProperty> doubleSettings = new HashMap<String, SimpleDoubleProperty>();
	private HashMap<String, SimpleIntegerProperty> integerSettings = new HashMap<String, SimpleIntegerProperty>();
	private HashMap<String, SimpleBooleanProperty> booleanSettings = new HashMap<String, SimpleBooleanProperty>();
	private HashMap<String, SimpleDoubleProperty> sliderSettings = new HashMap<String, SimpleDoubleProperty>();
	private HashMap<String, KeyCode> keySettings = new HashMap<String, KeyCode>();
	
	private HashMap<String, String> descriptions = new HashMap<String, String>();
	
	private Settings() {
		settingsFile = new File("settings.yml");
		loadDescriptions();
		// If file doesn't exists, then create it
		try {
			if (!settingsFile.exists()) {
				createSettingsFile();				
			} else {
				loadSettings();
			}
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error creating setting file settings.yml!");
		}
	}
	
	private void loadDescriptions() {
		descriptions.put("SCREEN_WIDTH", "The width of the screen in pixels");
		descriptions.put("SCREEN_HEIGHT", "The height of the screen in pixels");
		descriptions.put("DIRECTION_CHANGE_CHANCE", "The chance a fish will change trajectory per tick.");
		descriptions.put("MIN_EFISH_SPEED", "The minimum speed of an enemy fish");
		descriptions.put("MAX_EFISH_SPEED", "The maximum speed of an enemy fish");
		descriptions.put("MAX_PLAYER_SPEED", "The maximum speed of the player fish");		
		descriptions.put("DEBUG_DRAW", "Render debug values");
		descriptions.put("GROWTH_SPEED", "The growth speed of the player fish");
		descriptions.put("LOG_LEVEL", "The level of messages that will be logged");
		descriptions.put("START_LIVES", "The amount of lives a player starts with");
		descriptions.put("MAX_LIVES", "The maximum amount of lives a player can get");
		descriptions.put("POWERUP_SPAWN_INTERVAL", "The interval between the spawning of powerups in seconds");
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
		doubleSettings.put("SCREEN_WIDTH", new SimpleDoubleProperty(1280));
		doubleSettings.put("SCREEN_HEIGHT", new SimpleDoubleProperty(720));
		doubleSettings.put("FISH_EAT_THRESHOLD", new SimpleDoubleProperty(1.2));
		doubleSettings.put("DIRECTION_CHANGE_CHANCE", new SimpleDoubleProperty(0.1));
		doubleSettings.put("MIN_EFISH_SPEED", new SimpleDoubleProperty(1));
		doubleSettings.put("MAX_EFISH_SPEED", new SimpleDoubleProperty(4));
		doubleSettings.put("MAX_PLAYER_SPEED", new SimpleDoubleProperty(4.0));
		
		// booleans
		booleanSettings.put("DEBUG_DRAW", new SimpleBooleanProperty(false));
		booleanSettings.put("PIXEL_PERFECT_COLLISIONS", new SimpleBooleanProperty(true));
		
		// integers
		integerSettings.put("GROWTH_SPEED", new SimpleIntegerProperty(2));
		integerSettings.put("LOG_LEVEL", new SimpleIntegerProperty(2));
		integerSettings.put("START_LIVES", new SimpleIntegerProperty(3));
		integerSettings.put("MAX_LIVES", new SimpleIntegerProperty(5));
		integerSettings.put("POWERUP_SPAWN_INTERVAL", new SimpleIntegerProperty(30));

		// keys
		keySettings.put("SWIM_UP", KeyCode.UP);
		keySettings.put("SWIM_DOWN", KeyCode.DOWN);
		keySettings.put("SWIM_LEFT", KeyCode.LEFT);
		keySettings.put("SWIM_RIGHT", KeyCode.RIGHT);
		
		//slider values
		sliderSettings.put("MASTER_VOLUME", new SimpleDoubleProperty(1.0));
		sliderSettings.put("MUSIC_VOLUME", new SimpleDoubleProperty(0.8));
		sliderSettings.put("EFFECTS_VOLUME", new SimpleDoubleProperty(1.0));
		
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
	public SimpleDoubleProperty getSliderProperty(String setting) {
		SimpleDoubleProperty s = sliderSettings.get(setting);
		if (s == null) {
			log.log(LogLevel.ERROR, "Setting '" + setting + "' not found!");
			return new SimpleDoubleProperty(Double.NaN);
		}		
		return s;
	}
	
	/**
	 * Get the SimpleDoubleProperty of the specified setting.
	 * @param setting
	 * 		setting to get the value for.
	 * @return
	 * 		a SimpleDoubleProperty with the value of the setting or NaN if the setting is not found.
	 */
	public SimpleDoubleProperty getDoubleProperty(String setting) {
		SimpleDoubleProperty s = doubleSettings.get(setting);
		if (s == null) {
			log.log(LogLevel.ERROR, "Setting '" + setting + "' not found!");
			return new SimpleDoubleProperty(Double.NaN);
		}		
		return s;
	}
	
	/**
	 * Get the Integer Property of the specified setting.
	 * @param setting
	 * 		setting to get the value for.
	 * @return
	 * 		a Property<Number> with the value of the setting or Integer.MIN_VALUE if the setting is not found.
	 */
	public SimpleIntegerProperty getIntegerProperty(String setting) {
		SimpleIntegerProperty s = integerSettings.get(setting);
		if (s == null) {
			log.log(LogLevel.ERROR, "Setting '" + setting + "' not found!");
			return new SimpleIntegerProperty(Integer.MIN_VALUE);
		}		
		return s;
	}
	
	/**
	 * Get the Boolean Property of the specified setting.
	 * @param setting
	 * 		setting to get the value for.
	 * @return
	 * 		a Boolean eProperty with the value of the setting.
	 */
	public SimpleBooleanProperty getBooleanProperty(String setting) {
		SimpleBooleanProperty s = booleanSettings.get(setting);
		if (s == null) {
			log.log(LogLevel.ERROR, "Setting '" + setting + "' not found!");
			return new SimpleBooleanProperty();
		}
		return s;
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting or NaN if the setting is not found.
	 */
	public double getDouble(String setting) {
		return getDoubleProperty(setting).getValue().doubleValue();
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting.
	 */
	public KeyCode getKeyCode(String setting) {
		return keySettings.get(setting);
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting or Integer.MIN_VALUE if the setting is not found.
	 */
	public int getInteger(String setting) {
		return getIntegerProperty(setting).getValue().intValue();
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting or NaN if the setting is not found.
	 */
	public double getSlider(String setting) {
		return getSliderProperty(setting).getValue().doubleValue();
	}
	
	/**
	 * Get the value of the specified setting.
	 * @param setting
	 * 		The setting to get the value of.
	 * @return
	 * 		The value of the setting or NaN if the setting is not found.
	 */
	public boolean getBoolean(String setting) {
		return getBooleanProperty(setting).getValue().booleanValue();
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void setDouble(String setting, double newValue) {
		getDoubleProperty(setting).setValue(newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue);
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void setSlider(String setting, double newValue) {
		getSliderProperty(setting).setValue(newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue);
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void setBoolean(String setting, boolean newValue) {
		getBooleanProperty(setting).setValue(newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue);
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void setInteger(String setting, int newValue) {
		getIntegerProperty(setting).setValue(newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue);
	}
	
	/**
	 * Set the value of a setting.
	 * @param setting
	 * 		the setting to set the new value for.
	 * @param newValue
	 * 		The new value for the setting.
	 */
	public void setKey(String setting, KeyCode newValue) {
		keySettings.put(setting, newValue);
		log.log(LogLevel.DEBUG, setting + " changed to " + newValue.getName());		
	}

	/**
	 * Method that loads the settings from the settings file.
	 */
	@SuppressWarnings("unchecked")
	public void loadSettings() {
		try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
			YamlReader reader = new YamlReader(br);
			Map<String, String> object = (Map<String, String>) reader.read();
		    for (String key : object.keySet()) {
		    	double value = Double.valueOf(object.get(key));
		    	doubleSettings.put(key, new SimpleDoubleProperty(value));
		    }
		    object = (Map<String, String>) reader.read();
		    for (String key : object.keySet()) {
		    	int value = Integer.valueOf(object.get(key));
		    	integerSettings.put(key, new SimpleIntegerProperty(value));
		    }
		    object = (Map<String, String>) reader.read();
		    for (String key : object.keySet()) {
		    	boolean value = Boolean.valueOf(object.get(key));
		    	booleanSettings.put(key, new SimpleBooleanProperty(value));
		    }
		    object = (Map<String, String>) reader.read();
		    for (String key : object.keySet()) {
		    	KeyCode value = KeyCode.getKeyCode(object.get(key));
		    	keySettings.put(key, value);
		    }
		    
		    object = (Map<String, String>) reader.read();
		    for (String key : object.keySet()) {
		    	double value = Double.valueOf(object.get(key));
		    	sliderSettings.put(key, new SimpleDoubleProperty(value));
		    }
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error reading settings.yml!");
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current settings to a file.
	 */
	public void save() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile))) {
			//TODO get yaml writing working
			for (String key : doubleSettings.keySet()) {
				bw.write(key + ": " + doubleSettings.get(key).getValue());
				bw.newLine();
			}
			bw.write("---");
			bw.newLine();
			
			for (String key : integerSettings.keySet()) {
				bw.write(key + ": " + integerSettings.get(key).getValue());
				bw.newLine();
			}
			bw.write("---");
			bw.newLine();
			
			for (String key : booleanSettings.keySet()) {
				bw.write(key + ": " + booleanSettings.get(key).getValue());
				bw.newLine();
			}
			
			bw.write("---");
			bw.newLine();
			
			for (String key : keySettings.keySet()) {
				bw.write(key + ": " + keySettings.get(key).getName());
				bw.newLine();
			}
			
			bw.write("---");
			bw.newLine();
			
			for (String key : sliderSettings.keySet()) {
				bw.write(key + ": " + sliderSettings.get(key).getValue());
				bw.newLine();
			}
			
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error saving settings file!");
		}
	}

	/**
	 * @return
	 * 		all the double settings stored.
	 */
	public Set<String> getDoubleSettings() {
		return doubleSettings.keySet();
	}
	
	/**
	 * @return
	 * 		all the integer settings stored.
	 */
	public Set<String> getIntegerSettings() {
		return integerSettings.keySet();
	}
	
	/**
	 * @return
	 * 		all the boolean settings stored.
	 */
	public Set<String> getBooleanSettings() {
		return booleanSettings.keySet();
	}
	
	/**
	 * @return
	 * 		all the key settings stored.
	 */
	public Set<String> getKeySettings() {
		return keySettings.keySet();
	}

	/**
	 * Get the description of a setting.
	 * @param key
	 * 		the setting.
	 * @return
	 * 		the description of the setting, or 'No description available' when not found.
	 */
	public String getDescription(String key) {
		String res = descriptions.get(key);
		if (res == null) {
			return "No description available";
		}
		return res;
	}
}
