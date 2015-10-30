package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fishio.control.ScreenController;
import com.github.fishio.control.TestController;
import com.github.fishio.gui.SlimGuiTest;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.test.util.TestUtil;

/**
 * Test for the {@link Preloader} class.
 */
public class TestPreloader extends SlimGuiTest {
	private static final String DEFAULT_NAME = "banana";
	private static final String DEFAULT_IMAGE = "AlphaDataTest.png";
	private static HashMap<String, Image> images;
	private static HashMap<String, Scene> screens;

	/**
	 * Set the logLevel to trace and get the hashMaps containing loaded images and screens.
	 */
	@BeforeClass
	public static void setUpClass() {
		TestUtil.setUpLoggerForTesting(LogLevel.TRACE);
		images = Preloader.getImages();
		screens = Preloader.getScreens();
	}

	/**
	 * Restore the logLevel after the tests.
	 */
	@AfterClass
	public static void tearDownClass() {
		//Restore the logger
		TestUtil.restoreLogger();
	}

	/**
	 * Before each test reset the consoleHandler and clear all loaded images and screens.
	 */
	@Before
	public  void setUp() {
		//Reset the logger
		TestUtil.resetMockHandler();

		images.clear();
		screens.clear();
	}

	/**
	 * Test if preloading is logged.
	 */
	@Test
	public void testPreloadImagesLog() {
		Preloader.preloadImages();
		verify(TestUtil.getMockHandler()).output(LogLevel.DEBUG, "[Preloader] Preloading images...");
	}

	/**
	 * Test the exception handling for loading a faulty image.
	 */
	@Test
	public void testTryLoadImagesException() {
		Preloader.tryPreLoad(DEFAULT_NAME);
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error while trying to load image: " + DEFAULT_NAME);
	}

	/**
	 * Test if loading an exist.
	 */
	@Test
	public void testTryLoadImagesExisting() {
		Image image = new Image(DEFAULT_IMAGE);
		images.put(DEFAULT_NAME, image);
		Preloader.tryPreLoad(DEFAULT_NAME);
		assertEquals(image, images.get(DEFAULT_NAME)); // not overwritten		
	}

	/**
	 * Test the loading of an image.
	 */
	@Test
	public void testTryLoadImagesLoad() {
		Preloader.tryPreLoad(DEFAULT_IMAGE);
		assertNotNull(images.get(DEFAULT_IMAGE));	
		assertEquals(3, images.get(DEFAULT_IMAGE).getWidth(), 1E-8);
	}

	/**
	 * Test getting an existing image.
	 */
	@Test
	public void testGetImageOrLoadGet() {
		Image image = new Image(DEFAULT_IMAGE);
		images.put(DEFAULT_NAME, image);
		assertEquals(image, Preloader.getImageOrLoad(DEFAULT_NAME));	
	}

	/**
	 * Test the loading of a non existing image.
	 */
	@Test
	public void testGetImageOrLoadLoad() {
		Image image = Preloader.getImageOrLoad(DEFAULT_IMAGE);
		assertEquals(3, image.getWidth(), 1E-8);
	}

	/**
	 * Get an existing image and check if it returns the correct image.
	 */
	@Test
	public void testGetImageExisting() {
		Image image = new Image(DEFAULT_IMAGE);
		images.put(DEFAULT_NAME, image);
		Preloader.getImage(DEFAULT_NAME);
		assertEquals(image, images.get(DEFAULT_NAME));		
	}

	/**
	 * Get not loaded image and check the logging and exception throwing.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetImageLoad() {
		Preloader.getImage(DEFAULT_NAME);
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "No image loaded for " + DEFAULT_NAME + "!");
	}

	/**
	 * Test for loading a screen that is arleady loaded.
	 */
	@Test
	public void testLoadScreenLoaded() {
		Scene exp = new Scene(new StackPane());
		screens.put(DEFAULT_NAME, exp);
		Scene res = Preloader.loadScreen(DEFAULT_NAME);
		assertEquals(exp, res);
	}

	/**
	 * Test if the controller for the screen gets loaded.
	 */
	@Test
	public void testLoadScreenLoadController() {
		Preloader.loadScreen("testScreen");
		verify(TestUtil.getMockHandler()).output(LogLevel.INFO, "Controller loaded");
	}
	
	/**
	 * Test error handling for missing controller.
	 */
	@Test
	public void testLoadScreenLoadNoController() {
		Preloader.loadScreen("testScreen_NoController");
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR,
				"Screen controller not found for testScreen_NoController");
	}
	
	/**
	 * Test error handling for screen with a compile error in the .fxml.
	 */
	@Test
	public void testLoadScreenLoadError() {
		Preloader.loadScreen("errorScreen");
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error loading screen errorScreen");
	}
	
	/**
	 * Test to check if the loaded screen is the correct screen.
	 */
	@Test
	public void testLoadScreenLoad() {
		Scene scene = Preloader.loadScreen("testScreen");
		assertTrue(scene.getRoot() instanceof StackPane);
		assertTrue(scene.getRoot().getChildrenUnmodifiable().isEmpty());
	}
	
	/**
	 * Test for loading a screen that is already loading.
	 */
	@Test
	public void testLoadScreenLoadWait() {
		MultiThreadedUtility.submitTask(() -> Preloader.loadScreen("testScreen"), false);
		Scene scene = Preloader.loadScreen("testScreen");
		assertEquals(screens.get("testScreen"), scene);
	}
	
	/**
	 * Test for getting the controller of a null.
	 */
	@Test
	public void testGetControllerNull() {
		assertNull(Preloader.getController(null));		
	}
	
	/**
	 * Test if getting the controller results in the set controller.
	 */
	@Test
	public void testGetController() {
		Scene scene = new Scene(new StackPane());
		ScreenController controller = new TestController();
		scene.getProperties().put("Controller", controller);
		assertEquals(controller, Preloader.getController(scene));		
	}
	
	/**
	 * Test for getting the controller of a class without a controller.
	 */
	@Test
	public void testGetControllerEmpty() {
		Scene scene = new Scene(new StackPane());
		assertNull(Preloader.getController(scene));	
	}
}
