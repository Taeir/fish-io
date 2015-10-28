package com.github.fishio.settings;

import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

/**
 * Interface for loading settings.
 */
public interface ISettingLoader {

	/**
	 * Load the double Settings.
	 * @return
	 * 		A HashMap containing the doubleSettings
	 */
	HashMap<String, SimpleDoubleProperty> getDoubleSettings();

	/**
	 * Load the integer Settings.
	 * @return
	 * 		A HashMap containing the integerSettings
	 */
	HashMap<String, SimpleIntegerProperty> getIntegerSettings();

	/**
	 * load the boolean settings.
	 * @return
	 * 		A HashMap containing the booleanSettings
	 */
	HashMap<String, SimpleBooleanProperty> getBooleanSettings();

	/**
	 * Load the KeyCode settings.
	 * @return 
	 * 		A HashMap containing the keyCodeSettings
	 */
	HashMap<String, KeyCode> getKeyCodeSettings();

	/**
	 * Load the slider settings.
	 * @return
	 * 		A HashMap containing the sliderSettings
	 */
	HashMap<String, SimpleDoubleProperty> getSliderSettings();
	
	/**
	 * Load the descriptions from the settingDescriptions.txt file.
	 * @return
	 * 		A HashMap containing all the descriptions.
	 */
	HashMap<String, String> getDescriptions();
}
