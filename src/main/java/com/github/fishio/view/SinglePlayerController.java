package com.github.fishio.view;

import com.github.fishio.FishIO;
import com.github.fishio.PlayingField;
import com.github.fishio.SinglePlayerPlayingField;

import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
	
	private PlayingField pf;

	@Override
	public void setMainApp(FishIO mainApp) {
		this.mainApp = mainApp;
		
		//setup the playing field
		pf = new SinglePlayerPlayingField(60, gameCanvas, this);
		pf.setBackground(new Image("background.png"));
		
		mainApp.getPrimaryStage().setTitle("Fish.io Singleplayer");
		pf.startGame();
	}
	
	/**
	 * Called when the pause button is pressed.
	 * 
	 * @param event
	 * 		the event of the user pressing the button.
	 */
	@FXML
	public void onPause(ActionEvent event) {
		if (pf == null) {
			return;
		}
		
		if (pf.getGameThread().getStatus() == Status.RUNNING) {
			pf.stopGame();
		} else {
			pf.startGame();
		}
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
		pf.stopGame();
		mainApp.openMainMenu();
	}

	/**
	 * Restarts the game.
	 */
	@FXML
	public void restartGame() {
		showDeathScreen(false);
		scoreField.setText("score: 0");
		
		//Reset the map
		pf.stopGame();
		pf = new SinglePlayerPlayingField(60, gameCanvas, this);
		pf.setBackground(new Image("background.png"));
		
		pf.startGame();
	}

}
