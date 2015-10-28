package com.github.fishio.control;

import com.github.fishio.HighScore;
import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.Util;
import com.github.fishio.achievements.AchievementManager;
import com.github.fishio.achievements.EnemyKillObserver;
import com.github.fishio.achievements.PlayerDeathObserver;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The controller class of the single player game.
 */
public class SinglePlayerController implements ScreenController {
	private static final String PAUSE_TEXT = "Pause";
	private static final String UNPAUSE_TEXT = "Unpause";

	private Log logger = Log.getLogger();
	
	private SinglePlayerPlayingField playingField;
	
	/**
	 * ChangeListener that can be attached to the
	 * {@link SinglePlayerPlayingField#playerProperty()} to make sure the
	 * correct GUI items are updated when the player fish is replaced by
	 * a new one.
	 */
	private final ChangeListener<PlayerFish> playerChangeListener = (player, oldPlayer, newPlayer) -> {
		//Listen for changes in the score
		newPlayer.scoreProperty().addListener((o, oldScore, newScore) -> updateScoreDisplay(newScore.intValue()));
		
		//Listen for changes in the lives.
		newPlayer.livesProperty().addListener((observable, oldValue, newValue) -> {
			//Update lives display
			updateLivesDisplay(newValue.intValue());
			
			//If we lost a life (this includes death (0 lives))
			if (newValue.intValue() < oldValue.intValue()) {
				//Stop the game
				playingField.getGameThread().stop();
				
				//Show the death screen, and when done, stop rendering as well.
				showDeathScreen(true, event -> playingField.getRenderer().stopRendering());
			}
		});
		
		//Update fields to initial values.
		updateScoreDisplay(newPlayer.scoreProperty().get());
		updateLivesDisplay(newPlayer.getLives());
		
		//TODO add listener to deathProperty of fish?
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
	private TextField scoreName;
	@FXML
	private Label livesField;
	@FXML
	private Label endScore;
	
	@FXML
	private Button btnPause;
	@FXML
	private Button btnMute;
	@FXML
	private Button btnMenu;
	
	@FXML
	private Button btnDSRevive;
	@FXML
	private Button btnDSRestart;
	@FXML
	private Button btnDSMenu;
	
	@Override
	public void init(Scene scene) {
		//setup the playing field
		playingField = new SinglePlayerPlayingField(60, gameCanvas, scene, 1280, 720);
		playingField.getRenderer().setBackground(Preloader.getImageOrLoad("background.png"));
		
		//If the player fish changes, this listener will be called.
		playingField.playerProperty().addListener(playerChangeListener);
		
		//The change listener has to be force called once. The player has already been created,
		//but we still want to add the listeners.
		playerChangeListener.changed(playingField.playerProperty(), playingField.getPlayer(), playingField.getPlayer());
		
		//Create observers for the achievements
		new PlayerDeathObserver(playingField);
		new EnemyKillObserver(playingField);
		
		AudioEngine.getInstance().getMuteStateProperty().addListener((o, oVal, nVal) -> {
			if (nVal.intValue() == AudioEngine.NO_MUTE) {
				btnMute.setText("Mute music");
			} else if (nVal.intValue() == AudioEngine.MUTE_MUSIC) {
				btnMute.setText("Mute all sounds");
			} else if (nVal.intValue() == AudioEngine.MUTE_ALL) {
				btnMute.setText("Unmute all sounds");
			}
		});
		
		registerAchievementPopups();
	}

	/**
	 * Adds listeners to achievements to show popups.
	 */
	private void registerAchievementPopups() {
		AchievementManager.ENEMY_KILL.getLevelProperty().addListener((o, oVal, nVal) -> {
			Util.onJavaFX(() -> {
				Image img = new Image("/sprites/chieveLarge/Achieve1.png");
				showAchievePopup(img, "Glutton!", nVal.intValue());
			});
		});
		
		AchievementManager.PLAYER_DEATH.getLevelProperty().addListener((o, oVal, nVal) -> {
			Util.onJavaFX(() -> {
				Image img = new Image("/sprites/chieveLarge/Achieve2.png");
				showAchievePopup(img, "Survival of the fittest!", nVal.intValue());

			});
		});
	}
	
	@Override
	public void onSwitchTo() {
		//Reset the pause button
		getPauseButton().setText(PAUSE_TEXT);
		getPauseButton().setDisable(false);
		
		//Hide the death screen
		deathScreen.setVisible(false);
		
		//Start the game.
		playingField.startGame();
		logger.log(LogLevel.INFO, "Started Game.");
	}

	/**
	 * Called when the pause button is pressed.
	 */
	@FXML
	public void onPause() {
		AudioEngine.getInstance().playButtonSound();
		
		GameThread gameThread = playingField.getGameThread();
		if (gameThread.isRunning()) {
			try {
				playingField.stopGameAndWait();
			} catch (InterruptedException ex) { }
			getPauseButton().setText(UNPAUSE_TEXT);
			
			logger.log(LogLevel.INFO, "Player paused the game.");
		} else {
			logger.log(LogLevel.INFO, "Player resumed the game.");
			
			playingField.startGame();
			getPauseButton().setText(PAUSE_TEXT);
		}
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
	 * Shows a fade-in and fade-out of a pop-up image when an achievement is
	 * obtained.
	 * 
	 * @param image
	 *            the image to show.
	 * @param description
	 *            the description of the popup
	 * @param level
	 *            the level of the achievement that has to be displayed.
	 */
	public void showAchievePopup(Image image, String description, int level) {
		int in = 2000;
		int out = 1000;
		int duration = 5000;
		
		ObservableList<Node> list = ((HBox) achievePopup.getChildren().get(1)).getChildren();
		ImageView img = (ImageView) list.get(0);
		ImageView popUpLVL = (ImageView) list.get(1);
		Label label = (Label) list.get(2);
		
		popUpLVL.setImage(Preloader.getImageOrLoad("/sprites/ChieveLvls/AchieveLVL" + level + ".png"));
		img.setImage(image);
		label.setText(description);
		
		achievePopup.setVisible(true);
		FadeTransition fadeIn = new FadeTransition(Duration.millis(in), achievePopup);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(0.8);
		
		FadeTransition fadeOut = new FadeTransition(Duration.millis(out), achievePopup);
		fadeOut.setFromValue(0.8);
		fadeOut.setToValue(0.0);
		fadeOut.setDelay(Duration.millis(duration));
		
		SequentialTransition transition = new SequentialTransition(fadeIn, fadeOut);
		transition.play();
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
			updateReviveButton();
			
			Util.callEventHandler(onDone);
			return;
		} else if (!visible && !deathScreen.isVisible()) {
			updateReviveButton();
			
			Util.callEventHandler(onDone);
			return;
		}
		
		FadeTransition fade = new FadeTransition(Duration.millis(400), deathScreen);
		
		if (visible) {
			//Disable pause button
			getPauseButton().setDisable(true);
			
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
		
		//Revive button
		updateReviveButton();
		
		//Start animation
		fade.play();
	}
	
	/**
	 * Updates the revive button. It is enabled when applicable.
	 */
	private void updateReviveButton() {
		//If there are no players in the game, we disable the revive button.
		if (playingField.getPlayers().isEmpty()) {
			btnDSRevive.setDisable(true);
			return;
		}
		
		//If player has lives left, we enable the revive button.
		PlayerFish player = playingField.getPlayer();
		if (player.getLives() > 0) {
			btnDSRevive.setDisable(false);
			scoreName.setVisible(false);
		} else {
			btnDSRestart.requestFocus();
			btnDSRevive.setDisable(true);
			scoreName.setVisible(true);
		}
	}

	/**
	 * Opens the main menu.
	 */
	@FXML
	public void backToMenu() {
		AudioEngine.getInstance().playButtonSound();
		
		if (playingField.getPlayer().livesProperty().intValue() <= 0) {
			 saveScore();
		}
		
		playingField.stopGame();
		playingField.clear();
		
		logger.log(LogLevel.INFO, "Player pressed backToMenu button");
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * Revives the player.
	 */
	@FXML
	public void revive() {
		AudioEngine.getInstance().playButtonSound();
		
		//Reset the pause button
		getPauseButton().setText(PAUSE_TEXT);
		getPauseButton().setDisable(false);
		
		//Remove all enemies.
		playingField.clearEnemies();
		
		//Revive the player
		playingField.revivePlayer();
		
		//Start the render thread (it takes some time to appear).
		playingField.getRenderer().startRendering();
		
		//Hide the deathscreen. When the animation is done, start the game thread.
		showDeathScreen(false, event -> playingField.getGameThread().start());
	}

	/**
	 * Restarts the game.
	 */
	@FXML
	public void restartGame() {
		AudioEngine.getInstance().playButtonSound();
		
		if (playingField.getPlayer().livesProperty().intValue() <= 0) {
			saveScore();
		}		
		//Reset the pause button
		getPauseButton().setText(PAUSE_TEXT);
		getPauseButton().setDisable(false);
		
		//Stop the game, clear all items, and start it again.
		try {
			playingField.stopGameAndWait();
		} catch (InterruptedException ex) { }
		playingField.clear();
		
		//Start the render thread (it takes some time to appear).
		playingField.getRenderer().startRendering();

		//Hide the deathscreen. When the animation is done, start the game thread.
		showDeathScreen(false, event -> playingField.startGame());
	}
	
	/**
	 * Updates the pause button to the correct state.
	 */
	public void updatePauseButton() {
		GameThread gameThread = playingField.getGameThread();
		if (!playingField.isPlayerAlive()) {
			//All player fish are dead
			getPauseButton().setText(PAUSE_TEXT);
			getPauseButton().setDisable(true);
		} else if (gameThread.isRunning()) {
			//There is a player fish alive and the game is running
			getPauseButton().setText(PAUSE_TEXT);
			getPauseButton().setDisable(false);
		} else {
			//There is a player fish alive and the game is not running
			getPauseButton().setText(UNPAUSE_TEXT);
			getPauseButton().setDisable(false);
		}
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
	 * Update the displayed life count.
	 * 
	 * @param lives
	 * 		the new life count.
	 */
	public void updateLivesDisplay(int lives) {
		Util.onJavaFX(() -> livesField.setText("Lives: " + lives));
	}

	/**
	 * @return
	 * 		the playingfield
	 */
	public SinglePlayerPlayingField getPlayingField() {
		return playingField;
	}
	
	/**
	 * Sets the playing field to the given one.
	 * 
	 * @param playingField
	 * 		the playing field to use.
	 */
	public void setPlayingField(SinglePlayerPlayingField playingField) {
		this.playingField = playingField;
	}

	/**
	 * Sets the canvas of this controller.
	 * Used by the tests.
	 * 
	 * @param canvas
	 * 		the new canvas to use.
	 */
	public void setGameCanvas(Canvas canvas) {
		this.gameCanvas = canvas;
	}
	
	/**
	 * @return
	 * 		the pause button.
	 */
	public Button getPauseButton() {
		return btnPause;
	}
	
	/**
	 * @return
	 * 		the mute/unmute button.
	 */
	public Button getMuteButton() {
		return btnMute;
	}
	
	/**
	 * Saves the score to the high scores.
	 */
	private void saveScore() {
		int score = playingField.getPlayer().scoreProperty().intValue();
		HighScore.addScore(score, scoreName.getText());
	}
	
	/**
	 * Initializes all the fields in this class that would normally be
	 * initialized from the fxml file. This method is used only for testing.
	 */
	protected void initFXMLForTest() {
		this.btnPause = new Button();
		this.btnMute = new Button();
		this.btnMenu = new Button();
		
		this.btnDSMenu = new Button();
		this.btnDSRestart = new Button();
		this.btnDSRevive = new Button();
		
		this.livesField = new Label();
		this.scoreField = new Label();
		
		this.endScore = new Label();
		this.scoreName = new TextField();
		
		this.deathScreen = new VBox();
		
		this.achievePopup = new VBox();
		this.achievePopup.getChildren().add(new HBox());
		
		HBox achieves = new HBox();
		achieves.getChildren().add(new ImageView());
		achieves.getChildren().add(new Label());
		this.achievePopup.getChildren().add(achieves);
	}
}
