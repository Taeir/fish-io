package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Help screen controller.
 */
public class SettingsScreenController implements ScreenController {

	private Log log = Log.getLogger();
	private Settings settings = Settings.getInstance();
	
	@FXML
	private Button btnBackToMenu;
	
	@FXML
	private GridPane gridPane;
	
	@Override
	public void init(Scene scene) { }

	@Override
	public void onSwitchTo() {
		int row = 0;
		for (String key : settings.getSettings()) {
			Label label = new Label(key);
			TextField textField = new TextField();
			textField.setText(String.valueOf(settings.get(key)));
			textField.setOnAction(e -> {
				e.consume();
				double value = Double.valueOf(textField.getText());
				settings.set(key, value);
			});
			gridPane.add(label, 0, row);
			gridPane.add(textField, 1, row);
			row++;
		}
		
	}
	
	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		settings.save();
		log.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
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
