package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;

import javafx.scene.input.MouseButton;

import org.junit.Test;

/**
 * Test class for the HelpScreen.
 */
public class TestHelpScreen extends GuiTest {
	
	/**
	 * Creates the help screen GUI test, with the help screen
	 * as the start screen.
	 */
	public TestHelpScreen() {
		super("helpScreen");
	}

	/**
	 * Test back to main menu button.
	 */
	@Test
	public void test() {
		//Click on the back to menu button.
		clickOn(getHelpScreenController().getBtnBackToMenu(), MouseButton.PRIMARY);
		
		//We should now be on the main menu screen.
		assertTrue(isCurrentScene(getMainMenuScene()));
	}

}
