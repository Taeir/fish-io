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
 * This class tests the Hit Wall achievement.
 *
 */
public class TestAchieveHitWall {

	/**
	 * Resets the HIT_WALL achievement level to 0.
	 */
	@Before
	public void setUp() {
		AchievementManager.HIT_WALL.setLevel(0);
	}

	/**
	 * @return a new HitWallObserver.
	 */
	public HitWallObserver getHitObserver() {
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);

		PlayerFish pf = mock(PlayerFish.class);
		SimpleObjectProperty<PlayerFish> sop = new SimpleObjectProperty<>();

		when(sppf.getPlayer()).thenReturn(pf);
		when(sppf.playerProperty()).thenReturn(sop);
		HitWallObserver lco = new HitWallObserver(sppf);
		return lco;
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 500. The return should be achievement level 5.
	 */
	@Test
	public void testcheckHitWall5() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(500);
		assertEquals(5, AchievementManager.HIT_WALL.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 100. The return should be achievement level 4.
	 */
	@Test
	public void testcheckHitWall4() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(100);
		assertEquals(4, AchievementManager.HIT_WALL.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 50. The return should be achievement level 3.
	 */
	@Test
	public void testcheckHitWall3() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(50);
		assertEquals(3, AchievementManager.HIT_WALL.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 10. The return should be achievement level 2.
	 */
	@Test
	public void testcheckHitWall2() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(10);
		assertEquals(2, AchievementManager.HIT_WALL.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 1. The return should be achievement level 1.
	 */
	@Test
	public void testcheckHitWall1() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(1);
		assertEquals(1, AchievementManager.HIT_WALL.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * HitWalls equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckHitWall0() {
		HitWallObserver lco = getHitObserver();
		lco.setCounter(0);
		assertEquals(0, AchievementManager.HIT_WALL.getLevel());
	}
}
