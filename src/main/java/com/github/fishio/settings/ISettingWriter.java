package com.github.fishio.settings;

import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

/**
 * Interface for storing Setting values.
 */
interface ISettingWriter {
	
	/**
	 * Write all the double settings in the input to a file.
	 * @param map
	 * 		The HashMap containing all the settings.
	 */
	void writeDoubleSettings(HashMap<String, SimpleDoubleProperty> map);
	
	/**
	 * Write all the integer settings to a file.
	 * @param map
	 * 		The HashMap containing all the settings.
	 */
	void writeIntegerSettings(HashMap<String, SimpleIntegerProperty> map);
	
	/**
	 * Write all the boolean settings to a file.
	 * @param map
	 * 		The HashMap containing all the settings.
	 */
	void writeBooleanSettings(HashMap<String, SimpleBooleanProperty> map);
	
	/**
	 * Write all the keyCode settings to a file.
	 * @param map
	 * 		The HashMap containing all the settings.
	 */
	void writeKeyCodeSettings(HashMap<String, KeyCode> map);
	
	/**
	 * Write the slider settings to a file.
	 * @param map
	 * 		The HashMap containing all the settings.
	 */
	void writeSliderSettings(HashMap<String, SimpleDoubleProperty> map);
	
	/**
	 * Flush the stored settings.
	 * For some implementations it it might not be needed to flush.
	 */
	void flush();
}