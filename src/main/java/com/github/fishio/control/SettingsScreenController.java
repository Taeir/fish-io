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
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
		gridPane.getChildren().clear();
		int row = showDoubleSettings(0);
		row = showIntegerSettings(row);
		row = showBooleanSettings(row);
		showKeySettings(row);
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
			Label label = new Label(key);
			Label value = new Label(settings.getKeyCode(key).getName());
			label.setTooltip(new Tooltip(settings.getDescription(key)));
			Button button = new Button();
			button.setText("Change");
			button.setOnAction(e -> {
				e.consume();
				button.setText("Type new key");
				
				button.setOnKeyPressed(k -> {
					KeyCode keyCode = k.getCode();
					button.setText("Change");
					value.setText(keyCode.getName());
					settings.setKey(key, keyCode);
					button.setOnKeyPressed(empty -> { });
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
			Label label = new Label(key);
			label.setTooltip(new Tooltip(settings.getDescription(key)));
			Button button = new Button();
			if (settings.getBoolean(key)) {
				button.setText("disable");				
			} else {
				button.setText("enable");
			}
			button.setOnAction(e -> {
				e.consume();
				if (settings.getBoolean(key)) {
					settings.setBoolean(key, false);
					button.setText("enable");				
				} else {
					settings.setBoolean(key, true);
					button.setText("disable");
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
			Label label = new Label(key);
			label.setTooltip(new Tooltip(settings.getDescription(key)));
			TextField textField = new TextField();
			textField.setText(String.valueOf(settings.getInteger(key)));
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
	 * show all the double settings.
	 * @param row
	 * 		current row in the table
	 * @return
	 * 		current row in the table
	 */
	private int showDoubleSettings(int row) {
		for (String key : settings.getDoubleSettings()) {
			Label label = new Label(key);
			label.setTooltip(new Tooltip(settings.getDescription(key)));
			TextField textField = new TextField();
			textField.setText(String.valueOf(settings.getDouble(key)));
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
