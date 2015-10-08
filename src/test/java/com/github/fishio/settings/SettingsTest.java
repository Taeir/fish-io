package com.github.fishio.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;

import org.junit.AfterClass;
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
	public static void before() {
		File file = new File("settings.yml");
		if (file.exists()) {
			file.renameTo(new File(".settings.yml.tmp"));
		}
		instance = Settings.getInstance();
		instance.save();
	}
	
	/**
	 * restore the old setting file.
	 */
	@AfterClass
	public static void after() {
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

}
