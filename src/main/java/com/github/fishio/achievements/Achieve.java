package com.github.fishio.achievements;

/**
 * This class creates a set of Achievements and sets the rules that have to be
 * met in order for the achievement to be obtained.
 *
 */
public class Achieve {
	
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achievement enemyKill = new Achievement("enemyKill");
	/**
	 * Changes the achievement level of the playerDeath achievement when certain
	 * requirements have been met.
	 */
	public void checkPlayerDeath() {
		
		if (PlayerDeathObserver.getCounter() >= 100) {
			playerDeath.setLevel(5);
		}
		else if (PlayerDeathObserver.getCounter() >= 50) {
			playerDeath.setLevel(4);
		}
		else if (PlayerDeathObserver.getCounter() >= 10) {
			playerDeath.setLevel(3);
		}
		else if (PlayerDeathObserver.getCounter() >= 5) {
			playerDeath.setLevel(2);
		}
		else if (PlayerDeathObserver.getCounter() >= 1) {
			playerDeath.setLevel(1);
		}
	}
	
	/**
	 * Changes the achievement level of the playerDeath achievement when certain
	 * requirements have been met.
	 */
	public void checkEnemyKill() {
		
		if (EnemyKillObserver.getCounter() >= 500) {
			enemyKill.setLevel(5);
		}
		else if (EnemyKillObserver.getCounter() >= 100) {
			enemyKill.setLevel(4);
		}
		else if (EnemyKillObserver.getCounter() >= 50) {
			enemyKill.setLevel(3);
		}
		else if (EnemyKillObserver.getCounter() >= 10) {
			enemyKill.setLevel(2);
		}
		else if (EnemyKillObserver.getCounter() >= 3) {
			enemyKill.setLevel(1);
		}
	}
	
}
