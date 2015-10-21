package com.github.fishio.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.game.GameState;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.MultiplayerPlayingField;
import com.github.fishio.multiplayer.client.FishIOClient;
import com.github.fishio.multiplayer.server.FishIOServer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Help screen controller.
 */
public class MultiplayerScreenController implements ScreenController {

	private Log logger = Log.getLogger();
	private Pattern addressPattern = Pattern.compile("((?<address>.*)(?<port>:\\d+))|(?<address2>.*)");
	
	//Listens for when the game starts, and switches to the multiplayer game screen when it does.
	private ChangeListener<? super GameState> gameStartListener = (observable, oldVal, newVal) -> {
		if (newVal == GameState.RUNNING) {
			Util.onJavaFX(() -> Preloader.switchTo("multiplayerGameScreen", 0));
		}
	};
	
	//Listens for when a playingField is created, and adds the gameStartListener.
	private ChangeListener<MultiplayerPlayingField> playingFieldListener = (observable, oldVal, newVal) -> {
		if (newVal != null) {
			newVal.getGameThread().stateProperty().addListener(gameStartListener);
			
			//Call the listener once, so we ensure we did not miss the game starting.
			SimpleObjectProperty<GameState> sop = newVal.getGameThread().stateProperty();
			gameStartListener.changed(sop, sop.get(), sop.get());
		}
	};
	
	@FXML
	private Button btnBackToMenu;
	
	@FXML
	private Button hostGame;
	
	@FXML
	private Button joinGame;
	
	@FXML
	private TextField joinIP;
	
	@FXML
	private TextField createGamePort;
	
	@Override
	public void init(Scene scene) {
		FishIOClient.getInstance().getPlayingFieldProperty().addListener(playingFieldListener);
		FishIOServer.getInstance().getPlayingFieldProperty().addListener(playingFieldListener);
	}

	@Override
	public void onSwitchTo() { }
	
	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		AudioEngine.getInstance().playButtonSound();
		logger.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}

	/**
	 * Join a multiplayer game.
	 */
	@FXML
	public void joinGame() {
		AudioEngine.getInstance().playButtonSound();
		logger.log(LogLevel.INFO, "Player Pressed JoinGame Button.");

		String s = joinIP.getText();
		Matcher m = addressPattern.matcher(s);
		
		//Invalid pattern
		if (!m.matches()) {
			joinIP.setStyle("-fx-text-color: red");
			return;
		}
		
		//Reset style
		joinIP.setStyle("");
		
		String sport = m.group("port");
		int port;
		if (sport == null || sport.isEmpty()) {
			port = 25565;
		} else {
			port = Integer.parseInt(sport.substring(1));
		}
		
		//Start connecting
		String address = m.group("address");
		if (address == null || address.isEmpty()) {
			address = m.group("address2");
		}
		FishIOClient.getInstance().connect(address, port);
	}
	
	/**
	 * Host a multi player game.
	 */
	@FXML
	public void hostGame() {
		AudioEngine.getInstance().playButtonSound();
		logger.log(LogLevel.INFO, "Player Pressed HostGame Button.");

		String s = createGamePort.getText();
		
		//Invalid port
		if (!s.matches("\\d+")) {
			createGamePort.setStyle("-fx-text-color: red");
			return;
		}
		
		//Reset style
		createGamePort.setStyle("");
		
		int port = Integer.parseInt(s);
		
		//Start the server
		FishIOServer.getInstance().start(port);
	}

}
