package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;
import javafx.scene.input.KeyCode;

import org.junit.Test;

/**
 * Test class for the Splash Screen.
 */
public class TestSplashScreen extends GuiTest {
	/**
	 * Test for the following scenario, with key SPACE.<br>
	 * <br>
	 * Scenario S1.1: Splash Skip 1<br>
	 * Given the user has launched the Fish.IO GUI,<br>
	 * and   the splash screen is shown;<br>
	 * When  the user presses any key;<br>
	 * Then  the main menu screen should be shown.
	 */
	@Test
	public void testSkipSplash1() {
		//The splash screen should be shown.
		assertTrue(isCurrentScene(getSplashScene()));
		
		//Skip the splash screen
		press(KeyCode.SPACE);
		
		//Now the main menu should be shown.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}
	
	/**
	 * Test for the following scenario, with key K.<br>
	 * <br>
	 * Scenario S1.2: Splash Skip 2<br>
	 * Given the user has launched the Fish.IO GUI,<br>
	 * and   the splash screen is shown;<br>
	 * When  the user presses any key;<br>
	 * Then  the main menu screen should be shown.
	 */
	@Test
	public void testSkipSplash2() {
		//The splash screen should be shown.
		assertTrue(isCurrentScene(getSplashScene()));
		
		//Skip the splash screen
		press(KeyCode.K);
		
		//Now the main menu should be shown.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}
	
	/**
	 * Test for the following scenario, with key ENTER.<br>
	 * <br>
	 * Scenario S1.3: Splash Skip 3<br>
	 * Given the user has launched the Fish.IO GUI,<br>
	 * and   the splash screen is shown;<br>
	 * When  the user presses any key;<br>
	 * Then  the main menu screen should be shown.
	 */
	@Test
	public void testSkipSplash3() {
		//The splash screen should be shown.
		assertTrue(isCurrentScene(getSplashScene()));
		
		//Skip the splash screen
		press(KeyCode.ENTER);
		
		//Now the main menu should be shown.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}
	
	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S1.4: Wait for Splash<br>
	 * Given the user has launched the Fish.IO GUI,<br>
	 * and   the splash screen is shown;<br>
	 * When  the user waits for 15 seconds;<br>
	 * Then  the main menu screen should be shown.
	 */
	@Test
	public void testWaitSplash() {
		//The splash screen should be shown.
		assertTrue(isCurrentScene(getSplashScene()));
		
		//Wait for 15 seconds
		sleepFail(15_000L);
		
		//Now the main menu should be shown.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}
}
