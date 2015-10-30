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
 * This class tests the Lives Consumption achievement.
 *
 */
public class TestAchieveLivesConsumption {

	/**
	 * Resets the LIVES_CONSUMPTION achievement level to 0.
	 */
	@Before
	public void setUp() {
		AchievementManager.LIVES_CONSUMPTION.setLevel(0);
	}

	/**
	 * @return a new LivesConsumptionObserver.
	 */
	public LivesConsumptionObserver getLivesObserver() {
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);

		PlayerFish pf = mock(PlayerFish.class);
		SimpleObjectProperty<PlayerFish> sop = new SimpleObjectProperty<>();

		when(sppf.getPlayer()).thenReturn(pf);
		when(sppf.playerProperty()).thenReturn(sop);
		LivesConsumptionObserver lco = new LivesConsumptionObserver(sppf);
		return lco;
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 243. The return should be achievement level
	 * 5.
	 */
	@Test
	public void testcheckLivesConsumption5() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(243);
		assertEquals(5, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 81. The return should be achievement level 4.
	 */
	@Test
	public void testcheckLivesConsumption4() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(81);
		assertEquals(4, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 27. The return should be achievement level 3.
	 */
	@Test
	public void testcheckLivesConsumption3() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(27);
		assertEquals(3, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 9. The return should be achievement level 2.
	 */
	@Test
	public void testcheckLivesConsumption2() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(9);
		assertEquals(2, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 3. The return should be achievement level 1.
	 */
	@Test
	public void testcheckLivesConsumption1() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(3);
		assertEquals(1, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}

	/**
	 * This method tests the achievement level obtained when the amount of
	 * LivesConsumptions equals to 0. The return should be achievement level 0.
	 */
	@Test
	public void testcheckLivesConsumption0() {
		LivesConsumptionObserver lco = getLivesObserver();
		lco.setCounter(0);
		assertEquals(0, AchievementManager.LIVES_CONSUMPTION.getLevel());
	}
}
