package com.github.fishio.control;

import com.github.fishio.FishIO;
import com.github.fishio.PlayingField;
import com.github.fishio.Preloader;
import com.github.fishio.SinglePlayerPlayingField;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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

	@FXML
	private Canvas gameCanvas;
	@FXML
	private VBox deathScreen;
	@FXML
	private Label scoreField;
	@FXML
	private Label endScore;

	private PlayingField pf;

	@Override
	public void init(Scene scene) {
		//setup the playing field
		pf = new SinglePlayerPlayingField(60, gameCanvas, this);
		pf.setBackground(Preloader.getImageOrLoad("background.png"));
	}
	
	@Override
	public void onSwitchTo() {
		FishIO.getInstance().getPrimaryStage().setTitle("Fish.io Singleplayer");
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
		if (pf.isRunning()) {
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
	 * @param onDone
	 * 			will be called after the death screen has been shown.
	 * 			Can be <code>null</code>.
	 */
	public void showDeathScreen(boolean visible, EventHandler<ActionEvent> onDone) {
		if (visible && deathScreen.isVisible() && deathScreen.getOpacity() == 1.0) {
			if (onDone != null) {
				onDone.handle(new ActionEvent());
			}
			return;
		} else if (!visible && !deathScreen.isVisible()) {
			if (onDone != null) {
				onDone.handle(new ActionEvent());
			}
			return;
		}
		
		FadeTransition fade = new FadeTransition(Duration.millis(400), deathScreen);
		
		if (visible) {
			//Show deathscreen fully transparent and fade it in
			deathScreen.setOpacity(0.0);
			deathScreen.setVisible(true);
			
			fade.setFromValue(0.0);
			fade.setToValue(1.0);
			
			if (onDone != null) {
				fade.setOnFinished(onDone);
			}
		} else {
			//Show deathscreen fully visible and fade it out
			deathScreen.setOpacity(1.0);
			deathScreen.setVisible(true);
			
			fade.setFromValue(1.0);
			fade.setToValue(0.0);
			
			//Hide deathscreen when animation is done.
			fade.setOnFinished(event -> {
				deathScreen.setVisible(false);
				
				if (onDone != null) {
					onDone.handle(event);
				}
			});
		}
		
		//Start animation
		fade.play();
	}

	/**
	 * Opens the main menu.
	 */
	@FXML
	public void backToMenu() {
		pf.stopGame();
		FishIO.getInstance().openMainMenu();
	}

	/**
	 * Restarts the game.
	 */
	@FXML
	public void restartGame() {
		//Stop the game, clear all items, and start it again.
		pf.stopGame();
		pf.clear();
		
		//Start the render thread (it takes some time to appear).
		pf.getRenderThread().play();
		
		//Hide the deathscreen. When the animation is done, start the game thread.
		showDeathScreen(false, event -> pf.getGameThread().play());
	}
	
	/**
	 * Update the displayed score.
	 * 
	 * @param score
	 * 			the new score to be displayed on the screen.
	 */
	public void updateScoreDisplay(int score) {
		scoreField.setText("score:" + score);
		endScore.setText("score: " + score + " points");
	}

}
