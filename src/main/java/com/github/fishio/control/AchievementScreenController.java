package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
	private Log log = Log.getLogger();
	
	@FXML
	private Button btnBackToMenu;
	
	@Override
	public void init(Scene scene) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onSwitchTo() {
		// TODO if necessary.
	}
	
	/**
	 * Go back to main menu. This button loads the main menu.
	 */
	@FXML
	public void backToMenu() {
		log.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * @return btnbackToMenu
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}

}
