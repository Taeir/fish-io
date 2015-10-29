package com.github.fishio.control;

import java.util.Map.Entry;
import java.util.Set;

import com.github.fishio.HighScore;
import com.github.fishio.Preloader;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for the high scores screen.
 */
public class HighScoreController implements ScreenController {

	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TableView<Entry<String, Integer>> table;
	@FXML
	private TableColumn<Entry<String, Integer>, String> playerColumn;
	@FXML
	private TableColumn<Entry<String, Integer>, Number> scoreColumn;
	
	@Override
	public void init(Scene scene) {
		scrollPane.prefHeightProperty().bind(Settings.getInstance().getDoubleProperty("SCREEN_HEIGHT").subtract(240));
		table.prefHeightProperty().bind(scrollPane.heightProperty().subtract(10));
	}

	@Override
	public void onSwitchTo() {
		Set<Entry<String, Integer>> set = HighScore.getAll().entrySet();
		
		playerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
		scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()));
			
		ObservableList<Entry<String, Integer>> list = FXCollections.observableArrayList();
		list.addAll(set);
		table.setItems(list);		

		scoreColumn.setSortType(TableColumn.SortType.DESCENDING);
		table.getSortOrder().add(scoreColumn);
	}
	
	/**
	 * Load the main menu screen.
	 */
	@FXML
	public void backToMenu() {
		AudioEngine.getInstance().playButtonSound();
		Log.getLogger().log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}

}
