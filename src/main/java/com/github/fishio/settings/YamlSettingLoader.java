package com.github.fishio.settings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

/**
 * Class implementing the settingLoader interface.
 * This class reads the settings from settings.yml.
 */
public class YamlSettingLoader implements SettingLoader {
	
	private HashMap<String, SimpleDoubleProperty> doubleSettings;
	private HashMap<String, SimpleIntegerProperty> integerSettings;
	private HashMap<String, SimpleBooleanProperty> booleanSettings;
	private HashMap<String, KeyCode> keyCodeSettings;
	private HashMap<String, SimpleDoubleProperty> sliderSettings;
	
	private File settingsFile = new File("settings.yml");

	/**
	 * Constructor for the loader.
	 * Loads all the settings and stores them.
	 */
	public YamlSettingLoader() {
		// If file doesn't exists, then create it
		if (!settingsFile.exists()) {
			createSettingsFile(settingsFile);				
		} else {
			try (FileReader fr = new FileReader(settingsFile)) {
				doubleSettings = new HashMap<String, SimpleDoubleProperty>();
				integerSettings = new HashMap<String, SimpleIntegerProperty>();
				booleanSettings = new HashMap<String, SimpleBooleanProperty>();
				keyCodeSettings = new HashMap<String, KeyCode>();
				sliderSettings = new  HashMap<String, SimpleDoubleProperty>();
				
				YamlReader reader = new YamlReader(fr);
				
				loadDoubleSettings(reader);
				loadIntegerSettings(reader);
				loadBooleanSettings(reader);
				loadKeyCodeSettings(reader);
				loadSliderSettings(reader);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * create the missing file and get the defaults from the settings class.
	 * @param file
	 * 		the file to create.
	 */
	private void createSettingsFile(File file) {
		file.getAbsoluteFile().getParentFile().mkdirs();
		try {
			file.createNewFile();
			doubleSettings = Settings.getDefaultDoubleSettings();
			integerSettings = Settings.getDefaultIntegerSettings();
			booleanSettings = Settings.getDefaultBooleanSettings();
			keyCodeSettings = Settings.getDefaultKeyCodeSettings();
			sliderSettings = Settings.getDefaultSliderSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadDoubleSettings(YamlReader reader) throws IOException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (String key : map.keySet()) {
	    	double value = Double.valueOf(map.get(key));
	    	doubleSettings.put(key, new SimpleDoubleProperty(value));
	    }
	}
	
	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadIntegerSettings(YamlReader reader) throws IOException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (String key : map.keySet()) {
	    	int value = Integer.valueOf(map.get(key));
	    	integerSettings.put(key, new SimpleIntegerProperty(value));
	    }
	}
	
	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadBooleanSettings(YamlReader reader) throws IOException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (String key : map.keySet()) {
	    	boolean value = Boolean.valueOf(map.get(key));
	    	booleanSettings.put(key, new SimpleBooleanProperty(value));
	    }
	}
	
	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadKeyCodeSettings(YamlReader reader) throws IOException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (String key : map.keySet()) {
	    	KeyCode value = KeyCode.getKeyCode(map.get(key));
	    	keyCodeSettings.put(key, value);
	    }
	}
	
	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadSliderSettings(YamlReader reader) throws IOException {	    
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (String key : map.keySet()) {
	    	double value = Double.valueOf(map.get(key));
	    	sliderSettings.put(key, new SimpleDoubleProperty(value));
	    }		
	}

	@Override
	public HashMap<String, SimpleDoubleProperty> getDoubleSettings() {
		return doubleSettings;
	}

	@Override
	public HashMap<String, SimpleIntegerProperty> getIntegerSettings() {
		return integerSettings;
	}

	@Override
	public HashMap<String, SimpleBooleanProperty> getBooleanSettings() {
		return booleanSettings;
	}

	@Override
	public HashMap<String, KeyCode> getKeyCodeSettings() {
		return keyCodeSettings;
	}

	@Override
	public HashMap<String, SimpleDoubleProperty> getSliderSettings() {
		return sliderSettings;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getDescriptions() {
		try (FileReader fr = new FileReader("settingDescriptions.txt")) {
			return (HashMap<String, String>) (new YamlReader(fr)).read();
			
		} catch (IOException e) {
			Log.getLogger().log(LogLevel.ERROR, "Eror loading descriptions!");
			return new HashMap<String, String>();
		}
		
	}

	@Override
	public void setFileName(String name) {
		settingsFile = new File(name + ".yml");
	}
}
