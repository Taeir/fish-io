package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Help screen controller.
 */
public class MultiplayerScreenController implements ScreenController {

	private Log logger = Log.getLogger();
	
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
	
	@FXML
	private Canvas keyboard;
	
	@Override
	public void init(Scene scene) { }

	@Override
	public void onSwitchTo() { }
	
	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		AudioEngine.getInstance().playEffect("button");
		logger.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}

	/**
	 * Join a multiplayer game.
	 */
	@FXML
	public void joinGame() {
		AudioEngine.getInstance().playEffect("button");
		logger.log(LogLevel.INFO, "Player Pressed JoinGame Button.");

		//TODO Actually join a multiplayer game
	}
	
	/**
	 * Host a multi player game.
	 */
	@FXML
	public void hostGame() {
		AudioEngine.getInstance().playEffect("button");
		logger.log(LogLevel.INFO, "Player Pressed HostGame Button.");

		//TODO Actually start a multiplayer game
	}

}
