package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.MouseButton;

/**
 * Test class for the AchievementScreen
 */
public class TestAchievementScreen extends GuiTest {

	/**
	 * Test back to main menu button.
	 */
	@Test
	public void test() {
		//Skip the splash screen.
		skipSplash();
		
		//Switch to the single player screen
		switchToScreen("AchievementScene");
		
		//Check that we are on the single player screen.
		assertTrue(isCurrentScene(getHelpScene()));
		
		clickOn(getAchievementScreenController().getBtnBackToMenu(), MouseButton.PRIMARY);
		
		//We should now be on the single player screen.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}

}
