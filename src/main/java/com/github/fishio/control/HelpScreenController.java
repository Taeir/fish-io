package com.github.fishio.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Help screen controller.
 * @author Dio
 *
 */
public class HelpScreenController implements ScreenController{

	@FXML
	private Label helpText;
	
	@Override
	public void init(Scene scene) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchTo() {
		helpText.setText("Test");
	}

}
