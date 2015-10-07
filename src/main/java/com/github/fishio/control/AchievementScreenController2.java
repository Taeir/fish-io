package com.github.fishio.control;




import com.github.fishio.Preloader;
import com.github.fishio.achievements.Achievement;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController2 implements ScreenController {
	
	private Log log = Log.getLogger();
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achievement enemyKill = new Achievement("enemyKill");
	private Achievement hitWall = new Achievement("HitWall");
	private Achievement revive = new Achievement("Revive");
	private Achievement pun = new Achievement("Pun");

	@FXML
	private Button btnBackToMenu;
	private Label bigachieve1;
	private Label bigachieve2;
	private Label bigachieve3;
	private Label bigachieve4;
	private Label bigachieve5;
	private Label bigachieve6;
	private Label bigachieve7;
	private Label smallachieve11;
	private Label smallachieve12;
	private Label smallachieve13;
	private Label smallachieve14;
	private Label smallachieve15;
	private Label smallachieve21;
	private Label smallachieve22;
	private Label smallachieve23;
	private Label smallachieve24;
	private Label smallachieve25;
	private Label smallachieve31;
	private Label smallachieve32;
	private Label smallachieve33;
	private Label smallachieve34;
	private Label smallachieve35;
	private Label smallachieve41;
	private Label smallachieve42;
	private Label smallachieve43;
	private Label smallachieve44;
	private Label smallachieve45;
	private Label smallachieve51;
	private Label smallachieve52;
	private Label smallachieve53;
	private Label smallachieve54;
	private Label smallachieve55;
	private Label smallachieve61;
	private Label smallachieve62;
	private Label smallachieve63;
	private Label smallachieve64;
	private Label smallachieve65;
	private Label smallachieve71;
	private Label smallachieve72;
	private Label smallachieve73;
	private Label smallachieve74;
	private Label smallachieve75;
	
	
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
	
	@Override
	public void init(Scene scene) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSwitchTo() {
		// TODO Auto-generated method stub
		
	}

}
