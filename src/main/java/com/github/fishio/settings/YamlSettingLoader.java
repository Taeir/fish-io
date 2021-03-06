package com.github.fishio.settings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

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
@SuppressWarnings("unchecked")
public class YamlSettingLoader implements ISettingLoader {
	
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
			createSettingsFile();				
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
	private void createSettingsFile() {
		doubleSettings = Settings.getDefaultDoubleSettings();
		integerSettings = Settings.getDefaultIntegerSettings();
		booleanSettings = Settings.getDefaultBooleanSettings();
		keyCodeSettings = Settings.getDefaultKeyCodeSettings();
		sliderSettings = Settings.getDefaultSliderSettings();
		
		YamlSettingWriter writer = new YamlSettingWriter();
		writer.writeBooleanSettings(booleanSettings);
		writer.writeDoubleSettings(doubleSettings);
		writer.writeIntegerSettings(integerSettings);
		writer.writeKeyCodeSettings(keyCodeSettings);
		writer.writeSliderSettings(sliderSettings);
		writer.flush();
	}

	/**
	 * Read the settings from file.
	 * @param reader
	 * 		the YamlReader to read with.
	 * @throws IOException
	 * 		when something goes wrong when reading the file.
	 */
	private void loadDoubleSettings(YamlReader reader) throws IOException {
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (Entry<String, String> entry : map.entrySet()) {
	    	double value = Double.parseDouble(entry.getValue());
	    	doubleSettings.put(entry.getKey(), new SimpleDoubleProperty(value));
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
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (Entry<String, String> entry : map.entrySet()) {
	    	int value = Integer.parseInt(entry.getValue());
	    	integerSettings.put(entry.getKey(), new SimpleIntegerProperty(value));
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
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (Entry<String, String> entry : map.entrySet()) {
	    	boolean value = Boolean.parseBoolean(entry.getValue());
	    	booleanSettings.put(entry.getKey(), new SimpleBooleanProperty(value));
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
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (Entry<String, String> entry : map.entrySet()) {
	    	KeyCode value = KeyCode.getKeyCode(entry.getValue());
	    	keyCodeSettings.put(entry.getKey(), value);
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
		HashMap<String, String> map = (HashMap<String, String>) reader.read();
	    for (Entry<String, String> entry : map.entrySet()) {
	    	double value = Double.parseDouble(entry.getValue());
	    	sliderSettings.put(entry.getKey(), new SimpleDoubleProperty(value));
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
	public HashMap<String, String> getDescriptions() {
		try (FileReader fr = new FileReader("settingDescriptions.txt")) {
			return (HashMap<String, String>) (new YamlReader(fr)).read();
			
		} catch (IOException e) {
			Log.getLogger().log(LogLevel.ERROR, "Eror loading descriptions!");
			return new HashMap<String, String>();
		}
		
	}
}
