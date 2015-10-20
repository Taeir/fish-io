package com.github.fishio.control;

import java.util.HashMap;

import com.github.fishio.HighScore;
import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 * Controller for the high scores screen.
 */
public class HighScoreController implements ScreenController {

	@FXML
	private ScrollPane scrollPane;
	@FXML
	private GridPane gridPane;
	
	@Override
	public void init(Scene scene) {
		SimpleDoubleProperty p = Settings.getInstance().getDoubleProperty("SCREEN_HEIGHT");
		p.addListener((o, old, newVal) -> {
			scrollPane.setPrefHeight(newVal.intValue() - 240);
		});
		scrollPane.setPrefHeight(p.intValue() - 240);
	}

	@Override
	public void onSwitchTo() {
		gridPane.getChildren().clear();
		int row = 1;
		HashMap<String, Integer> map = HighScore.getAll();
		
		for (String key : map.keySet()) {
			Label score = new Label(map.get(key).toString());
			Label name = new Label(key);
			gridPane.add(name, 0, row);
			gridPane.add(score, 1, row);
			row++;
		}
	}
	
	/**
	 * Load the main menu screen.
	 */
	@FXML
	public void backToMenu() {
		Log.getLogger().log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}

}
