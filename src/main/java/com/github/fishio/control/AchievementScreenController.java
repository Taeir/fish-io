package com.github.fishio.control;

import com.github.fishio.Preloader;
import com.github.fishio.achievements.AchievementManager;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

/**
 * Achievement screen controller.
 */
public class AchievementScreenController implements ScreenController {

	private Log logger = Log.getLogger();

	@FXML 
	private ScrollPane scrollPane;
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
	@FXML
	private ImageView achieveLVLIcon1;
	@FXML
	private ImageView achieveLVLIcon2;
	
	@Override
	public void init(Scene scene) {
		bindOpacities();
		
		setDefaultOpacities();
		addDeathListener();
		addKillListener();
		
		SimpleDoubleProperty p = Settings.getInstance().getDoubleProperty("SCREEN_HEIGHT");
		scrollPane.maxHeightProperty().bind(p.subtract(200));
	}

	/**
	 * Set the default opacities.
	 */
	private void setDefaultOpacities() {
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

	/**
	 * Adds a listener to the death achievement.
	 */
	private void addDeathListener() {
		AchievementManager.PLAYER_DEATH.getLevelProperty().addListener((o, oVal, nVal) -> {
			//Fall through is intended.
			switch (nVal.intValue()) {
				case 5:
				smallachieve25.setOpacity(1);
				case 4:
				smallachieve24.setOpacity(1);
				case 3:
				smallachieve23.setOpacity(1);
				case 2:
				smallachieve22.setOpacity(1);
				case 1:
				smallachieve21.setOpacity(1);
					break;
				default:
					break;
			}
			achieveLVLIcon2
					.setImage(Preloader.getImageOrLoad("/sprites/ChieveLvls/AchieveLVL" + nVal.intValue() + ".png"));
		});
	}

	/**
	 * Adds a listener to the Enemy Kill achievement.
	 */
	private void addKillListener() {
		AchievementManager.ENEMY_KILL.getLevelProperty().addListener((o, oVal, nVal) -> {
			//Fall through is intended.
			switch (nVal.intValue()) {
				case 5:
				smallachieve15.setOpacity(1);
				case 4:
				smallachieve14.setOpacity(1);
				case 3:
				smallachieve13.setOpacity(1);
				case 2:
				smallachieve12.setOpacity(1);
				case 1:
				smallachieve11.setOpacity(1);
					break;
				default:
					break;
			}
			achieveLVLIcon1
					.setImage(Preloader.getImageOrLoad("/sprites/ChieveLvls/AchieveLVL" + nVal.intValue() + ".png"));
		});
	}

	/**
	 * Links the opacities from the icons to the labels.
	 */
	private void bindOpacities() {
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
	}
	
	@Override
	public void onSwitchTo() { }
	
	/**
	 * Go back to main menu. This button loads the main menu.
	 */
	@FXML
	public void backToMenu() {
		AudioEngine.getInstance().playButtonSound();
		logger.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}
	
	/**
	 * @return btnbackToMenu
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}
}
