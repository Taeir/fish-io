package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.image.Image;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fishio.gui.SlimGuiTest;
import com.github.fishio.logging.ConsoleHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

/**
 * Test for the {@link Preloader} class.
 */
public class TestPreloader extends SlimGuiTest {
	private static HashMap<String, Image> images;
	private static HashMap<String, Scene> screens;
	private ConsoleHandler consoleHandler;
	
	/**
	 * Set the logLevel to trace and get the hashMaps containing loaded images and screens.
	 */
	@BeforeClass
	public static void beforeClass() {
		Log.getLogger().setLogLevel(LogLevel.TRACE);
		images = Preloader.getImages();
		screens = Preloader.getScreens();
	}
	
	/**
	 * Restore the logLoevel after the tests.
	 */
	@AfterClass
	public static void afterClass() {
		Log.getLogger().setLogLevel(LogLevel.fromInt(Settings.getInstance().getInteger("LOG_LEVEL")));
	}
	
	/**
	 * Before each test reset the consoleHandler and clear all loaded images and screens.
	 */
	@Before
	public  void before() {
		Log log = Log.getLogger();
		log.removeAllHandlers();
		consoleHandler = mock(ConsoleHandler.class);
		log.addHandler(consoleHandler);
		
		images.clear();
		screens.clear();
	}
	
	/**
	 * Test if preloading is logged.
	 */
	@Test
	public void testPreloadImagesLog() {
		Preloader.preloadImages();
		verify(consoleHandler).output(LogLevel.DEBUG, "[Preloader] Preloading images...");
	}
	
	/**
	 * Test the exception handling for loading a faulty image.
	 */
	@Test
	public void testTryLoadImagesException() {
		Preloader.tryPreLoad("banana");
		verify(consoleHandler).output(LogLevel.ERROR, "Error while trying to load image: banana");
	}
	
	/**
	 * Test if loading an exist.
	 */
	@Test
	public void testTryLoadImagesExisting() {
		Image image = new Image("AlphaDataTest.png");
		images.put("banana", image);
		Preloader.tryPreLoad("banana");
		assertEquals(image, images.get("banana")); // not overwritten		
	}
	
	/**
	 * Test the loading of an image.
	 */
	@Test
	public void testTryLoadImagesLoad() {
		Preloader.tryPreLoad("AlphaDataTest.png");
		assertNotNull(images.get("AlphaDataTest.png"));	
		assertEquals(3, images.get("AlphaDataTest.png").getWidth(), 1E-8);
	}
	
	/**
	 * Test getting an existing image.
	 */
	@Test
	public void testGetImageOrLoadGet() {
		Image image = new Image("AlphaDataTest.png");
		images.put("banana", image);
		assertEquals(image, Preloader.getImageOrLoad("banana"));	
	}
	
	/**
	 * Test the loading of a non existing image.
	 */
	@Test
	public void testGetImageOrLoadLoad() {
		Image image = Preloader.getImageOrLoad("AlphaDataTest.png");
		assertEquals(3, image.getWidth(), 1E-8);
	}
	
	/**
	 * Get an existing image and check if it returns the correct image.
	 */
	@Test
	public void testGetImageExisting() {
		Image image = new Image("AlphaDataTest.png");
		images.put("banana", image);
		Preloader.getImage("banana");
		assertEquals(image, images.get("banana"));		
	}
	
	/**
	 * Get not loaded image and check the logging and exception throwing.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetImageLoad() {
		Preloader.getImage("banana");
		verify(consoleHandler).output(LogLevel.ERROR, "No image loaded for banana!");
	}
}
