package com.github.fishio.control;




import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
	private Log log = Log.getLogger();
	
	ColorAdjust colorAdjust1 = new ColorAdjust(0, 0, 0, 1);
	
	@FXML
	private Button btnBackToMenu;

	@FXML
	private Label achievesmall11;
	
	@FXML
	private Label achievesmall12;
	
	@FXML
	private Label achievesmall13;
	
	@FXML
	private Label achievesmall14;
	
	@FXML
	private Label achievesmall15;
	
	@FXML
	private Label achievesmall21;
	
	@FXML
	private Label achievesmall22;
	
	@FXML
	private Label achievesmall23;
	
	@FXML
	private Label achievesmall24;
	
	@FXML
	private Label achievesmall25;
	
	@FXML
	private Label achievesmall31;
	
	@FXML
	private Label achievesmall32;
	
	@FXML
	private Label achievesmall33;
	
	@FXML
	private Label achievesmall34;
	
	@FXML
	private Label achievesmall35;
	
	@FXML
	private Label achievesmall41;
	
	@FXML
	private Label achievesmall42;
	
	@FXML
	private Label achievesmall43;
	
	@FXML
	private Label achievesmall44;
	
	@FXML
	private Label achievesmall45;
	
	@FXML
	private Label achievesmall51;
	
	@FXML
	private Label achievesmall52;
	
	@FXML
	private Label achievesmall53;
	
	@FXML
	private Label achievesmall54;
	
	@FXML
	private Label achievesmall55;
	
	@FXML
	private Label achievesmall61;
	
	@FXML
	private Label achievesmall62;
	
	@FXML
	private Label achievesmall63;
	
	@FXML
	private Label achievesmall64;
	
	@FXML
	private Label achievesmall65;
	
	@FXML
	private Label achievesmall71;
	
	@FXML
	private Label achievesmall72;
	
	@FXML
	private Label achievesmall73;
	
	@FXML
	private Label achievesmall74;
	
	@FXML
	private Label achievemall75;
	
	@Override
	public void init(Scene scene) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onSwitchTo() {
		// TODO if necessary.
	}
	
	/**
	 * Go back to main menu. This button loads the main menu.
	 */
	@FXML
	public void backToMenu() {
		log.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * @return btnbackToMenu
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}

}
