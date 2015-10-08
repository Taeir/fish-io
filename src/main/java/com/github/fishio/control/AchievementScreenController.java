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

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
	private Log log = Log.getLogger();
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achievement enemyKill = new Achievement("enemyKill");

	@FXML
	private Button btnBackToMenu;
	@FXML
	private Label smallachieve11;
	@FXML
	private Label smallachieve12;
	@FXML
	private Label smallachieve13;
	@FXML
	private Label smallachieve14;
	@FXML
	private Label smallachieve15;
	@FXML
	private Label smallachieve21;
	@FXML
	private Label smallachieve22;
	@FXML
	private Label smallachieve23;
	@FXML
	private Label smallachieve24;
	@FXML
	private Label smallachieve25;
	@FXML
	private Label icon11;
	@FXML
	private Label icon12;
	@FXML
	private Label icon13;
	@FXML
	private Label icon14;
	@FXML
	private Label icon15;
	@FXML
	private Label icon21;
	@FXML
	private Label icon22;
	@FXML
	private Label icon23;
	@FXML
	private Label icon24;
	@FXML
	private Label icon25;
	
	/**
	 * Controls the viewer for the first achievement: player deaths.
	 */
	public void setPlayerDeath() {
		Achieve.checkPlayerDeath(playerDeath);
		
		if (playerDeath.getLevel() >= 1) {
			smallachieve11.setOpacity(1);
			log.log(LogLevel.INFO, "Glutton achievement level 1 gained!");
		}
		if (playerDeath.getLevel() >= 2) {
			smallachieve12.setOpacity(1);
			log.log(LogLevel.INFO, "Glutton achievement level 2 gained!");
		}
		if (playerDeath.getLevel() >= 3) {
			smallachieve13.setOpacity(1);
			log.log(LogLevel.INFO, "Glutton achievement level 3 gained!");
		}
		if (playerDeath.getLevel() >= 4) {
			smallachieve14.setOpacity(1);
			log.log(LogLevel.INFO, "Glutton achievement level 4 gained!");
		}
		if (playerDeath.getLevel() >= 5) {
			smallachieve15.setOpacity(1);
			log.log(LogLevel.INFO, "Glutton achievement level 5 gained!");
		}
	}
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
		public void setEnemyKill() {
			Achieve.checkEnemyKill(enemyKill);
			if (enemyKill.getLevel() >= 1) {
				smallachieve21.setOpacity(1);
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 1 gained!");
			}
			if (enemyKill.getLevel() >= 2) {
				smallachieve22.setOpacity(1);
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 2 gained!");
			}
			if (enemyKill.getLevel() >= 3) {
				smallachieve23.setOpacity(1);
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 3 gained!");
			}
			if (enemyKill.getLevel() >= 4) {
				smallachieve24.setOpacity(1);
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 4 gained!");
			}
			if (enemyKill.getLevel() >= 5) {
				smallachieve25.setOpacity(1);
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 5 gained!");
			}
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
	
	@Override
	public void init(Scene scene) {
		icon11.opacityProperty().bind(smallachieve11.opacityProperty());
		icon12.opacityProperty().bind(smallachieve12.opacityProperty());
		icon13.opacityProperty().bind(smallachieve13.opacityProperty());
		icon14.opacityProperty().bind(smallachieve14.opacityProperty());
		icon15.opacityProperty().bind(smallachieve15.opacityProperty());
		icon21.opacityProperty().bind(smallachieve21.opacityProperty());
		icon22.opacityProperty().bind(smallachieve22.opacityProperty());
		icon23.opacityProperty().bind(smallachieve23.opacityProperty());
		icon24.opacityProperty().bind(smallachieve24.opacityProperty());
		icon25.opacityProperty().bind(smallachieve25.opacityProperty());
		smallachieve11.setOpacity(0.1);
		smallachieve12.setOpacity(0.1);
		smallachieve13.setOpacity(0.1);
		smallachieve14.setOpacity(0.1);
		smallachieve15.setOpacity(0.1);
		smallachieve21.setOpacity(0.1);
		smallachieve22.setOpacity(0.1);
		smallachieve23.setOpacity(0.1);
		smallachieve24.setOpacity(0.1);
		smallachieve25.setOpacity(0.1);
	}
	
	@Override
	public void onSwitchTo() {
		setPlayerDeath();
		setEnemyKill();
		
	}

}
