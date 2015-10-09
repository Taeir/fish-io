package com.github.fishio.achievements;

import com.github.fishio.Preloader;
import com.github.fishio.control.AchievementScreenController;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

public class AchievedController {
	
	private Achievement enemyKill = new Achievement("enemyKill");
	private Achievement playerDeath = new Achievement("playerDeath");
	private Log log = Log.getLogger();
	private Achieve achievementchecker = new Achieve();
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
	public void setEnemyKill() {

		achievementchecker.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 1) {
			System.out.println("SetenemyKill lvl1 reached");
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 1 gained!");
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onEnemyKill(1);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
		}
		if (enemyKill.getLevel() >= 2) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onEnemyKill(2);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 2 gained!");
		}
		if (enemyKill.getLevel() >= 3) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onEnemyKill(3);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 3 gained!");
		}
		if (enemyKill.getLevel() >= 4) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onEnemyKill(4);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 4 gained!");
		}
		if (enemyKill.getLevel() >= 5) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onEnemyKill(5);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 5 gained!");
		}
	}
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
	public void setPlayerDeath() {
		achievementchecker.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 1) {
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 1 gained!");
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onPlayerDeath(1);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
		}
		if (playerDeath.getLevel() >= 2) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onPlayerDeath(2);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 2 gained!");
		}
		if (playerDeath.getLevel() >= 3) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onPlayerDeath(3);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 3 gained!");
		}
		if (playerDeath.getLevel() >= 4) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onPlayerDeath(4);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 4 gained!");
		}
		if (playerDeath.getLevel() >= 5) {
			((AchievementScreenController) Preloader.loadScreen("achievementScreen").getProperties().get("Controller"))
					.onPlayerDeath(5);
			((SinglePlayerController) Preloader.loadScreen("singlePlayer").getProperties().get("Controller"))
					.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 5 gained!");
		}
	}
}
