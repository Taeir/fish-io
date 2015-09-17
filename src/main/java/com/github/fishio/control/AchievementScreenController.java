package com.github.fishio.control;

import com.github.fishio.Preloader;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
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
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * @return btnbackToMenu
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}

}
