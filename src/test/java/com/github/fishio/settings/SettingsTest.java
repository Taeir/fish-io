package com.github.fishio.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the {@link Settings} class.
 * This class
 */
public class SettingsTest {

	private static Settings instance;
	private static final double DELTA = 10E-8;
	
	/**
	 * Copy the settings file and load the default settings.
	 */
	@BeforeClass
	public static void beforeClass() {
		File file = new File("settings.yml");
		if (file.exists()) {
			file.renameTo(new File(".settings.yml.tmp"));
		}
		instance = Settings.getInstance();
		instance.save();
	}
	
	/**
	 * Reload settings.
	 */
	@Before
	public void before() {
		new File("settings.yml").delete();
		instance.load();
	}
	
	/**
	 * restore the old setting file.
	 */
	@AfterClass
	public static void afterClass() {
		File file = new File("settings.yml");
		File temp = new File(".settings.yml.tmp");
		file.delete();
		temp.renameTo(file);
		temp.delete();
	}
	
	/**
	 * Test {@link Settings#getInstance()} to check if it returns the same instance.
	 */
	@Test
	public void testGetInstance() {
		assertTrue(instance == Settings.getInstance());
	}
	
	/**
	 * Test for {link {@link Settings#getSliderProperty(String)} with non existing setting.
	 */
	@Test
	public void testGetSliderPropertyNull() {
		SimpleDoubleProperty property = instance.getSliderProperty("NON_EXISTENT");
		assertEquals(Double.NaN, property.doubleValue(), DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#getSlider(String)} with existing setting.
	 */
	@Test
	public void testGetSliderValue() {
		double value = instance.getSlider("MUSIC_VOLUME");
		assertEquals(0.8, value, DELTA);
	}	
	
	/**
	 * Test for {link {@link Settings#getDouble(String)} with non existing setting.
	 */
	@Test
	public void testGetDoubleNull() {
		double value = instance.getDouble("NON_EXISTENT");
		assertEquals(Double.NaN, value, DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#getDouble(String)} with existing setting.
	 */
	@Test
	public void testGetDoubleValue() {
		double value = instance.getDouble("FISH_EAT_THRESHOLD");
		assertEquals(1.2, value, DELTA);
	}	
	
	/**
	 * Test for {link {@link Settings#getInteger(String)} with non existing setting.
	 */
	@Test
	public void testGetIntegerNull() {
		int value = instance.getInteger("NON_EXISTENT");
		assertEquals(Integer.MIN_VALUE, value);
	}
	
	/**
	 * Test for {link {@link Settings#getInteger(String)} with existing setting.
	 */
	@Test
	public void testGetIntegerValue() {
		int value = instance.getInteger("MAX_LIVES");
		assertEquals(5, value);
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with non existing setting.
	 */
	@Test
	public void testGetBooleanNull() {
		assertFalse(instance.getBoolean("NON_EXISTENT"));
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with existing setting.
	 */
	@Test
	public void testGetBooleanValueFalse() {
		assertFalse(instance.getBoolean("DEBUG_DRAW"));
	}
	
	/**
	 * Test for {link {@link Settings#getBoolean(String)} with existing setting.
	 */
	@Test
	public void testGetBooleanValueTrue() {
		assertTrue(instance.getBoolean("PIXEL_PERFECT_COLLISIONS"));
	}
	
	/**
	 * Test for {link {@link Settings#getKeyCode(String)} with non existing setting.
	 */
	@Test
	public void testGetKeyCodeNull() {
		KeyCode key = instance.getKeyCode("NON_EXISTENT");
		assertNull(key);
	}
	
	/**
	 * Test for {link {@link Settings#getKeyCode(String)} with existing setting.
	 */
	@Test
	public void testGetKeyCodeValue() {
		KeyCode value = instance.getKeyCode("SWIM_UP");
		assertEquals(KeyCode.UP, value);
	}
	
	/**
	 * Test for {link {@link Settings#getDescription(String)} with non existing setting.
	 */
	@Test
	public void testGetDescritionNull() {
		String desc = instance.getDescription("NON_EXISTENT");
		assertEquals("No description available.", desc);
	}
	
	/**
	 * Test for {link {@link Settings#getDescription(String)} with existing setting.
	 */
	@Test
	public void testGetDescriptionValue() {
		String desc = instance.getDescription("DEBUG_DRAW");
		assertEquals("Render debug values.", desc);
	}
	
	/**
	 * Test for {link {@link Settings#getDefaultDoubleSettings()} and {@link Settings#getDoubleSettings()}.
	 * Checks if the loaded list is equal to the default list.
	 */
	@Test
	public void testDefaultDoubleSettingsKeySet() {
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
		Set<String> expected = Settings.getDefaultKeyCodeSettings().keySet();
		Set<String> actual = instance.getKeySettings();
		assertEquals(expected, actual);
	}	
	
	/**
	 * Test for {link {@link Settings#setDouble(String, double)}.
	 */
	@Test
	public void testSetDouble() {
		instance.setDouble("FISH_EAT_THRESHOLD", 12345.6);
		assertEquals(12345.6, instance.getDouble("FISH_EAT_THRESHOLD"), DELTA);
	}
	
	/**
	 * Test for {link {@link Settings#setInteger(String, int)}.
	 */
	@Test
	public void testSetInteger() {
		instance.setInteger("MAX_LIVES", 12);
		assertEquals(12, instance.getInteger("MAX_LIVES"));
	}
	
	/**
	 * Test for {link {@link Settings#setBoolean(String, boolean)}.
	 */
	@Test
	public void testSetBoolean() {
		instance.setBoolean("DEBUG_DRAW", true);
		assertTrue(instance.getBoolean("DEBUG_DRAW"));
	}
	
	/**
	 * Test for {link {@link Settings#setKey(String, KeyCode)}.
	 */
	@Test
	public void testSetKeyCode() {
		instance.setKey("SWIM_UP", KeyCode.SPACE);
		assertEquals(KeyCode.SPACE, instance.getKeyCode("SWIM_UP"));
	}
	
	/**
	 * Test for {link {@link Settings#setSlider(String, double)}.
	 */
	@Test
	public void testSetSlider() {
		instance.setSlider("MASTER_VOLUME", 0.001);
		assertEquals(0.001, instance.getSlider("MASTER_VOLUME"), DELTA);
	}
	
	/**
	 * Test the {@link Settings#save()} and {@link Settings#load()} methods, to verify they work.
	 * First save, then alter some values and lastly reload and check values.
	 */
	@Test
	public void testSaveLoad() {
		double doubl = instance.getDouble("FISH_EAT_THRESHOLD");
		int in = instance.getInteger("MAX_LIVES");
		boolean bool = instance.getBoolean("DEBUG_DRAW");
		double slider = instance.getSlider("MASTER_VOLUME");
		KeyCode key = instance.getKeyCode("SWIM_UP");
		
		instance.save();
		
		instance.setDouble("FISH_EAT_THRESHOLD", doubl + 12.3);
		instance.setInteger("MAX_LIVES", in + 12);
		instance.setBoolean("DEBUG_DRAW", !bool);
		instance.setSlider("MASTER_VOLUME", slider - 0.5);
		instance.setKey("SWIM_UP", KeyCode.SPACE); // as space as a default would break the game.
		
		instance.load();
	
		assertEquals(doubl, instance.getDouble("FISH_EAT_THRESHOLD"), DELTA);
		assertEquals(in, instance.getInteger("MAX_LIVES"));
		assertEquals(bool, instance.getBoolean("DEBUG_DRAW"));
		assertEquals(slider, instance.getSlider("MASTER_VOLUME"), DELTA);
		assertEquals(key, instance.getKeyCode("SWIM_UP"));
	}
}
