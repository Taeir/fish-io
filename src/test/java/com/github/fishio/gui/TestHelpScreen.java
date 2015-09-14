package com.github.fishio.gui;

import static org.junit.Assert.*;
import javafx.scene.input.MouseButton;

import org.junit.Test;

/**
 * Test class for the HelpScreen.
 */
public class TestHelpScreen extends GuiTest {

	/**
	 * Test back to main menu button.
	 */
	@Test
	public void test() {
		//Skip the splash screen.
		skipSplash();
		
		//Switch to the single player screen
		switchToScreen("helpScreen");
		
		//Check that we are on the single player screen.
		assertTrue(isCurrentScene(getHelpScene()));
		
		clickOn(getHelpScreenController().getBtnBackToMenu(), MouseButton.PRIMARY);
		
		//We should now be on the single player screen.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}

}
