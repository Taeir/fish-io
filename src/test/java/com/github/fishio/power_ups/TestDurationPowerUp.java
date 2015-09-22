package com.github.fishio.power_ups;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.never;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the DurationPowerUp class.
 */
public abstract class TestDurationPowerUp extends TestPowerUp {

	private DurationPowerUp pu = getDurationPowerUp();
	
	/**
	 * @return
	 * 		The DurationPowerUp used for testing.
	 */
	public abstract DurationPowerUp getDurationPowerUp();
	
	@Override
	public void testExecuteEffect() {
		assertFalse(pu.isActive());
		
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		pu.executeEffect(pf);
		
		assertTrue(pu.isActive());
		Mockito.verify(pu).startEffect(pf);
	}

	/**
	 * Tests the PowerUp to make sure it's registered.
	 */
	@Test
	public void testRegistered() {
		PlayingField pf = getPlayingField();
		Mockito.verify(pf).registerGameListener(pu);
	}
	
	/**
	 * Tests the getDuration method.
	 */
	@Test
	public abstract void testGetDuration();
	
	/**
	 * Tests the startEffect method.
	 */
	@Test
	public abstract void testStartEffect();
	
	/**
	 * Tests the preTickEffect method.
	 */
	@Test
	public abstract void testPreTickEffect();
	
	/**
	 * Tests the postTickEffect method.
	 */
	@Test
	public abstract void testPostTickEffect();
	
	/**
	 * Tests the endEffect method.
	 */
	@Test
	public abstract void testEndEffect();
	
	/**
	 * Tests the preTick method while the PowerUp isn't active.
	 */
	@Test
	public void testPreTickNotActive() {
		pu.preTick();
		
		assertEquals(0, pu.getTickCounter());
		Mockito.verify(pu, never()).preTickEffect();
	}
	
	/**
	 * Tests the preTick method on the very first tick while the
	 * counter .
	 */
	@Test
	public void testPreTickActiveOffPoint() {
		pu.setActive(true);
		
		pu.preTick();
		
		assertEquals(0, pu.getTickCounter());
		Mockito.verify(pu, never()).preTickEffect();
	}
	
	/**
	 * Tests the postTick method on while the PowerUp isn't active.
	 */
	@Test
	public void testPostTickNotActive() {
		pu.postTick();
		
		assertEquals(0, pu.getTickCounter());
		Mockito.verify(pu, never()).postTickEffect();
		Mockito.verify(pu, never()).endEffect();
		assertFalse(pu.isActive());
	}
	
	/**
	 * Tests the on-point of the tickCounter in the postTick
	 * method while the PowerUp is active.
	 */
	@Test
	public void testPostTickActiveOnPoint() {
		pu.setActive(true);
		pu.setTickCounter(pu.getTimeTicks() - 1); // The on-point
		
		pu.postTick();
		
		assertEquals(pu.getTimeTicks(), pu.getTickCounter());
		Mockito.verify(pu, never()).postTickEffect();
		Mockito.verify(pu).endEffect();
		assertFalse(pu.isActive());
		
		//Making sure the PowerUp is no longer ticking in the PlayingField.
		PlayingField pf = getPlayingField();
		Mockito.verify(pf).unregisterGameListener(pu);
	}
	
	/**
	 * Tests the off-point of the tickCounter in the postTick 
	 * method while the PowerUp is active.
	 */
	@Test
	public void testPostTickActiveOffPoint() {
		pu.setActive(true);
		pu.setTickCounter(pu.getTimeTicks() - 2); // The off-point
		
		pu.postTick();
		
		assertEquals(pu.getTimeTicks() - 1, pu.getTickCounter());
		Mockito.verify(pu).postTickEffect();
		Mockito.verify(pu, never()).endEffect();
		assertTrue(pu.isActive());
		
		//Making sure the PowerUp is still longer ticking in the PlayingField.
		PlayingField pf = getPlayingField();
		Mockito.verify(pf, never()).unregisterGameListener(pu);
	}
}
