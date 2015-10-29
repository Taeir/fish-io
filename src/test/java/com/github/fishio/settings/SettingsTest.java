package com.github.fishio.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;

/**
 * Tests for the {@link Settings} class.
 * This class
 */
public class SettingsTest {
	private static final String NON_EXISTENT_SETTING = "NON_EXISTENT";
	private static final String BOOLEAN_TRUE_SETTING = "BOOLEAN_TRUE";
	private static final String BOOLEAN_FALSE_SETTING = "BOOLEAN_FALSE";
	private static final String DOUBLE_SETTING = "DOUBLE";
	private static final String INTEGER_SETTING = "INTEGER";
	private static final String SLIDER_SETTING = "SLIDER";
	private static final String KEY_SETTING = "KEY";
	private static final String DESCRIPTION_SETTING = "SETTING_TEST_DESCRIPTION";
	private static final String DESCRIPTION = "This is a description.";
	
	private static final String SETTINGS_FILE = "settings.yml";
	private static final double DELTA = 10E-8;
	
	private static Settings instance;
	
	/**
	 * Copy the settings file and load the default settings.
	 */
	@BeforeClass
	public static void setUpClass() {
		//Rename the settings file to a temp file
		File file = new File(SETTINGS_FILE);
		if (file.exists()) {
			file.renameTo(new File(".settings.yml.tmp"));
		}
		
		//Set the instance, load and save the settings
		instance = Settings.getInstance();
		instance.load();
		instance.save();
	}
	
	/**
	 * restore the old setting file.
	 */
	@AfterClass
	public static void tearDownClass() {
		//Delete the settings file
		File file = new File(SETTINGS_FILE);
		file.delete();
		
		//Restore the old settings
		File temp = new File(".settings.yml.tmp");
		temp.renameTo(file);
	}
	
	/**
	 * Reload settings.
	 */
	@Before
	public void setUp() {
		//Delete the settings file and load (this loads the defaults)
		new File(SETTINGS_FILE).delete();
		instance.load();
		
		//Add fake settings
		instance.setBoolean(BOOLEAN_TRUE_SETTING, true);
		instance.setBoolean(BOOLEAN_FALSE_SETTING, false);
		instance.setDouble(DOUBLE_SETTING, 3.8);
		instance.setInteger(INTEGER_SETTING, 6);
		instance.setKey(KEY_SETTING, KeyCode.ENTER);
		instance.setSlider(SLIDER_SETTING, 0.45);
		
		instance.setDescription(DESCRIPTION_SETTING, DESCRIPTION);
	}
	
	/**
	 * Resets the settings to defaults.
	 */
	private void resetSettings() {
		//Delete the settings file and load (this loads the defaults)
		new File(SETTINGS_FILE).delete();
		instance.load();
	}
	
	/**
	 * Test {@link Settings#getInstance()} to check if it returns the same instance.
	 */
	@Test
	public void testGetInstance() {
		assertSame(instance, Settings.getInstance());
	}
	
	/**
	 * Test for {link {@link Settings#getSliderProperty(String)} with non existing setting.
	 */
	@Test
	public void testGetSliderPropertyNull() {
		SimpleDoubleProperty property = instance.getSliderProperty(NON_EXISTENT_SETTING);
		assertEquals(Double.NaN, property.doubleValue(), DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#getSlider(String)} with existing setting.
	 */
	@Test
	public void testGetSliderValue() {
		double value = instance.getSlider(SLIDER_SETTING);
		assertEquals(0.45, value, DELTA);
	}	
	
	/**
	 * Test for {link {@link Settings#getDouble(String)} with non existing setting.
	 */
	@Test
	public void testGetDoubleNull() {
		double value = instance.getDouble(NON_EXISTENT_SETTING);
		assertEquals(Double.NaN, value, DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#getDouble(String)} with existing setting.
	 */
	@Test
	public void testGetDoubleValue() {
		double value = instance.getDouble(DOUBLE_SETTING);
		assertEquals(3.8, value, DELTA);
	}	
	
	/**
	 * Test for {link {@link Settings#getInteger(String)} with non existing setting.
	 */
	@Test
	public void testGetIntegerNull() {
		int value = instance.getInteger(NON_EXISTENT_SETTING);
		assertEquals(Integer.MIN_VALUE, value);
	}
	
	/**
	 * Test for {link {@link Settings#getInteger(String)} with existing setting.
	 */
	@Test
	public void testGetIntegerValue() {
		int value = instance.getInteger(INTEGER_SETTING);
		assertEquals(6, value);
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with non existing setting.
	 */
	@Test
	public void testGetBooleanNull() {
		assertFalse(instance.getBoolean(NON_EXISTENT_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with existing setting.
	 */
	@Test
	public void testGetBooleanValueFalse() {
		assertFalse(instance.getBoolean(BOOLEAN_FALSE_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with existing setting.
	 */
	@Test
	public void testGetBooleanValueTrue() {
		assertTrue(instance.getBoolean(BOOLEAN_TRUE_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#getKeyCode(String)} with non existing setting.
	 */
	@Test
	public void testGetKeyCodeNull() {
		KeyCode key = instance.getKeyCode(NON_EXISTENT_SETTING);
		assertNull(key);
	}
	
	/**
	 * Test for {link {@link Settings#getKeyCode(String)} with existing setting.
	 */
	@Test
	public void testGetKeyCodeValue() {
		KeyCode value = instance.getKeyCode(KEY_SETTING);
		assertEquals(KeyCode.ENTER, value);
	}
	
	/**
	 * Test for {link {@link Settings#getDescription(String)} with non existing setting.
	 */
	@Test
	public void testGetDescriptionNull() {
		String desc = instance.getDescription(NON_EXISTENT_SETTING);
		assertEquals("No description available.", desc);
	}
	
	/**
	 * Test for {link {@link Settings#getDescription(String)} with existing setting.
	 */
	@Test
	public void testGetDescriptionValue() {
		String desc = instance.getDescription(DESCRIPTION_SETTING);
		assertEquals(DESCRIPTION, desc);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultDoubleSettings()} and {@link Settings#getDoubleSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultDoubleSettingsKeySet() {
		resetSettings();
		Set<String> expected = Settings.getDefaultDoubleSettings().keySet();
		Set<String> actual = instance.getDoubleSettings();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultIntegerSettings()} and {@link Settings#getIntegerSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultIntegerSettingsKeySet() {
		resetSettings();
		Set<String> expected = Settings.getDefaultIntegerSettings().keySet();
		Set<String> actual = instance.getIntegerSettings();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultBooleanSettings()} and {@link Settings#getBooleanSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultBooleanSettingsKeySet() {
		resetSettings();
		Set<String> expected = Settings.getDefaultBooleanSettings().keySet();
		Set<String> actual = instance.getBooleanSettings();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultSliderSettings()} and {@link Settings#getSliderSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultSliderSettingsKeySet() {
		resetSettings();
		Set<String> expected = Settings.getDefaultSliderSettings().keySet();
		Set<String> actual = instance.getSliderSettings();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultKeyCodeSettings()} and {@link Settings#getKeyCodeSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultKeyCodeSettingsKeySet() {
		resetSettings();
		Set<String> expected = Settings.getDefaultKeyCodeSettings().keySet();
		Set<String> actual = instance.getKeySettings();
		assertEquals(expected, actual);
	}	
	
	/**
	 * Test for {link {@link Settings#setDouble(String, double)}.
	 */
	@Test
	public void testSetDouble() {
		instance.setDouble(DOUBLE_SETTING, 12345.6);
		assertEquals(12345.6, instance.getDouble(DOUBLE_SETTING), DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#setInteger(String, int)}.
	 */
	@Test
	public void testSetInteger() {
		instance.setInteger(INTEGER_SETTING, 12);
		assertEquals(12, instance.getInteger(INTEGER_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#setBoolean(String, boolean)}.
	 */
	@Test
	public void testSetBoolean() {
		instance.setBoolean(BOOLEAN_FALSE_SETTING, true);
		assertTrue(instance.getBoolean(BOOLEAN_FALSE_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#setKey(String, KeyCode)}.
	 */
	@Test
	public void testSetKeyCode() {
		instance.setKey(KEY_SETTING, KeyCode.SPACE);
		assertEquals(KeyCode.SPACE, instance.getKeyCode(KEY_SETTING));
	}
	
	/**
	 * Test for {link {@link Settings#setSlider(String, double)}.
	 */
	@Test
	public void testSetSlider() {
		instance.setSlider(SLIDER_SETTING, 0.001);
		assertEquals(0.001, instance.getSlider(SLIDER_SETTING), DELTA);
	}
	
	/**
	 * Test the {@link Settings#save()} and {@link Settings#load()} methods, to verify they work.
	 * First save, then alter some values and lastly reload and check values.
	 */
	@Test
	public void testSaveLoad() {
		double doubl = instance.getDouble(DOUBLE_SETTING);
		int in = instance.getInteger(INTEGER_SETTING);
		boolean bool = instance.getBoolean(BOOLEAN_FALSE_SETTING);
		double slider = instance.getSlider(SLIDER_SETTING);
		KeyCode key = instance.getKeyCode(KEY_SETTING);
		
		instance.save();
		
		instance.setDouble(DOUBLE_SETTING, doubl + 12.3);
		instance.setInteger(INTEGER_SETTING, in + 12);
		instance.setBoolean(BOOLEAN_FALSE_SETTING, !bool);
		instance.setSlider(SLIDER_SETTING, slider - 0.5);
		instance.setKey(KEY_SETTING, KeyCode.SPACE); // as space as a default would break the game.
		
		instance.load();
	
		assertEquals(doubl, instance.getDouble(DOUBLE_SETTING), DELTA);
		assertEquals(in, instance.getInteger(INTEGER_SETTING));
		assertEquals(bool, instance.getBoolean(BOOLEAN_FALSE_SETTING));
		assertEquals(slider, instance.getSlider(SLIDER_SETTING), DELTA);
		assertEquals(key, instance.getKeyCode(KEY_SETTING));
	}
}
