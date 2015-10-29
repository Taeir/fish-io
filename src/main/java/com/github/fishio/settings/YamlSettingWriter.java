package com.github.fishio.settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

import com.esotericsoftware.yamlbeans.YamlWriter;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * A class implementing the SettingWriter interface.
 * Writes settings to the file settings.yml
 * This implementation of the interface needs a flush() call to work.
 */
public class YamlSettingWriter implements ISettingWriter {
	
	private HashMap<String, Double> doubleSettings;
	private HashMap<String, Integer> integerSettings;
	private HashMap<String, Boolean> booleanSettings;
	private HashMap<String, String> keyCodeSettings;
	private HashMap<String, Double> sliderSettings;
	private File settingsFile = new File("settings.yml");
	
	/**
	 * Constructor for the YamlSettingWriter.
	 */
	public YamlSettingWriter() {
		doubleSettings = new HashMap<String, Double>();
		integerSettings = new HashMap<String, Integer>();
		booleanSettings = new HashMap<String, Boolean>();
		keyCodeSettings = new HashMap<String, String>();
		sliderSettings = new HashMap<String, Double>();
	}
	
	@Override
	public void writeDoubleSettings(HashMap<String, SimpleDoubleProperty> map) {
		HashMap<String, Double> output = new HashMap<>(map.size());
		for (Entry<String, SimpleDoubleProperty> e : map.entrySet()) {
			output.put(e.getKey(), e.getValue().doubleValue());
		}
		
		doubleSettings = output;
	}
	
	@Override
	public void writeIntegerSettings(HashMap<String, SimpleIntegerProperty> map) {
		HashMap<String, Integer> output = new HashMap<>(map.size());
		for (Entry<String, SimpleIntegerProperty> e : map.entrySet()) {
			output.put(e.getKey(), e.getValue().intValue());
		}
		
		integerSettings = output;
	}
	
	@Override
	public void writeBooleanSettings(HashMap<String, SimpleBooleanProperty> map) {
		HashMap<String, Boolean> output = new HashMap<>(map.size());
		for (Entry<String, SimpleBooleanProperty> e : map.entrySet()) {
			output.put(e.getKey(), e.getValue().getValue());
		}
		
		booleanSettings = output;
	}
	
	@Override
	public void writeKeyCodeSettings(HashMap<String, KeyCode> map) {
		HashMap<String, String> output = new HashMap<>(map.size());
		for (Entry<String, KeyCode> e : map.entrySet()) {
			output.put(e.getKey(), e.getValue().getName());
		}
		
		keyCodeSettings = output;
	}
	
	@Override
	public void writeSliderSettings(HashMap<String, SimpleDoubleProperty> map) {
		HashMap<String, Double> output = new HashMap<>(map.size());
		for (Entry<String, SimpleDoubleProperty> e : map.entrySet()) {
			output.put(e.getKey(), e.getValue().doubleValue());
		}
		
		sliderSettings = output;
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
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile))) {
			YamlWriter yw = new YamlWriter(bw);
			
			yw.write(doubleSettings);
			yw.write(integerSettings);
			yw.write(booleanSettings);
			yw.write(keyCodeSettings);
			yw.write(sliderSettings);
			yw.close();
		} catch (IOException e) {
			Log.getLogger().log(LogLevel.ERROR, "Error saving settings file!");
		}
	}
}
