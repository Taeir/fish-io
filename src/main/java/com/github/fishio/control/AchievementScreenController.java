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
	private Label smallachieve31;
	@FXML
	private Label smallachieve32;
	@FXML
	private Label smallachieve33;
	@FXML
	private Label smallachieve34;
	@FXML
	private Label smallachieve35;
	@FXML
	private Label smallachieve51;
	@FXML
	private Label smallachieve52;
	@FXML
	private Label smallachieve53;
	@FXML
	private Label smallachieve54;
	@FXML
	private Label smallachieve55;
	@FXML
	private Label smallachieve71;
	@FXML
	private Label smallachieve72;
	@FXML
	private Label smallachieve73;
	@FXML
	private Label smallachieve74;
	@FXML
	private Label smallachieve75;
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
	private Label icon31;
	@FXML
	private Label icon32;
	@FXML
	private Label icon33;
	@FXML
	private Label icon34;
	@FXML
	private Label icon35;
	@FXML
	private Label icon51;
	@FXML
	private Label icon52;
	@FXML
	private Label icon53;
	@FXML
	private Label icon54;
	@FXML
	private Label icon55;
	@FXML
	private Label icon71;
	@FXML
	private Label icon72;
	@FXML
	private Label icon73;
	@FXML
	private Label icon74;
	@FXML
	private Label icon75;

	@FXML
	private ImageView achieveLVLIcon1;
	@FXML
	private ImageView achieveLVLIcon2;
	@FXML
	private ImageView achieveLVLIcon3;
	@FXML
	private ImageView achieveLVLIcon5;
	@FXML
	private ImageView achieveLVLIcon7;
	
	@Override
	public void init(Scene scene) {
		bindOpacities();
		setDefaultOpacities();

		addKillListener();
		addDeathListener();
		addHitWallListener();
		addLivesListener();
		addScoreListener();

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

		smallachieve31.setOpacity(0.1);
		smallachieve32.setOpacity(0.1);
		smallachieve33.setOpacity(0.1);
		smallachieve34.setOpacity(0.1);
		smallachieve35.setOpacity(0.1);

		smallachieve51.setOpacity(0.1);
		smallachieve52.setOpacity(0.1);
		smallachieve53.setOpacity(0.1);
		smallachieve54.setOpacity(0.1);
		smallachieve55.setOpacity(0.1);

		smallachieve71.setOpacity(0.1);
		smallachieve72.setOpacity(0.1);
		smallachieve73.setOpacity(0.1);
		smallachieve74.setOpacity(0.1);
		smallachieve75.setOpacity(0.1);
	}

	/**
	 * Adds a listener to the Enemy Kill achievement.
	 */
	private void addKillListener() {
		AchievementManager.ENEMY_KILL.getLevelProperty().addListener((o, oVal, nVal) -> {
			// Fall through is intended.
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
	 * Adds a listener to the death achievement.
	 */
	private void addHitWallListener() {
		AchievementManager.HIT_WALL.getLevelProperty().addListener((o, oVal, nVal) -> {
			// Fall through is intended.
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
			achieveLVLIcon3
					.setImage(Preloader.getImageOrLoad("/sprites/ChieveLvls/AchieveLVL" + nVal.intValue() + ".png"));
		});
	}


	/**
	 * Adds a listener to the Lives consumption achievement.
	 */
	private void addLivesListener() {
		AchievementManager.LIVES_CONSUMPTION.getLevelProperty().addListener((o, oVal, nVal) -> {
			//Fall through is intended.
			switch (nVal.intValue()) {
				case 5:
				smallachieve55.setOpacity(1);
				case 4:
				smallachieve54.setOpacity(1);
				case 3:
				smallachieve53.setOpacity(1);
				case 2:
				smallachieve52.setOpacity(1);
				case 1:
				smallachieve51.setOpacity(1);
					break;
				default:
					break;
			}
			achieveLVLIcon5
					.setImage(Preloader.getImageOrLoad("/sprites/ChieveLvls/AchieveLVL" + nVal.intValue() + ".png"));
		});
	}

	/**
	 * Adds a listener to the player score achievement.
	 */
	private void addScoreListener() {
		AchievementManager.PLAYER_SCORE.getLevelProperty().addListener((o, oVal, nVal) -> {
			// Fall through is intended.
			switch (nVal.intValue()) {
			case 5:
				smallachieve75.setOpacity(1);
			case 4:
				smallachieve74.setOpacity(1);
			case 3:
				smallachieve73.setOpacity(1);
			case 2:
				smallachieve72.setOpacity(1);
			case 1:
				smallachieve71.setOpacity(1);
				break;
			default:
				break;
			}
			achieveLVLIcon7
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

		icon31.opacityProperty().bind(smallachieve31.opacityProperty());
		icon32.opacityProperty().bind(smallachieve32.opacityProperty());
		icon33.opacityProperty().bind(smallachieve33.opacityProperty());
		icon34.opacityProperty().bind(smallachieve34.opacityProperty());
		icon35.opacityProperty().bind(smallachieve35.opacityProperty());

		icon51.opacityProperty().bind(smallachieve51.opacityProperty());
		icon52.opacityProperty().bind(smallachieve52.opacityProperty());
		icon53.opacityProperty().bind(smallachieve53.opacityProperty());
		icon54.opacityProperty().bind(smallachieve54.opacityProperty());
		icon55.opacityProperty().bind(smallachieve55.opacityProperty());

		icon71.opacityProperty().bind(smallachieve71.opacityProperty());
		icon72.opacityProperty().bind(smallachieve72.opacityProperty());
		icon73.opacityProperty().bind(smallachieve73.opacityProperty());
		icon74.opacityProperty().bind(smallachieve74.opacityProperty());
		icon75.opacityProperty().bind(smallachieve75.opacityProperty());
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
