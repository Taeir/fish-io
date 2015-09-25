package com.github.fishio.achievements;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * This class loads a set of Achievements and sets the rules that have to be met
 * in order for the achievement to be obtained.
 *
 */
public final class Achieve {
	
	private Achieve() {
	
	}
	
	/**
	 * Changes the achievement level of the playerDeath achievement when certain
	 * requirements have been met.
	 * 
	 * @param playerDeath
	 *            The Achievement associated with the playerDeath observer.
	 */
	public static void checkPlayerDeath(Achievement playerDeath) {
		
		if (PlayerDeathObserver.getCounter() >= 100) {
			playerDeath.setLevel(5);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 100 times");
		}
		else if (PlayerDeathObserver.getCounter() >= 50) {
			playerDeath.setLevel(4);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 50 times");
		}
		else if (PlayerDeathObserver.getCounter() >= 10) {
			playerDeath.setLevel(3);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 10 times");
		}
		else if (PlayerDeathObserver.getCounter() >= 5) {
			playerDeath.setLevel(2);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 5 times");
		}
		else if (PlayerDeathObserver.getCounter() >= 1) {
			playerDeath.setLevel(1);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 1 time");
		}
	}
	
	/**
	 * Changes the achievement level of the enemyKill achievement when certain
	 * requirements have been met.
	 * 
	 * @param enemyKill
	 *            The Achievement associated with the enemyKill observer.
	 */
	public static void checkEnemyKill(Achievement enemyKill) {
		
		if (EnemyKillObserver.getCounter() >= 500) {
			enemyKill.setLevel(5);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 500 times");
		}
		else if (EnemyKillObserver.getCounter() >= 100) {
			enemyKill.setLevel(4);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 100 times");
		}
		else if (EnemyKillObserver.getCounter() >= 50) {
			enemyKill.setLevel(3);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 50 times");
		}
		else if (EnemyKillObserver.getCounter() >= 10) {
			enemyKill.setLevel(2);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 10 times");
		}
		else if (EnemyKillObserver.getCounter() >= 3) {
			enemyKill.setLevel(1);
			Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 3 times");
		}
	}
	
}
