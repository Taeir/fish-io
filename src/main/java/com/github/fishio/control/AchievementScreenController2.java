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
	private Label smallachieve41;
	@FXML
	private Label smallachieve42;
	@FXML
	private Label smallachieve43;
	@FXML
	private Label smallachieve44;
	@FXML
	private Label smallachieve45;
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
	private Label smallachieve61;
	@FXML
	private Label smallachieve62;
	@FXML
	private Label smallachieve63;
	@FXML
	private Label smallachieve64;
	@FXML
	private Label smallachieve65;
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
	private Label icon41;
	@FXML
	private Label icon42;
	@FXML
	private Label icon43;
	@FXML
	private Label icon44;
	@FXML
	private Label icon45;
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
	private Label icon61;
	@FXML
	private Label icon62;
	@FXML
	private Label icon63;
	@FXML
	private Label icon64;
	@FXML
	private Label icon65;
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
	
	/**
	 * Controls the viewer for the first achievement: player deaths.
	 */
	public void setPlayerDeath() {
		if (playerDeath.getLevel() == 1) {
			smallachieve11.setOpacity(1);
		}
		if (playerDeath.getLevel() == 2) {
			smallachieve12.setOpacity(1);
		}
		if (playerDeath.getLevel() == 3) {
			smallachieve13.setOpacity(1);
		}
		if (playerDeath.getLevel() == 4) {
			smallachieve14.setOpacity(1);
		}
		if (playerDeath.getLevel() == 5) {
			smallachieve15.setOpacity(1);
		}
	}
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
		public void setEnemyKill() {
			if (enemyKill.getLevel() == 1) {
				smallachieve21.setOpacity(1);
			}
			if (enemyKill.getLevel() == 2) {
				smallachieve22.setOpacity(1);
			}
			if (enemyKill.getLevel() == 3) {
				smallachieve23.setOpacity(1);
			}
			if (enemyKill.getLevel() == 4) {
				smallachieve24.setOpacity(1);
			}
			if (enemyKill.getLevel() == 5) {
				smallachieve25.setOpacity(1);
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
		icon31.opacityProperty().bind(smallachieve31.opacityProperty());
		icon32.opacityProperty().bind(smallachieve32.opacityProperty());
		icon33.opacityProperty().bind(smallachieve33.opacityProperty());
		icon34.opacityProperty().bind(smallachieve34.opacityProperty());
		icon35.opacityProperty().bind(smallachieve35.opacityProperty());
		icon41.opacityProperty().bind(smallachieve41.opacityProperty());
		icon42.opacityProperty().bind(smallachieve42.opacityProperty());
		icon43.opacityProperty().bind(smallachieve43.opacityProperty());
		icon44.opacityProperty().bind(smallachieve44.opacityProperty());
		icon45.opacityProperty().bind(smallachieve45.opacityProperty());
		icon51.opacityProperty().bind(smallachieve51.opacityProperty());
		icon52.opacityProperty().bind(smallachieve52.opacityProperty());
		icon53.opacityProperty().bind(smallachieve53.opacityProperty());
		icon54.opacityProperty().bind(smallachieve54.opacityProperty());
		icon55.opacityProperty().bind(smallachieve55.opacityProperty());
		icon61.opacityProperty().bind(smallachieve61.opacityProperty());
		icon62.opacityProperty().bind(smallachieve62.opacityProperty());
		icon63.opacityProperty().bind(smallachieve63.opacityProperty());
		icon64.opacityProperty().bind(smallachieve64.opacityProperty());
		icon65.opacityProperty().bind(smallachieve65.opacityProperty());
		icon71.opacityProperty().bind(smallachieve71.opacityProperty());
		icon72.opacityProperty().bind(smallachieve72.opacityProperty());
		icon73.opacityProperty().bind(smallachieve73.opacityProperty());
		icon74.opacityProperty().bind(smallachieve74.opacityProperty());
		icon75.opacityProperty().bind(smallachieve75.opacityProperty());
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
		smallachieve41.setOpacity(0.1);
		smallachieve42.setOpacity(0.1);
		smallachieve43.setOpacity(0.1);
		smallachieve44.setOpacity(0.1);
		smallachieve45.setOpacity(0.1);
		smallachieve51.setOpacity(0.1);
		smallachieve52.setOpacity(0.1);
		smallachieve53.setOpacity(0.1);
		smallachieve54.setOpacity(0.1);
		smallachieve55.setOpacity(0.1);
		smallachieve61.setOpacity(0.1);
		smallachieve62.setOpacity(0.1);
		smallachieve63.setOpacity(0.1);
		smallachieve64.setOpacity(0.1);
		smallachieve65.setOpacity(0.1);
		smallachieve71.setOpacity(0.1);
		smallachieve72.setOpacity(0.1);
		smallachieve73.setOpacity(0.1);
		smallachieve74.setOpacity(0.1);
		smallachieve75.setOpacity(0.1);
	}
	
	@Override
	public void onSwitchTo() {
		setPlayerDeath();
		setEnemyKill();
		
	}

}
