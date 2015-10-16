package com.github.fishio.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.game.GameState;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.client.FishIOClient;
import com.github.fishio.multiplayer.server.FishIOServer;

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
	private Pattern addressPattern = Pattern.compile("(?<address>.*)(?<port>:\\d+)?");
	
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
		ChangeListener<? super GameState> cl = (o, oVal, nVal) -> {
			if (nVal == GameState.RUNNING) {
				Util.onJavaFX(() -> Preloader.switchTo("multiplayerGameScreen", 0));
			}
		};
		
		GameThread gt = FishIOClient.getInstance().getPlayingField().getGameThread();
		gt.stateProperty().addListener(cl);
		
		gt = FishIOServer.getInstance().getPlayingField().getGameThread();
		gt.stateProperty().addListener(cl);
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
			port = Integer.parseInt(sport);
		}
		
		//Start connecting
		FishIOClient.getInstance().connect(m.group("address"), port);
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
