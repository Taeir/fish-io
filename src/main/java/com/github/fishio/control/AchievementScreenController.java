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
		System.out.println("method reached");
		achievesmall11.setEffect(colorAdjust1);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 1) {
			System.out.println("if reached");
			colorAdjust1.setBrightness(0);
			achievesmall11.setEffect(colorAdjust1);
		}
		System.out.println("method end");
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
	
	/**
	 * Changes the color of the HitWall achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement31picture() {
		Achievement HitWall = new Achievement("HitWall");
		achievesmall31.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall32;
	
	/**
	 * Changes the color of the HitWall achievement level 2 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement32picture() {
		Achievement HitWall = new Achievement("HitWall");
		achievesmall32.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall33;
	
	/**
	 * Changes the color of the HitWall achievement level 3 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement33picture() {
		Achievement HitWall = new Achievement("HitWall");
		achievesmall33.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall34;
	
	/**
	 * Changes the color of the HitWall achievement level 4 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement34picture() {
		Achievement HitWall = new Achievement("HitWall");
		achievesmall34.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall35;
	
	/**
	 * Changes the color of the HitWall achievement level 5 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement35picture() {
		Achievement HitWall = new Achievement("HitWall");
		achievesmall35.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall41;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement41picture() {
		Achievement Revive = new Achievement("Revive");
		achievesmall41.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall42;
	
	/**
	 * Changes the color of the Revive achievement level 2 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement42picture() {
		Achievement Revive = new Achievement("Revive");
		achievesmall42.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall43;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement43picture() {
		Achievement Revive = new Achievement("Revive");
		achievesmall43.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall44;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement44picture() {
		Achievement Revive = new Achievement("Revive");
		achievesmall44.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall45;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement45picture() {
		Achievement Revive = new Achievement("Revive");
		achievesmall45.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall51;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement51picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall51.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall52;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement52picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall52.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall53;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement53picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall53.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall54;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement54picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall54.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall55;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement55picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall55.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall61;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement61picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall61.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall62;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement62picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall62.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall63;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement63picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall63.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall64;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement64picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall64.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall65;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement65picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall65.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall71;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement71picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall71.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall72;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement72picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall72.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall73;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement73picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall73.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall74;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement74picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall74.setEffect(colorAdjust1);
	}
	
	@FXML
	private Label achievesmall75;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement75picture() {
		Achievement Pun = new Achievement("Pun");
		achievesmall75.setEffect(colorAdjust1);
	}
	
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
		setAchievement31picture();
		setAchievement32picture();
		setAchievement33picture();
		setAchievement34picture();
		setAchievement35picture();
		setAchievement41picture();
		setAchievement42picture();
		setAchievement43picture();
		setAchievement44picture();
		setAchievement45picture();
		setAchievement51picture();
		setAchievement52picture();
		setAchievement53picture();
		setAchievement54picture();
		setAchievement55picture();
		setAchievement61picture();
		setAchievement62picture();
		setAchievement63picture();
		setAchievement64picture();
		setAchievement65picture();
		setAchievement71picture();
		setAchievement72picture();
		setAchievement73picture();
		setAchievement74picture();
		setAchievement75picture();
		
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
