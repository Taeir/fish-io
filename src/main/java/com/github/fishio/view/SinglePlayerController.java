package com.github.fishio.view;

import com.github.fishio.FishIO;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The controller class of the single player game.
 * 
 * @author Chiel Bruin
 * @since 03-09-2015
 */
public class SinglePlayerController implements ScreenController {

	private FishIO mainApp;
	
	@FXML
	private VBox deathScreen;
	@FXML
	private Label scoreField;
	
	@Override
	public void setMainApp(FishIO mainApp) {
		this.mainApp = mainApp;
		showDeathScreen(true);
	}
	
	/**
	 * Set the visibility of the death screen.
	 * 
	 * @param visible
	 * 			Boolean to indicate if the screen is visible.
	 */
	public void showDeathScreen(boolean visible) {
		if (deathScreen.isVisible() != visible) {
			deathScreen.setVisible(visible);
		}
	}
	
	/**
	 * Opens the main menu.
	 */
	@FXML
	public void backToMenu() {
		mainApp.openMainMenu();
	}
	
	/**
	 * Restarts the game.
	 */
	@FXML
	public void restartGame() {
		showDeathScreen(false);
		scoreField.setText("score: 0");
		//TODO - reset the map etc.
	}
	
}
