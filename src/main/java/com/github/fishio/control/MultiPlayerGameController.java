package com.github.fishio.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MultiPlayerGameController implements ScreenController {

	@FXML
	private Canvas gameCanvas;
	@FXML
	private VBox deathScreen;
	@FXML
	private VBox achievePopup;
	@FXML
	private Label scoreField;
	@FXML
	private Label livesField;
	@FXML
	private Label endScore;
	
	@FXML
	private Button btnPause;
	@FXML
	private Button btnMute;
	@FXML
	private Button btnMenu;
	
	@FXML
	private Button btnDSRevive;
	@FXML
	private Button btnDSRestart;
	@FXML
	private Button btnDSMenu;
	
	@Override
	public void init(Scene scene) {}

	@Override
	public void onSwitchTo() {
		
	}
	
	/**
	 * @return
	 * 		the canvas of the MultiPlayerGame screen.
	 */
	public Canvas getCanvas() {
		return this.gameCanvas;
	}

}
