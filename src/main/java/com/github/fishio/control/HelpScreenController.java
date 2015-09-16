package com.github.fishio.control;

import com.github.fishio.Preloader;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Help screen controller.
 */
public class HelpScreenController implements ScreenController {

	@FXML
	private Label helpText;
	
	@FXML
	private Label helpTextPowerups;
	
	@FXML
	private Label helpTextExtra;
	
	@FXML
	private Button btnBackToMenu;
	
	@Override
	public void init(Scene scene) { }

	@Override
	public void onSwitchTo() { }
	
	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		Preloader.switchTo("mainMenu", 400);
	}

	/**
	 * @return
	 * 		the button that returns to the main menu.
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}

}
