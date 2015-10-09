package com.github.fishio.control;

import com.github.fishio.Preloader;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {
	
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
	 * Changes the screen based on the level of EnemyKill achievements gained.
	 * 
	 * @param level
	 *            The highest level of the EnemyKill achievement.
	 */
	public void onEnemyKill(int level) {
		if (level >= 1) {
			smallachieve21.setOpacity(1);
		}
		if (level >= 2) {
			smallachieve22.setOpacity(1);
		}
		if (level >= 3) {
			smallachieve23.setOpacity(1);
		}
		if (level >= 4) {
			smallachieve24.setOpacity(1);
		}
		if (level >= 5) {
			smallachieve25.setOpacity(1);
		}
	}
	
	/**
	 * Controls the viewer for the first achievement: player deaths.
	 * 
	 * @param level
	 *            the level of the achievement.
	 */
	public void onPlayerDeath(int level) {
		if (level >= 1) {
			smallachieve11.setOpacity(1);
		}
		if (level >= 2) {
			smallachieve12.setOpacity(1);
		}
		if (level >= 3) {
			smallachieve13.setOpacity(1);
		}
		if (level >= 4) {
			smallachieve14.setOpacity(1);
		}
		if (level >= 5) {
			smallachieve15.setOpacity(1);
		}
	}
	
	/**
	 * Go back to main menu. This button loads the main menu.
	 */
	@FXML
	public void backToMenu() {
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
	}
	
}
