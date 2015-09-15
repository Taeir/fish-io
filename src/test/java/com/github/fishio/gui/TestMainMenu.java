package com.github.fishio.gui;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.input.MouseButton;

/**
 * Test class for the Main Menu.
 */
public class TestMainMenu extends GuiTest {
	
	/**
	 * Runs before all the tests.<br>
	 * <br>
	 * This method simply skips the splash screen, so that we are on the
	 * main menu before every test.
	 */
	@Before
	public void beforeMainMenu() {
		//Skip the splash screen
		skipSplash();
	}
	
	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.1: Singleplayer<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Singleplayer" button;<br>
	 * Then  the singleplayer screen should be shown,<br>
	 * and   a single player game should start.
	 */
	@Test
	public void testStartSinglePlayerGame() {
		//Click on the single player button
		clickOn(getMainMenuController().getBtnSingleplayer(), MouseButton.PRIMARY);
		
		//We should now be on the single player screen.
		assertCurrentScene(getSinglePlayerScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.2: Multiplayer<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Multiplayer" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testStartMultiplayer() {
		//Click on the multi player button
		clickOn(getMainMenuController().getBtnMultiplayer(), MouseButton.PRIMARY);
		
		//We should still be on the multi player screen, since multiplayer is disabled.
		assertCurrentScene(getMainMenuScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.3: Load Level<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Load Level" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testLoadLevel() {
		//Click on the load level button
		clickOn(getMainMenuController().getBtnLoadLevel(), MouseButton.PRIMARY);
		
		//We should still be on the multi player screen, since the load level button is disabled.
		assertCurrentScene(getMainMenuScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.4: Highscores<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "High Scores" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testShowHighScores() {
		//Click on the highscores button
		clickOn(getMainMenuController().getBtnShowHighscores(), MouseButton.PRIMARY);
		
		//We should still be on the multi player screen, since the show highscores button is disabled.
		assertCurrentScene(getMainMenuScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.5: Achievements<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When the user presses the "Achievements" button;<br>
	 * Then the Achievement screen must be shown.
	 */
	@Test
	public void testShowAchievements() {
		//Click on the achievements button
		clickOn(getMainMenuController().getBtnShowAchievements(), MouseButton.PRIMARY);
		
		// We should now be on the achievement screen.
		assertCurrentScene(getAchievementScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.6: Statistics<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Statistics" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testShowStatistics() {
		//Click on the statistics button
		clickOn(getMainMenuController().getBtnShowStatistics(), MouseButton.PRIMARY);
		
		//We should still be on the multi player screen, since the show statistics button is disabled.
		assertCurrentScene(getMainMenuScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.7: Settings<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Settings" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testShowSettings() {
		//Click on the settings button
		clickOn(getMainMenuController().getBtnShowSettings(), MouseButton.PRIMARY);
		
		//We should still be on the multi player screen, since the settings button is disabled.
		assertCurrentScene(getMainMenuScene());
	}

	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S2.8: Help<br>
	 * Given the user is on the Main Menu Screen;<br>
	 * When  the user presses the "Help" button;<br>
	 * Then  the helpScreen must be shown.
	 */
	@Test
	public void testShowHelp() {
		//Click on the help button
		clickOn(getMainMenuController().getBtnShowHelp(), MouseButton.PRIMARY);
		
		// We should now be on the help screen.
		assertCurrentScene(getHelpScene());
	}
}
