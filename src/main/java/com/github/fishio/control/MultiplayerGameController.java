package com.github.fishio.control;

import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.multiplayer.MultiplayerPlayingField;
import com.github.fishio.multiplayer.client.FishClientRequestPlayerMessage;
import com.github.fishio.multiplayer.client.FishIOClient;
import com.github.fishio.multiplayer.server.FishIOServer;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for a multiplayer game (both for client as server side).
 */
public class MultiplayerGameController implements ScreenController {

	/**
	 * ChangeListener that can be attached to the
	 * {@link MultiplayerPlayingField#getOwnPlayerProperty()} to make sure
	 * the correct GUI items are updated when the player fish is replaced 
	 * by a new one.
	 */
	private final ChangeListener<PlayerFish> playerChangeListener = (player, oldPlayer, newPlayer) -> {
		if (newPlayer == null) {
			showDeathScreen(true, null);
			return;
		}
		
		//Listen for death changes
		newPlayer.getDeathProperty().addListener((o, oVal, nVal) -> showDeathScreen(nVal, null));
		
		//Listen for changes in the score
		newPlayer.scoreProperty().addListener((o, oldScore, newScore) -> updateScoreDisplay(newScore.intValue()));
		
		//Update death screen to initial state
		showDeathScreen(newPlayer.isDead(), null);
		
		//Update fields to initial values.
		updateScoreDisplay(newPlayer.scoreProperty().get());
		
	};
	
	@FXML
	private Canvas gameCanvas;
	@FXML
	private VBox deathScreen;
	@FXML
	private VBox achievePopup;
	@FXML
	private Label scoreField;
	@FXML
	private Label endScore;
	
	@FXML
	private Button btnMute;
	@FXML
	private Button btnEnd;
	
	@FXML
	private Button btnDSRevive;
	@FXML
	private Button btnDSEnd;
	
	@Override
	public void init(Scene scene) { }

	@Override
	public void onSwitchTo() {
		//Hide the death screen
		deathScreen.setVisible(false);
		
		MultiplayerPlayingField mppf;
		
		if (FishIOClient.getInstance().isConnectedQuick()) {
			btnEnd.setText("Disconnect");
			btnDSEnd.setText("Disconnect");
			
			mppf = FishIOClient.getInstance().getPlayingField();
		} else if (FishIOServer.getInstance().isRunning()) {
			btnEnd.setText("Stop server");
			btnDSEnd.setText("Stop server");
			
			mppf = FishIOServer.getInstance().getPlayingField();
		} else {
			Preloader.switchTo("mainMenu", 0);
			return;
		}
		
		//Set the background
		mppf.getRenderer().setBackground(Preloader.getImageOrLoad("background.png"));
		
		//If the player fish changes, this listener will be called.
		mppf.getOwnPlayerProperty().removeListener(playerChangeListener);
		mppf.getOwnPlayerProperty().addListener(playerChangeListener);
		
		//We force call the listener once to ensure we have reset to the right state.
		if (mppf.getOwnPlayer() != null) {
			playerChangeListener.changed(mppf.getOwnPlayerProperty(), mppf.getOwnPlayer(), mppf.getOwnPlayer());
		}
		
		AudioEngine.getInstance().getMuteStateProperty().addListener((o, oVal, nVal) -> {
			if (nVal.intValue() == AudioEngine.NO_MUTE) {
				btnMute.setText("Mute music");
			} else if (nVal.intValue() == AudioEngine.MUTE_MUSIC) {
				btnMute.setText("Mute all sounds");
			} else if (nVal.intValue() == AudioEngine.MUTE_ALL) {
				btnMute.setText("Unmute all sounds");
			}
		});
	}
	
	/**
	 * Called when the mute button is pressed.
	 */
	@FXML
	public void onMute() {
		AudioEngine.getInstance().playButtonSound();
		
		AudioEngine.getInstance().toggleMuteState();
	}
	
	/**
	 * Called when the revive button is pressed on the death screen.
	 */
	@FXML
	public void onRevive() {
		AudioEngine.getInstance().playButtonSound();
		
		if (FishIOClient.getInstance().isConnectedQuick()) {
			FishIOClient.getInstance().queueMessage(new FishClientRequestPlayerMessage(), true);
		} else if (FishIOServer.getInstance().isRunning()) {
			FishIOServer.getInstance().getPlayingField().respawnOwnPlayer();
		}
	}
	
	/**
	 * Called when the disconnect/stop server button is pressed.
	 */
	@FXML
	public void onEnd() {
		AudioEngine.getInstance().playButtonSound();
		
		//Wait for the client/server to stop running.
		try {
			FishIOClient.getInstance().disconnectAndWait(1000L);
			FishIOServer.getInstance().stopAndWait(1000L);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
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
		//Check if on FX thread.
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> showDeathScreen(visible, onDone));
			return;
		}
		
		if (visible && deathScreen.isVisible() && deathScreen.getOpacity() == 1.0) {
			Util.callEventHandler(onDone);
			return;
		} else if (!visible && !deathScreen.isVisible()) {
			Util.callEventHandler(onDone);
			return;
		}
		
		FadeTransition fade = new FadeTransition(Duration.millis(400), deathScreen);
		
		if (visible) {
			//Show deathscreen fully transparent and fade it in
			deathScreen.setOpacity(0.0);
			deathScreen.setVisible(true);
			
			fade.setFromValue(0.0);
			fade.setToValue(1.0);
			
			fade.setOnFinished(onDone);
		} else {
			//Show deathscreen fully visible and fade it out
			deathScreen.setOpacity(1.0);
			deathScreen.setVisible(true);
			
			fade.setFromValue(1.0);
			fade.setToValue(0.0);
			
			//Hide deathscreen when animation is done.
			fade.setOnFinished(event -> {
				deathScreen.setVisible(false);
				
				Util.callEventHandler(onDone);
			});
		}
		
		//Start animation
		fade.play();
	}
	
	/**
	 * Update the displayed score.
	 * 
	 * @param score
	 * 			the new score to be displayed on the screen.
	 */
	public void updateScoreDisplay(int score) {
		Util.onJavaFX(() -> {
			scoreField.setText("Score: " + score);
			endScore.setText("Score: " + score + " points");
		});
	}
	
	/**
	 * @return
	 * 		the canvas of the MultiPlayerGame screen.
	 */
	public Canvas getCanvas() {
		return this.gameCanvas;
	}

}
