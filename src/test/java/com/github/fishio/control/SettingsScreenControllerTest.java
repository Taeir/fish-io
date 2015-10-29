/**
 * 
 */
package com.github.fishio.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.gui.NoSoundSlimGuiTest;
import com.github.fishio.settings.Settings;

/**
 * Test class for {@link SettingsScreenController}.
 */
public class SettingsScreenControllerTest extends NoSoundSlimGuiTest {
	private SettingsScreenController controller;
	private Scene scene;
	
	/**
	 * Creates a new controller before every test.
	 */
	@Before
	public void setUp() {
		scene = mock(Scene.class);
		
		//Create the controller
		controller = new SettingsScreenController();
		
		//Initialize the fields
		controller.initFXMLForTest();
		
		//Initialize the controller
		controller.init(scene);
	}

	/**
	 * Test method for {@link SettingsScreenController#onSwitchTo()}.
	 */
	@Test
	public void testOnSwitchTo() {
		controller.onSwitchTo();
		
		//The gridpane should not be empty
		assertFalse(controller.getGridPane().getChildren().isEmpty());
		
		for (Node node : controller.getGridPane().getChildren()) {
			int column = GridPane.getColumnIndex(node);
			//All nodes on the left should be labels
			if (column == 0) {
				assertTrue(node instanceof Label);
			}
		}
	}
	
	/**
	 * Test method for {@link SettingsScreenController#onSwitchTo()}.<br>
	 * <br>
	 * Checks if the amount of settings displayed is equal to the amount
	 * of settings we have.
	 */
	@Test
	public void testOnSwitchTo2() {
		controller.onSwitchTo();
		
		int amountOfNodes = 0;
		for (Node node : controller.getGridPane().getChildren()) {
			//We count all left nodes
			if (GridPane.getColumnIndex(node) == 0) {
				amountOfNodes++;
			}
		}
		
		assertEquals(getAmountOfSettings(), amountOfNodes);
	}
	
	/**
	 * @return
	 * 		the total amount of settings.
	 */
	private int getAmountOfSettings() {
		int amountOfSettings = 0;
		Settings settings = Settings.getInstance();
		
		amountOfSettings += settings.getBooleanSettings().size();
		amountOfSettings += settings.getDoubleSettings().size();
		amountOfSettings += settings.getIntegerSettings().size();
		amountOfSettings += settings.getKeySettings().size();
		amountOfSettings += settings.getSliderSettings().size();
		
		return amountOfSettings;
	}

}
