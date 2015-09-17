package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.MouseButton;

/**
 * Test class for the AchievementScreen.
 */
public class TestAchievementScreen extends GuiTest {
	
	/**
	 * Creates the help screen GUI test, with the achievement screen as the
	 * start screen.
	 */
	public TestAchievementScreen() {
		super("achievementScreen");
	}

	/**
	 * Test back to main menu button.
	 */
	@Test
	public void test() {
		// Click on the back to menu button.
		clickOn(getAchievementScreenController().getBtnBackToMenu(), MouseButton.PRIMARY);
		
		// We should now be on the main menu screen.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}
	
}
