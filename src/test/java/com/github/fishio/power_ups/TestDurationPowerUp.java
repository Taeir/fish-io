package com.github.fishio.power_ups;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.never;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the DurationPowerUp class.
 */
public abstract class TestDurationPowerUp extends TestPowerUp {

	/**
	 * @return
	 * 		The DurationPowerUp used for testing.
	 */
	public abstract DurationPowerUp getDurationPowerUp();
	
	@Override
	public PowerUp getPowerUp() {
		return getDurationPowerUp();
	}
	
	@Override
	public void testExecuteEffect() {
		DurationPowerUp pu = getDurationPowerUp();
		
		assertFalse(pu.isActive());
		
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		pu.executeEffect(pf);
		
		assertTrue(pu.isActive());
		Mockito.verify(pu).startEffect();
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
		DurationPowerUp pu = getDurationPowerUp();
		
		pu.preTick();
		
		assertEquals(0, pu.getTickCounter());
		Mockito.verify(pu, never()).preTickEffect();
	}
	
	/**
	 * Tests the preTick method on the second tick while the PowerUp
	 * is active.
	 */
	@Test
	public void testPreTickActiveOnPoint() {
		DurationPowerUp pu = getDurationPowerUp();
		
		pu.setActive(true);
		pu.setTickCounter(1);
		
		pu.preTick();
		
		assertEquals(1, pu.getTickCounter());
		Mockito.verify(pu).preTickEffect();
	}
	
	/**
	 * Tests the preTick method on the very first tick while the PowerUp
	 * is active.
	 */
	@Test
	public void testPreTickActiveOffPoint() {
		DurationPowerUp pu = getDurationPowerUp();
		
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
		DurationPowerUp pu = getDurationPowerUp();
		
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
		DurationPowerUp pu = getDurationPowerUp();
		
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
		DurationPowerUp pu = getDurationPowerUp();
		
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
	
	/**
	 * Tests the getTarget method.
	 */
	@Test
	public void testGetTarget() {
		DurationPowerUp pu = getDurationPowerUp();
		
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		pu.executeEffect(pf);
		
		assertSame(pf, pu.getTarget());
	}
	
	/**
	 * Tests both the setter and the getter of the target in DurationPowerUp.
	 */
	@Test
	public void testGetSetTarget() {
		DurationPowerUp pu = getDurationPowerUp();
		
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		pu.executeEffect(Mockito.mock(PlayerFish.class));
		pu.setTarget(pf);
		
		assertSame(pf, pu.getTarget());
	}
}
