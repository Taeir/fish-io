package com.github.fishio.view;

import com.github.fishio.FishIO;

import javafx.fxml.FXML;

/**
 * The controller class for the main menu.
 * 
 * @author Chiel Bruin
 * @since 03-09-2015
 */
public class MainMenuController implements ScreenController {
	private FishIO mainApp;

	@Override
	public void setMainApp(FishIO mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Start a singleplayer game.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void startSinglePlayer() {
		mainApp.loadScreen("view/singlePlayer.fxml", 400);
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
		//TODO (not now)
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
		//TODO (not now)
	}

	/**
	 * Exit the application.
	 * This Method is called when pressing the menu button.
	 */
	@FXML
	public void exitGame() {
		mainApp.closeApplication();
	}

}
