package com.github.fishio.control;

import com.github.fishio.FishIO;
import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * The controller class for the main menu.
 */
public class MainMenuController implements ScreenController {
	
	private Log log = Log.getLogger();
	
	@FXML
	private Button btnSingleplayer;
	
	@FXML
	private Button btnMultiplayer;
	
	@FXML
	private Button btnLoadLevel;
	
	@FXML
	private Button btnShowHighscores;
	
	@FXML
	private Button btnShowAchievements;
	
	@FXML
	private Button btnShowStatistics;
	
	@FXML
	private Button btnShowSettings;
	
	@FXML
	private Button btnShowHelp;
	
	@FXML
	private Button btnExit;

	@Override
	public void init(Scene scene) { }
	
	@Override
	public void onSwitchTo() { }

	/**
	 * Start a singleplayer game.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void startSinglePlayer() {
		log.log(LogLevel.INFO, "Player Pressed the SinglePlayer Button.");
		Preloader.switchTo("singlePlayer", 400);
	}

	/**
	 * Start a multiplayer game.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void startMultiplayer() {
		//TODO (not now)
	}

	/**
	 * Load a custom/story level
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void loadLevel() {
		//TODO (not now)
	}

	/**
	 * Show the highscore screen.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void showHighScores() {
		//TODO (not now)
	}

	/**
	 * Show the achievements screen.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void showAchievements() {
		log.log(LogLevel.INFO, "Player Pressed the achievementScreen Button.");
		Preloader.switchTo("achievementScreen", 400);
	}

	/**
	 * Show the statistics screen.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void showStatistics() {
		//TODO (not now)
	}

	/**
	 * Show the game settings.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void showSettings() {
		//TODO (not now)
	}

	/**
	 * Show the help screen.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void showHelp() {
		log.log(LogLevel.INFO, "Player Pressed the Help Button.");
		Preloader.switchTo("helpScreen", 400);
	}

	/**
	 * Exit the application.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void exitGame() {
		log.log(LogLevel.INFO, "Player Pressed the Exit Button.");
		FishIO.getInstance().closeApplication();
	}
	
	/**
	 * @return
	 * 		the button that starts a single player game.
	 */
	public Button getBtnSingleplayer() {
		return btnSingleplayer;
	}

	/**
	 * @return
	 * 		the button that starts a multi player game.
	 */
	public Button getBtnMultiplayer() {
		return btnMultiplayer;
	}

	/**
	 * @return
	 * 		the button that loads a level from a save file.
	 */
	public Button getBtnLoadLevel() {
		return btnLoadLevel;
	}

	/**
	 * @return
	 * 		the button that shows the highscores screen.
	 */
	public Button getBtnShowHighscores() {
		return btnShowHighscores;
	}

	/**
	 * @return
	 * 		the button that shows the achievements screen.
	 */
	public Button getBtnShowAchievements() {
		return btnShowAchievements;
	}

	/**
	 * @return
	 * 		the button that shows the statistics.
	 */
	public Button getBtnShowStatistics() {
		return btnShowStatistics;
	}

	/**
	 * @return
	 * 		the button that shows the settings screen.
	 */
	public Button getBtnShowSettings() {
		return btnShowSettings;
	}

	/**
	 * @return
	 * 		the button that shows the help screen.
	 */
	public Button getBtnShowHelp() {
		return btnShowHelp;
	}

	/**
	 * @return
	 * 		the button that exits the menu.
	 */
	public Button getBtnExit() {
		return btnExit;
	}

}
