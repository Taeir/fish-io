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
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achievement enemyKill = new Achievement("enemyKill");
	private Achievement hitWall = new Achievement("HitWall");
	private Achievement revive = new Achievement("Revive");
	private Achievement pun = new Achievement("Pun");

	@FXML
	private Button btnBackToMenu;

	@FXML
	private Label achievesmall11;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 1 when it is
	 * achieved.
	 */
	public void setAchievement11picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		achievesmall11.setEffect(colorAdjust);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 1) {
			colorAdjust.setBrightness(0);
			achievesmall11.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall12;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 2 when it is
	 * achieved.
	 */
	public void setAchievement12picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall12.setEffect(colorAdjust);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 2) {
			colorAdjust.setBrightness(0);
			achievesmall12.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall13;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 3 when it is
	 * achieved.
	 */
	public void setAchievement13picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall13.setEffect(colorAdjust);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 3) {
			colorAdjust.setBrightness(0);
			achievesmall13.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall14;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 4 when it is
	 * achieved.
	 */
	public void setAchievement14picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall14.setEffect(colorAdjust);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 4) {
			colorAdjust.setBrightness(0);
			achievesmall14.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall15;
	
	/**
	 * Changes the color of the PlayerDeath achievement level 5 when it is
	 * achieved.
	 */
	public void setAchievement15picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall15.setEffect(colorAdjust);
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 5) {
			colorAdjust.setBrightness(0);
			achievesmall15.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall21;
	
	/**
	 * Changes the color of the EnemyKill achievement level 1 when it is
	 * achieved.
	 */
	public void setAchievement21picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall21.setEffect(colorAdjust);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 1) {
			colorAdjust.setBrightness(0);
			achievesmall21.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall22;
	
	/**
	 * Changes the color of the EnemyKill achievement level 2 when it is
	 * achieved.
	 */
	public void setAchievement22picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall22.setEffect(colorAdjust);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 2) {
			colorAdjust.setBrightness(0);
			achievesmall22.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall23;
	
	/**
	 * Changes the color of the EnemyKill achievement level 3 when it is
	 * achieved.
	 */
	public void setAchievement23picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall23.setEffect(colorAdjust);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 3) {
			colorAdjust.setBrightness(0);
			achievesmall23.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall24;
	
	/**
	 * Changes the color of the EnemyKill achievement level 4 when it is
	 * achieved.
	 */
	public void setAchievement24picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall24.setEffect(colorAdjust);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 4) {
			colorAdjust.setBrightness(0);
			achievesmall24.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall25;
	
	/**
	 * Changes the color of the EnemyKill achievement level 5 when it is
	 * achieved.
	 */
	public void setAchievement25picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall25.setEffect(colorAdjust);
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 5) {
			colorAdjust.setBrightness(0);
			achievesmall25.setEffect(colorAdjust);
		}
	}
	
	@FXML
	private Label achievesmall31;
	
	/**
	 * Changes the color of the HitWall achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement31picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall31.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall32;
	
	/**
	 * Changes the color of the HitWall achievement level 2 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement32picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall32.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall33;
	
	/**
	 * Changes the color of the HitWall achievement level 3 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement33picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall33.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall34;
	
	/**
	 * Changes the color of the HitWall achievement level 4 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement34picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall34.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall35;
	
	/**
	 * Changes the color of the HitWall achievement level 5 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement35picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall35.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall41;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement41picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall41.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall42;
	
	/**
	 * Changes the color of the Revive achievement level 2 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement42picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall42.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall43;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement43picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall43.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall44;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement44picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall44.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall45;
	
	/**
	 * Changes the color of the Revive achievement level 1 when it is achieved.
	 * For now does not change color yet.
	 */
	public void setAchievement45picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall45.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall51;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement51picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall51.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall52;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement52picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall52.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall53;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement53picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall53.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall54;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement54picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall54.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall55;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement55picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall55.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall61;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement61picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall61.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall62;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement62picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall62.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall63;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement63picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall63.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall64;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement64picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall64.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall65;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement65picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall65.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall71;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement71picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall71.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall72;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement72picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall72.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall73;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement73picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall73.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall74;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement74picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall74.setEffect(colorAdjust);
	}
	
	@FXML
	private Label achievesmall75;
	
	/**
	 * Changes the color of the Pun achievement level 1 when it is achieved. For
	 * now does not change color yet.
	 */
	public void setAchievement75picture() {
		ColorAdjust colorAdjust = new ColorAdjust(0, 0, 1, 0);
		
		achievesmall75.setEffect(colorAdjust);
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
