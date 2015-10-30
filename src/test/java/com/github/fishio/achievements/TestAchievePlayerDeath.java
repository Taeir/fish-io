package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.SinglePlayerPlayingField;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This class tests the Player Death achievement.
 *
 */
public class TestAchievePlayerDeath {
	
	/**
	 * Resets the PLAYER_DEATH achievement level to 0.
	 */
	@Before
	public void setUp() {
		AchievementManager.PLAYER_DEATH.setLevel(0);
	}
	
	/**
	 * @return
	 * 		a new PlayerDeathObserver.
	 */
	public PlayerDeathObserver getDeathObserver() {
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		
		PlayerFish pf = mock(PlayerFish.class);
		SimpleObjectProperty<PlayerFish> sop = new SimpleObjectProperty<>();
		
		when(sppf.getPlayer()).thenReturn(pf);
		when(sppf.playerProperty()).thenReturn(sop);
		PlayerDeathObserver pdo = new PlayerDeathObserver(sppf);
		return pdo;
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 150. The return should be achievement level 5.
	 */
	@Test
	public void testcheckPlayerDeath5() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(150);
		assertEquals(5, AchievementManager.PLAYER_DEATH.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 75. The return should be achievement level 4.
	 */
	@Test
	public void testcheckPlayerDeath4() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(75);
		assertEquals(4, AchievementManager.PLAYER_DEATH.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 25. The return should be achievement level 3.
	 */
	@Test
	public void testcheckPlayerDeath3() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(25);
		assertEquals(3, AchievementManager.PLAYER_DEATH.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 7. The return should be achievement level 2.
	 */
	@Test
	public void testcheckPlayerDeath2() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(7);
		assertEquals(2, AchievementManager.PLAYER_DEATH.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 3. The return should be achievement level 1.
	 */
	@Test
	public void testcheckPlayerDeath1() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(3);
		assertEquals(1, AchievementManager.PLAYER_DEATH.getLevel());
	}
	
	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerDeaths equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckPlayerDeath0() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.setCounter(0);
		assertEquals(0, AchievementManager.PLAYER_DEATH.getLevel());
	}

	/**
	 * This method tests whether the
	 * {@link AchievementObserver#unattachAchievement(Achievement)} method
	 * actually works correctly.
	 */
	@Test
	public void testUnattachAchievement() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.unattachAchievement(AchievementManager.PLAYER_DEATH);
		assertTrue(dko.getAchievements().isEmpty());
	}

	/**
	 * This method tests whether the
	 * {@link AchievementObserver#attachAchievement(Achievement)} method
	 * actually works correctly when attaching the same Achievement twice.
	 */
	@Test
	public void testAttachAchievementTwice() {
		PlayerDeathObserver dko = getDeathObserver();
		dko.attachAchievement(AchievementManager.PLAYER_DEATH);
		assertEquals(1, dko.getAchievements().size());
	}

	/**
	 * The method tests whether the {@link Subject#detach(AchievementObserver)}
	 * method works when trying to detach a subject from an observer.
	 * 
	 */
	@Test
	public void testDetach() {
		PlayerDeathObserver dko = getDeathObserver();
		Subject playerFish = dko.getSubject();
		playerFish.detach(dko);
		assertEquals(0, playerFish.getObservers().size());
	}

}
