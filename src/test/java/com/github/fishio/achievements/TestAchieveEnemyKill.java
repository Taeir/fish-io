package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class tests the player Death achievement part of the ({@link Achieve}
 * class.
 *
 */
public class TestAchieveEnemyKill {
	
	private Achievement enemyKill = new Achievement("enemyKill");
	private Achieve achieve = new Achieve();
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 150. The return should be achievement level 5.
	 */
	@Test
	public void testcheckEnemyKill5() {
		EnemyKillObserver.setCounter(650);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(5, enemyKill.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 75. The return should be achievement level 4.
	 */
	@Test
	public void testcheckEnemyKill4() {
		EnemyKillObserver.setCounter(150);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(4, enemyKill.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 25. The return should be achievement level 3.
	 */
	@Test
	public void testcheckEnemyKill3() {
		EnemyKillObserver.setCounter(65);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(3, enemyKill.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 7. The return should be achievement level 2.
	 */
	@Test
	public void testcheckEnemyKill2() {
		EnemyKillObserver.setCounter(25);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(2, enemyKill.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 3. The return should be achievement level 1.
	 */
	@Test
	public void testcheckEnemyKill1() {
		EnemyKillObserver.setCounter(7);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(1, enemyKill.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckEnemyKill0() {
		EnemyKillObserver.setCounter(2);
		achieve.checkEnemyKill(enemyKill);
		assertEquals(0, enemyKill.getLevel(), 0.0);
	}
}
