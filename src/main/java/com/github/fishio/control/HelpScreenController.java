package com.github.fishio.control;

import com.github.fishio.Preloader;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Help screen controller.
 */
public class HelpScreenController implements ScreenController {

	@FXML
	private Label helpText;
	
	@Override
	public void init(Scene scene) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchTo() {
		//TODO if needed
	}
	
	/**
	 * Go back to main menu.
	 * This button loads the main menu.
	 */
	@FXML
	public void backToMenu() {
		Preloader.switchTo("mainMenu", 400);
	}

}
