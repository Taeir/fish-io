package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
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
 * Controller for the settings screen.
 */
public class SettingsScreenController implements ScreenController {
	private Log logger = Log.getLogger();
	private Settings settings = Settings.getInstance();
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private ScrollPane scrollPane;
	
	@Override
	public void init(Scene scene) {
		//Link the height of the scroll pane to that of the window.
		DoubleBinding dp = settings.getDoubleProperty("SCREEN_HEIGHT").subtract(240);
		scrollPane.maxHeightProperty().bind(dp);
	}

	@Override
	public void onSwitchTo() {
		//Remove all the old settings
		gridPane.getChildren().clear();
		
		//Add all the new settings
		int row = showDoubleSettings(0);
		row = showIntegerSettings(row);
		row = showBooleanSettings(row);
		row = showSliderSettings(row);
		showKeySettings(row);
	}
	
	/** 
	 * Show all the slider settings.
	 * 
	 * @param row
	 * 		current row in the table
	 * 
	 * @return
	 * 		the (new) current row in the table
	 */
	private int showSliderSettings(int row) {
		for (String key : settings.getSliderSettings()) {
			//Create a label with a description and a tooltip
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			
			//Get the current value of the setting
			double value = settings.getSlider(key);
			
			//Create a slider to change this setting (min = 0, max = 100)
			Slider slider = new Slider(0, 100, 100 * value);
			slider.setMaxWidth(200);
			
			//Update the setting if the slider changes
			slider.valueProperty().addListener(e -> settings.setSlider(key, slider.getValue() / 100));
			
			//Create a label to display the percentage
			Label valueLabel = new Label();
			
			//Bind it to the sliderproperty, and display it as a percentage
			StringBinding percentage = settings.getSliderProperty(key).multiply(100).asString("%.0f%%");
			valueLabel.textProperty().bind(percentage);
			
			//Create a horizontal box with spacing of 10 between elements
			HBox box = new HBox(10);
			box.getChildren().addAll(slider, valueLabel);
			
			//Add the setting to the gridpane
			gridPane.add(label, 0, row);
			gridPane.add(box, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * Show all the key settings for the key bindings.
	 * 
	 * @param row
	 * 		current row in the table
	 * 
	 * @return
	 * 		the (new) current row in the table
	 */
	private int showKeySettings(int row) {
		for (String key : settings.getKeySettings()) {
			//Create a label with a description and a tooltip
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			
			//Create a label to display the key
			Label value = new Label(settings.getKeyCode(key).getName());
			
			//Create a change button
			Button button = new Button("Change");
			button.setOnAction(e -> {
				e.consume();
				//Play the button sound
				AudioEngine.getInstance().playButtonSound();
				
				//Make the button into a key listener
				button.setText("Type new key");
				button.setOnKeyPressed(keyEvent -> {
					//Don't let the key press have effect
					keyEvent.consume();
					button.setText("Change");
					
					//Set the new key
					KeyCode keyCode = keyEvent.getCode();
					value.setText(keyCode.getName());
					settings.setKey(key, keyCode);
					
					//Remove the key listener
					button.setOnKeyPressed(null);
				});
			});
			
			//Create a horizontal box with a spacing of 20
			HBox box = new HBox(20);
			box.getChildren().add(value);
			box.getChildren().add(button);
			
			//Add the setting to the grid
			gridPane.add(label, 0, row);
			gridPane.add(box, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * Show all the boolean settings.
	 * 
	 * @param row
	 * 		the current row in the table
	 * 
	 * @return
	 * 		the (new) current row in the table
	 */
	private int showBooleanSettings(int row) {
		for (String key : settings.getBooleanSettings()) {
			//Create a label with a description and a tooltip
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			
			//Create a button for toggling the setting.
			Button button = new Button(toButtonText(settings.getBoolean(key)));
			
			//Toggle the setting when the button is pressed
			button.setOnAction(e -> {
				e.consume();
				//Play the button sound
				AudioEngine.getInstance().playButtonSound();
				
				//The new value is the reverse of the old value
				boolean nValue = !settings.getBoolean(key);
				settings.setBoolean(key, nValue);
				
				//Update the button text
				button.setText(toButtonText(nValue));
			});
			
			//Add the setting to the gridpane
			gridPane.add(label, 0, row);
			gridPane.add(button, 1, row);
			row++;
		}
		return row;
	}
	
	/**
	 * Returns "Disable" if the given boolean is true, and "Enable" if it is
	 * false.
	 * 
	 * @param setting
	 * 		the value of the setting
	 * 
	 * @return
	 * 		"Disable" if setting is true, "Enable" otherwise.
	 */
	private String toButtonText(boolean setting) {
		if (setting) {
			return "Disable";
		} else {
			return "Enable";
		}
	}

	/** 
	 * Show all the integer settings.
	 * 
	 * @param row
	 * 		current row in the table
	 * 
	 * @return
	 * 		the (new) current row in the table
	 */
	private int showIntegerSettings(int row) {
		for (String key : settings.getIntegerSettings()) {
			//Create a label with a description and a tooltip
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			
			//Create a textfield for the setting
			TextField textField = new TextField(String.valueOf(settings.getInteger(key)));
			textField.setMaxWidth(200);
			
			//If the field loses focus, update the setting
			textField.focusedProperty().addListener((o, oVal, nVal) -> {
				if (!nVal) {
					settings.setInteger(key, Integer.parseInt(textField.getText()));
				}
			});

			//Add the setting to the gridpane
			gridPane.add(label, 0, row);
			gridPane.add(textField, 1, row);
			row++;
		}
		return row;
	}

	/** 
	 * Show all the double settings.
	 * 
	 * @param row
	 * 		current row in the table
	 * 
	 * @return
	 * 		the (new) current row in the table
	 */
	private int showDoubleSettings(int row) {
		for (String key : settings.getDoubleSettings()) {
			//Create a label with a description and a tooltip
			Label label = new Label(settings.getDescription(key));
			label.setTooltip(new Tooltip(key));
			
			//Create a textfield for the setting
			TextField textField = new TextField(String.valueOf(settings.getDouble(key)));
			textField.setMaxWidth(200);
			
			//If the field loses focus, update the setting
			textField.focusedProperty().addListener((o, oVal, nVal) -> {
				if (!nVal) {
					settings.setDouble(key, Double.parseDouble(textField.getText()));
				}
			});
			
			//Add the setting to the pane
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
		AudioEngine.getInstance().playButtonSound();
		
		settings.save();
		logger.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * Initializes all the fields in this class that would normally be
	 * initialized from the fxml file. This method is used only for testing.
	 */
	protected void initFXMLForTest() {
		gridPane = new GridPane();
		scrollPane = new ScrollPane(gridPane);
	}
	
	/**
	 * @return
	 * 		the gridpane used by the SettingsScreen.
	 */
	public GridPane getGridPane() {
		return gridPane;
	}
}
