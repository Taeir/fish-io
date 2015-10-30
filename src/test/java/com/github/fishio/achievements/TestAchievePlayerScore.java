package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.SinglePlayerPlayingField;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This class tests the Player Score achievement.
 *
 */
public class TestAchievePlayerScore {

	/**
	 * Resets the PLAYER_SCORE achievement level to 0.
	 */
	@Before
	public void setUp() {
		AchievementManager.PLAYER_SCORE.setLevel(0);
	}

	/**
	 * @return a new PlayerScoreObserver.
	 */
	public PlayerScoreObserver getScoreObserver() {
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);

		PlayerFish pf = mock(PlayerFish.class);
		SimpleObjectProperty<PlayerFish> sop = new SimpleObjectProperty<>();

		when(sppf.getPlayer()).thenReturn(pf);
		when(sppf.playerProperty()).thenReturn(sop);
		PlayerScoreObserver pdo = new PlayerScoreObserver(sppf);
		return pdo;
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 1000. The return should be achievement level 5.
	 */
	@Test
	public void testcheckPlayerScore5() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(1000);
		assertEquals(5, AchievementManager.PLAYER_SCORE.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 500. The return should be achievement level 4.
	 */
	@Test
	public void testcheckPlayerScore4() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(500);
		assertEquals(4, AchievementManager.PLAYER_SCORE.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 200. The return should be achievement level 3.
	 */
	@Test
	public void testcheckPlayerScore3() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(200);
		assertEquals(3, AchievementManager.PLAYER_SCORE.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 50. The return should be achievement level 2.
	 */
	@Test
	public void testcheckPlayerScore2() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(50);
		assertEquals(2, AchievementManager.PLAYER_SCORE.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 10. The return should be achievement level 1.
	 */
	@Test
	public void testcheckPlayerScore1() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(10);
		assertEquals(1, AchievementManager.PLAYER_SCORE.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * playerScore equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckPlayerScore0() {
		PlayerScoreObserver dko = getScoreObserver();
		dko.setCounter(0);
		assertEquals(0, AchievementManager.PLAYER_SCORE.getLevel());
	}
}
