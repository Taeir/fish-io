package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


/**
 * This class tests the ({@link Achieve} class.
 *
 */
public class TestAchieve {
	
	private Achievement playerDeath = new Achievement("playerDeath");
	private Achievement enemyKill = new Achievement("enemyKill");
	@Mock
	private PlayerDeathObserver playerDeathObserver;
	


	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 150. The return should be achievement level 5.
	 */
	@Test
	public void testcheckPlayerDeath() {
		Mockito.when(playerDeathObserver.getCounter()).thenReturn(150);
		Achieve.checkPlayerDeath(playerDeath);
		assertEquals(5, playerDeath.getLevel(), 0.0);
	}
	
}
