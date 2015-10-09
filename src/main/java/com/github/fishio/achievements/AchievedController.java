package com.github.fishio.achievements;

import com.github.fishio.Util;
import com.github.fishio.control.AchievementScreenController;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

public class AchievedController {
	
	private Achievement enemyKill = new Achievement("enemyKill");
	private Achievement playerDeath = new Achievement("playerDeath");
	private Log log = Log.getLogger();
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
	public void setEnemyKill() {
		Achieve.checkEnemyKill(enemyKill);
		if (enemyKill.getLevel() >= 1) {
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 1 gained!");
			AchievementScreenController.onEnemyKill(1);
			SinglePlayerController.showAchievePopup();
		}
		if (enemyKill.getLevel() >= 2) {
			AchievementScreenController.onEnemyKill(2);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 2 gained!");
		}
		if (enemyKill.getLevel() >= 3) {
			AchievementScreenController.onEnemyKill(3);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 3 gained!");
		}
		if (enemyKill.getLevel() >= 4) {
			AchievementScreenController.onEnemyKill(4);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 4 gained!");
		}
		if (enemyKill.getLevel() >= 5) {
			AchievementScreenController.onEnemyKill(5);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 5 gained!");
		}
	}
	
	/**
	 * Controls the viewer for the second achievement: enemy kills.
	 */
	public void setPlayerDeath() {
		Achieve.checkPlayerDeath(playerDeath);
		if (playerDeath.getLevel() >= 1) {
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 1 gained!");
			AchievementScreenController.onPlayerDeath(1);
			SinglePlayerController.showAchievePopup();
		}
		if (playerDeath.getLevel() >= 2) {
			AchievementScreenController.onPlayerDeath(2);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 2 gained!");
		}
		if (playerDeath.getLevel() >= 3) {
			AchievementScreenController.onPlayerDeath(3);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 3 gained!");
		}
		if (playerDeath.getLevel() >= 4) {
			AchievementScreenController.onPlayerDeath(4);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 4 gained!");
		}
		if (playerDeath.getLevel() >= 5) {
			AchievementScreenController.onPlayerDeath(5);
			SinglePlayerController.showAchievePopup();
			log.log(LogLevel.INFO, "Survival of the fittest achievement level 5 gained!");
		}
	}
	
	public void updateAchievements() {
		Util.onJavaFX(() ->
		{
			setEnemyKill();
			setPlayerDeath();
		});
	}
	
}
