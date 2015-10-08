package com.github.fishio.settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

/**
 * A class implementing the SettingWriter interface.
 * Writes settings to the file settings.yml
 * This implementation of the interface needs a flush() call to work.
 */
public class YamlSettingWriter implements SettingWriter {
	
	private HashMap<String, SimpleDoubleProperty> doubleSettings;
	private HashMap<String, SimpleIntegerProperty> integerSettings;
	private HashMap<String, SimpleBooleanProperty> booleanSettings;
	private HashMap<String, KeyCode> keyCodeSettings;
	private HashMap<String, SimpleDoubleProperty> sliderSettings;
	private File settingsFile = new File("settings.yml");
	
	/**
	 * Constructor for the YamlSettingWriter.
	 */
	public YamlSettingWriter() {
		doubleSettings = new HashMap<String, SimpleDoubleProperty>();
		integerSettings = new HashMap<String, SimpleIntegerProperty>();
		booleanSettings = new HashMap<String, SimpleBooleanProperty>();
		keyCodeSettings = new HashMap<String, KeyCode>();
		sliderSettings = new HashMap<String, SimpleDoubleProperty>();
	}
	
	@Override
	public void writeDoubleSettings(HashMap<String, SimpleDoubleProperty> map) {
		doubleSettings = map;
	}
	
	@Override
	public void writeIntegerSettings(HashMap<String, SimpleIntegerProperty> map) {
		integerSettings = map;
	}
	
	@Override
	public void writeBooleanSettings(HashMap<String, SimpleBooleanProperty> map) {
		booleanSettings = map;
	}
	
	@Override
	public void writeKeyCodeSettings(HashMap<String, KeyCode> map) {
		keyCodeSettings = map;
	}
	
	@Override
	public void writeSliderSettings(HashMap<String, SimpleDoubleProperty> map) {
		sliderSettings = map;
	}
	
	@Override
	public void flush() {
		if (!settingsFile.exists()) {
			settingsFile.getAbsoluteFile().getParentFile().mkdirs();
			try {
				settingsFile.createNewFile();
			} catch (IOException e) {
				Log.getLogger().log(LogLevel.ERROR, "Error creating settings file!");
			}			
		} else {
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
				
				for (String key : keyCodeSettings.keySet()) {
					bw.write(key + ": " + keyCodeSettings.get(key).getName());
					bw.newLine();
				}
				
				bw.write("---");
				bw.newLine();
				
				for (String key : sliderSettings.keySet()) {
					bw.write(key + ": " + sliderSettings.get(key).getValue());
					bw.newLine();
				}
				
			} catch (IOException e) {
				Log.getLogger().log(LogLevel.ERROR, "Error saving settings file!");
			}
		}
	}

	@Override
	public void setFileName(String name) {
		settingsFile = new File(name + ".yml");
	}
}
