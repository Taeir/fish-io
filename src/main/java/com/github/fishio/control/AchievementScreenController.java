package com.github.fishio.control;

import javax.swing.text.html.ImageView;

import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
	private Log log = Log.getLogger();
	
	@FXML
	private Button btnBackToMenu;
	
	@FXML
	private ImageView achievesmall11;
	
	@FXML
	private ImageView achievesmall12;
	@FXML
	private ImageView achievesmall13;
	@FXML
	private ImageView achievesmall14;
	@FXML
	private ImageView achievesmall15;
	@FXML
	private ImageView achievesmall21;
	@FXML
	private ImageView achievesmall22;
	@FXML
	private ImageView achievesmall23;
	@FXML
	private ImageView achievesmall24;
	@FXML
	private ImageView achievesmall25;
	@FXML
	private ImageView achievesmall31;
	@FXML
	private ImageView achievesmall32;
	@FXML
	private ImageView achievesmall33;
	@FXML
	private ImageView achievesmall34;
	@FXML
	private ImageView achievesmall35;
	@FXML
	private ImageView achievesmall41;
	@FXML
	private ImageView achievesmall42;
	@FXML
	private ImageView achievesmall43;
	@FXML
	private ImageView achievesmall44;
	@FXML
	private ImageView achievesmall45;
	@FXML
	private ImageView achievesmall51;
	@FXML
	private ImageView achievesmall52;
	@FXML
	private ImageView achievesmall53;
	@FXML
	private ImageView achievesmall54;
	@FXML
	private ImageView achievesmall55;
	@FXML
	private ImageView achievesmall61;
	@FXML
	private ImageView achievesmall62;
	@FXML
	private ImageView achievesmall63;
	@FXML
	private ImageView achievesmall64;
	@FXML
	private ImageView achievesmall65;
	@FXML
	private ImageView achievesmall71;
	@FXML
	private ImageView achievesmall72;
	@FXML
	private ImageView achievesmall73;
	@FXML
	private ImageView achievesmall74;
	@FXML
	private ImageView achievemall75;
	
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
