package com.github.fishio.view;

import com.github.fishio.FishIO;
import com.github.fishio.PlayingField;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The controller class of the single player game.
 * 
 * @author Chiel Bruin
 * @since 03-09-2015
 */
public class SinglePlayerController implements ScreenController {

	private FishIO mainApp;

	@FXML
	private Canvas gameCanvas;
	@FXML
	private VBox deathScreen;
	@FXML
	private Label scoreField;

	@Override
	public void setMainApp(FishIO mainApp) {
		this.mainApp = mainApp;

		//setup the playing field
		PlayingField pf = new PlayingField(30, "Fish.io Singleplayer");
		pf.setCanvas(gameCanvas);
		pf.show(mainApp.getPrimaryStage());		
		pf.startGame();

		showDeathScreen(true);
	}

	/**
	 * Set the visibility of the death screen.
	 * 
	 * @param visible
	 * 			Boolean to indicate if the screen is visible.
	 */
	public void showDeathScreen(boolean visible) {
		FadeTransition fade = new FadeTransition(Duration.millis(400), deathScreen);
		if (visible) {
			fade.setFromValue(0.0);
			fade.setToValue(1.0);
		} else {

			fade.setToValue(1.0);
			fade.setFromValue(0.0);
		}


		fade.play();
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
