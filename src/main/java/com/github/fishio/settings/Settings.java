package com.github.fishio.settings;

import java.util.HashMap;
import java.util.Set;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Data class with all the settings.
 * 
 * When the game stops working after an addition to the settings, simply remove the settings.yml file
 */
public final class Settings {
	private static final Settings INSTANCE = new Settings();
	
	private Log log = Log.getLogger();
	private HashMap<String, SimpleDoubleProperty> doubleSettings = new HashMap<String, SimpleDoubleProperty>();
	private HashMap<String, SimpleIntegerProperty> integerSettings = new HashMap<String, SimpleIntegerProperty>();
	private HashMap<String, SimpleBooleanProperty> booleanSettings = new HashMap<String, SimpleBooleanProperty>();
	private HashMap<String, SimpleDoubleProperty> sliderSettings = new HashMap<String, SimpleDoubleProperty>();
	private HashMap<String, KeyCode> keySettings = new HashMap<String, KeyCode>();
	
	private HashMap<String, String> descriptions = new HashMap<String, String>();
	
	private Settings() {		
		load();
	}
	
	/**
	 * Load all the settings.
	 */
	protected void load() {
		ISettingLoader parser = new YamlSettingLoader();
		doubleSettings = parser.getDoubleSettings();
		integerSettings = parser.getIntegerSettings();
		booleanSettings = parser.getBooleanSettings();
		keySettings = parser.getKeyCodeSettings();
		sliderSettings = parser.getSliderSettings();
		
		descriptions = parser.getDescriptions();
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
	 * 		The value of the setting or false if the setting is not found.
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
	 * Saves the current settings to a file.
	 */
	public void save() {
		ISettingWriter writer = new YamlSettingWriter();
		writer.writeDoubleSettings(doubleSettings);
		writer.writeIntegerSettings(integerSettings);
		writer.writeBooleanSettings(booleanSettings);
		writer.writeKeyCodeSettings(keySettings);
		writer.writeSliderSettings(sliderSettings);
		writer.flush();
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
			return "No description available.";
		}
		return res;
	}

	/**
	 * @return
	 * 		all the slider settings stored.
	 */
	public Set<String> getSliderSettings() {
		return sliderSettings.keySet();
	}

	/**
	 * @return
	 * 		All the default values for the settings.
	 */
	public static HashMap<String, SimpleDoubleProperty> getDefaultDoubleSettings() {
		HashMap<String, SimpleDoubleProperty> map = new HashMap<String, SimpleDoubleProperty>();
		 map.put("SCREEN_WIDTH", new SimpleDoubleProperty(1280));
		 map.put("SCREEN_HEIGHT", new SimpleDoubleProperty(720));
		 map.put("FISH_EAT_THRESHOLD", new SimpleDoubleProperty(1.2));
		 map.put("DIRECTION_CHANGE_CHANCE", new SimpleDoubleProperty(0.1));
		 map.put("MIN_EFISH_SPEED", new SimpleDoubleProperty(1));
		 map.put("MAX_EFISH_SPEED", new SimpleDoubleProperty(4));
		 map.put("MAX_PLAYER_SPEED", new SimpleDoubleProperty(4.0));
		return map;
	}

	/**
	 * @return
	 * 		All the default values for the settings.
	 */
	public static HashMap<String, SimpleIntegerProperty> getDefaultIntegerSettings() {
		HashMap<String, SimpleIntegerProperty> map = new HashMap<String, SimpleIntegerProperty>();
		 map.put("GROWTH_SPEED", new SimpleIntegerProperty(2));
		 map.put("LOG_LEVEL", new SimpleIntegerProperty(2));
		 map.put("START_LIVES", new SimpleIntegerProperty(3));
		 map.put("MAX_LIVES", new SimpleIntegerProperty(5));
		 map.put("POWERUP_SPAWN_INTERVAL", new SimpleIntegerProperty(30));
		return map;
	}

	/**
	 * @return
	 * 		All the default values for the settings.
	 */
	public static HashMap<String, SimpleBooleanProperty> getDefaultBooleanSettings() {
		HashMap<String, SimpleBooleanProperty> map = new HashMap<String, SimpleBooleanProperty>();
		 map.put("DEBUG_DRAW", new SimpleBooleanProperty(false));
		 map.put("PIXEL_PERFECT_COLLISIONS", new SimpleBooleanProperty(true));
		return map;
	}

	/**
	 * @return
	 * 		All the default values for the settings.
	 */
	public static HashMap<String, KeyCode> getDefaultKeyCodeSettings() {
		HashMap<String, KeyCode> map = new HashMap<String, KeyCode>();
		 map.put("SWIM_UP", KeyCode.UP);
		 map.put("SWIM_DOWN", KeyCode.DOWN);
		 map.put("SWIM_LEFT", KeyCode.LEFT);
		 map.put("SWIM_RIGHT", KeyCode.RIGHT);
		return map;
	}

	/**
	 * @return
	 * 		All the default values for the settings.
	 */
	public static HashMap<String, SimpleDoubleProperty> getDefaultSliderSettings() {
		HashMap<String, SimpleDoubleProperty> map = new HashMap<String, SimpleDoubleProperty>();
		 map.put("MASTER_VOLUME", new SimpleDoubleProperty(1.0));
		 map.put("MUSIC_VOLUME", new SimpleDoubleProperty(0.8));
		 map.put("EFFECTS_VOLUME", new SimpleDoubleProperty(1.0));
		return map;
	}
}
