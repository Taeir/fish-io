package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Help screen controller.
 */
public class SettingsScreenController implements ScreenController {

	private Log logger = Log.getLogger();
	private Settings settings = Settings.getInstance();
	
	@FXML
	private Button btnBackToMenu;
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private ScrollPane scrollPane;
	@Override
	public void init(Scene scene) {
		SimpleDoubleProperty p = settings.getDoubleProperty("SCREEN_HEIGHT");
		p.addListener((o, old, newVal) -> {
			scrollPane.setMaxHeight(newVal.intValue() - 240);
		});
		scrollPane.setMaxHeight(p.intValue() - 240);
	}

	@Override
	public void onSwitchTo() {
		gridPane.getChildren().clear();
		int row = showDoubleSettings(0);
		row = showIntegerSettings(row);
		row = showBooleanSettings(row);
		row = showSliderSettings(row);
		showKeySettings(row);
	}
	
	/** 
	 * show all the slider settings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showSliderSettings(int row) {
		for (String key : settings.getSliderSettings()) {
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			Slider slider = new Slider();
			double value = settings.getSlider(key);
			Label valueLabel = new Label(Math.round(value * 100) + "%");
			slider.setValue(100 * value);
			slider.setMaxWidth(200);
			slider.setMin(0);
			slider.setMax(100);
			slider.valueProperty().addListener(e -> {
				settings.setSlider(key, slider.getValue() / 100);
				valueLabel.setText(Math.round(slider.getValue()) + "%");
			});
			HBox box = new HBox();
			box.setSpacing(10);
			box.getChildren().addAll(slider, valueLabel);
			gridPane.add(label, 0, row);
			gridPane.add(box, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * show all the key settings for the key bindings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showKeySettings(int row) {
		for (String key : settings.getKeySettings()) {
			Label label = new Label(settings.getDescription(key));
			Label value = new Label(settings.getKeyCode(key).getName());
			label.setTooltip(new Tooltip(key));
			Button button = new Button();
			button.setText("Change");
			button.setOnAction(e -> {
				e.consume();
				AudioEngine.getInstance().playButtonSound();
				button.setText("Type new key");
				
				button.setOnKeyPressed(k -> {
					KeyCode keyCode = k.getCode();
					button.setText("Change");
					value.setText(keyCode.getName());
					settings.setKey(key, keyCode);
					button.setOnKeyPressed(null);
				});
			});
			HBox box = new HBox();
			box.setSpacing(20);
			box.getChildren().add(value);
			box.getChildren().add(button);
			gridPane.add(label, 0, row);
			gridPane.add(box, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * show all the boolean settings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showBooleanSettings(int row) {
		for (String key : settings.getBooleanSettings()) {
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			Button button = new Button();
			if (settings.getBoolean(key)) {
				button.setText("Disable");				
			} else {
				button.setText("Enable");
			}
			button.setOnAction(e -> {
				e.consume();
				if (settings.getBoolean(key)) {
					settings.setBoolean(key, false);
					AudioEngine.getInstance().playButtonSound();
					button.setText("Enable");				
				} else {
					settings.setBoolean(key, true);
					AudioEngine.getInstance().playButtonSound();
					button.setText("Disable");
				}
			});
			gridPane.add(label, 0, row);
			gridPane.add(button, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * show all the integer settings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showIntegerSettings(int row) {
		for (String key : settings.getIntegerSettings()) {
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			TextField textField = new TextField();
			textField.setText(String.valueOf(settings.getInteger(key)));
			textField.setMaxWidth(200);
			textField.setOnAction(e -> {
				e.consume();
				int value = Integer.valueOf(textField.getText());
				settings.setInteger(key, value);
			});
			gridPane.add(label, 0, row);
			gridPane.add(textField, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * show all the double settings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showDoubleSettings(int row) {
		for (String key : settings.getDoubleSettings()) {
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			TextField textField = new TextField();
			textField.setText(String.valueOf(settings.getDouble(key)));
			textField.setMaxWidth(200);
			textField.setOnAction(e -> {
				e.consume();
				double value = Double.valueOf(textField.getText());
				settings.setDouble(key, value);
			});
			gridPane.add(label, 0, row);
			gridPane.add(textField, 1, row);
			row++;
		}
		return row;
	}

	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		settings.save();
		AudioEngine.getInstance().playButtonSound();
		logger.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
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
