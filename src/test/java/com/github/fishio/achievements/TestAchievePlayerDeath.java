package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * This class tests the player Death achievement part of the ({@link Achieve}
 * class.
 *
 */
public class TestAchievePlayerDeath {
	
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achieve achieve = new Achieve();
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 150. The return should be achievement level 5.
	 */
	@Test
	public void testcheckPlayerDeath5() {
		PlayerDeathObserver.setCounter(150);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(5, playerDeath.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 75. The return should be achievement level 4.
	 */
	@Test
	public void testcheckPlayerDeath4() {
		PlayerDeathObserver.setCounter(75);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(4, playerDeath.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 25. The return should be achievement level 3.
	 */
	@Test
	public void testcheckPlayerDeath3() {
		PlayerDeathObserver.setCounter(25);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(3, playerDeath.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 7. The return should be achievement level 2.
	 */
	@Test
	public void testcheckPlayerDeath2() {
		PlayerDeathObserver.setCounter(7);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(2, playerDeath.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 3. The return should be achievement level 1.
	 */
	@Test
	public void testcheckPlayerDeath1() {
		PlayerDeathObserver.setCounter(3);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(1, playerDeath.getLevel(), 0.0);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckPlayerDeath0() {
		PlayerDeathObserver.setCounter(0);
		achieve.checkPlayerDeath(playerDeath);
		assertEquals(0, playerDeath.getLevel(), 0.0);
	}
}
