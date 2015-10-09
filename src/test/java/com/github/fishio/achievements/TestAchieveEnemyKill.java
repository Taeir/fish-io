package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javafx.beans.property.SimpleObjectProperty;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.SinglePlayerPlayingField;

/**
 * This class tests the player Death achievement part of the ({@link Achieve}
 * class.
 *
 */
public class TestAchieveEnemyKill {
	
	/**
	 * Resets the ENEMY_KILL achievement level to 0.
	 */
	@Before
	public void setUp() {
		AchievementManager.ENEMY_KILL.setLevel(0);
	}
	
	/**
	 * @return
	 * 		a new EnemyKillObserver.
	 */
	public EnemyKillObserver getKillObserver() {
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		
		PlayerFish pf = mock(PlayerFish.class);
		SimpleObjectProperty<PlayerFish> sop = new SimpleObjectProperty<>();
		
		when(sppf.getPlayer()).thenReturn(pf);
		when(sppf.playerProperty()).thenReturn(sop);
		return new EnemyKillObserver(sppf);
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 150. The return should be achievement level 5.
	 */
	@Test
	public void testcheckEnemyKill5() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(650);
		assertEquals(5, AchievementManager.ENEMY_KILL.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 75. The return should be achievement level 4.
	 */
	@Test
	public void testcheckEnemyKill4() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(150);
		assertEquals(4, AchievementManager.ENEMY_KILL.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 25. The return should be achievement level 3.
	 */
	@Test
	public void testcheckEnemyKill3() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(65);
		assertEquals(3, AchievementManager.ENEMY_KILL.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 7. The return should be achievement level 2.
	 */
	@Test
	public void testcheckEnemyKill2() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(25);
		assertEquals(2, AchievementManager.ENEMY_KILL.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 3. The return should be achievement level 1.
	 */
	@Test
	public void testcheckEnemyKill1() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(7);
		assertEquals(1, AchievementManager.ENEMY_KILL.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckEnemyKill0() {
		EnemyKillObserver eko = getKillObserver();
		eko.setCounter(0);
		assertEquals(0, AchievementManager.ENEMY_KILL.getLevel());
	}
}
