package com.github.fishio.control;




import com.github.fishio.Preloader;
import com.github.fishio.achievements.Achieve;
import com.github.fishio.achievements.Achievement;
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
	
	

	@FXML
	private Button btnBackToMenu;

	@FXML
	private Label achievesmall11;
	
	private ColorAdjust colorAdjust1 = new ColorAdjust(0, 0, 1, 0);
	/**
	 * Changes the color of the PlayerDeath achievement level 1 when it is
	 * achieved.
	 */
	public void setAchievement11picture() {
		Achievement playerDeath = new Achievement("PlayerDeath");
		System.out.println("1");
		achievesmall11.setEffect(colorAdjust1);
		System.out.println("2");
		Achieve.checkPlayerDeath(playerDeath);
		System.out.println("3");
		if (playerDeath.getLevel() >= 1) {
			System.out.println("4");
			colorAdjust1.setBrightness(0);
			System.out.println("5");
			achievesmall11.setEffect(colorAdjust1);
			System.out.println("6");
		}
		System.out.println("7");
	}
	
	@FXML
	private Label achievesmall12;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 2 when it is
	 * achieved.
	 */
	public void setAchievement12picture() {
		Achievement playerDeath = new Achievement("PlayerDeath");
		achievesmall12.setEffect(colorAdjust1);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 2) {
			colorAdjust1.setBrightness(0);
			achievesmall12.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall13;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 3 when it is
	 * achieved.
	 */
	public void setAchievement13picture() {
		Achievement playerDeath = new Achievement("PlayerDeath");
		achievesmall13.setEffect(colorAdjust1);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 3) {
			colorAdjust1.setBrightness(0);
			achievesmall13.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall14;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 4 when it is
	 * achieved.
	 */
	public void setAchievement14picture() {
		Achievement playerDeath = new Achievement("PlayerDeath");
		achievesmall14.setEffect(colorAdjust1);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 4) {
			colorAdjust1.setBrightness(0);
			achievesmall14.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall15;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 5 when it is
	 * achieved.
	 */
	public void setAchievement15picture() {
		Achievement playerDeath = new Achievement("PlayerDeath");
		achievesmall15.setEffect(colorAdjust1);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 5) {
			colorAdjust1.setBrightness(0);
			achievesmall15.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall21;
	
	/**
	 * Changes the color of the EnemyKill achievement level 1 when it is
	 * achieved.
	 */
	public void setAchievement21picture() {
		Achievement enemyKill = new Achievement("enemyKill");
		achievesmall21.setEffect(colorAdjust1);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 1) {
			colorAdjust1.setBrightness(0);
			achievesmall21.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall22;
	
	/**
	 * Changes the color of the EnemyKill achievement level 2 when it is
	 * achieved.
	 */
	public void setAchievement22picture() {
		Achievement enemyKill = new Achievement("enemyKill");
		achievesmall22.setEffect(colorAdjust1);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 2) {
			colorAdjust1.setBrightness(0);
			achievesmall22.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall23;
	
	/**
	 * Changes the color of the EnemyKill achievement level 3 when it is
	 * achieved.
	 */
	public void setAchievement23picture() {
		Achievement enemyKill = new Achievement("enemyKill");
		achievesmall23.setEffect(colorAdjust1);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 3) {
			colorAdjust1.setBrightness(0);
			achievesmall23.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall24;
	
	/**
	 * Changes the color of the EnemyKill achievement level 4 when it is
	 * achieved.
	 */
	public void setAchievement24picture() {
		Achievement enemyKill = new Achievement("enemyKill");
		achievesmall24.setEffect(colorAdjust1);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 4) {
			colorAdjust1.setBrightness(0);
			achievesmall24.setEffect(colorAdjust1);
		}
	}
	
	@FXML
	private Label achievesmall25;
	
	/**
	 * Changes the color of the EnemyKill achievement level 5 when it is
	 * achieved.
	 */
	public void setAchievement25picture() {
		Achievement enemyKill = new Achievement("enemyKill");
		achievesmall25.setEffect(colorAdjust1);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 5) {
			colorAdjust1.setBrightness(0);
			achievesmall25.setEffect(colorAdjust1);
		}
	}
	
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
		setAchievement11picture();
		setAchievement12picture();
		setAchievement13picture();
		setAchievement14picture();
		setAchievement15picture();
		setAchievement21picture();
		setAchievement22picture();
		setAchievement23picture();
		setAchievement24picture();
		setAchievement25picture();
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
