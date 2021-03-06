package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.game.GameThread;

/**
 * Tests the DurationPowerUp class.
 */
public abstract class TestDurationPowerUp extends TestPowerUp {

	/**
	 * @return
	 * 		The DurationPowerUp used for testing.
	 */
	public abstract PowerUpDuration getDurationPowerUp();
	
	@Override
	public PowerUp getPowerUp() {
		return getDurationPowerUp();
	}
	
	@Override
	public void testExecuteEffect() {
		PowerUpDuration pu = getDurationPowerUp();
		
		assertFalse(pu.isActive());
		
		PlayerFish pf = mock(PlayerFish.class);
		pu.executeEffect(pf);
		
		assertTrue(pu.isActive());
		verify(pu).startEffect();
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
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.preTick();
		
		assertEquals(0, pu.getTickCounter());
	}
	
	/**
	 * Tests the preTick method on the second tick while the PowerUp
	 * is active.
	 */
	@Test
	public void testPreTickActiveOnPoint() {
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.setActive(true);
		pu.setTickCounter(1);
		
		pu.preTick();
		
		assertEquals(1, pu.getTickCounter());
	}
	
	/**
	 * Tests the preTick method on the very first tick while the PowerUp
	 * is active.
	 */
	@Test
	public void testPreTickActiveOffPoint() {
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.setActive(true);
		
		pu.preTick();
		
		assertEquals(0, pu.getTickCounter());
	}
	
	/**
	 * Tests the postTick method on while the PowerUp isn't active.
	 */
	@Test
	public void testPostTickNotActive() {
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.postTick();
		
		assertEquals(0, pu.getTickCounter());
		verify(pu, never()).endEffect();
		assertFalse(pu.isActive());
	}
	
	/**
	 * Tests the on-point of the tickCounter in the postTick
	 * method while the PowerUp is active.
	 */
	@Test
	public void testPostTickActiveOnPoint() {
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.setActive(true);
		pu.setTickCounter(pu.getTimeTicks() - 1); // The on-point
		
		pu.postTick();
		
		assertEquals(pu.getTimeTicks(), pu.getTickCounter());
		verify(pu).endEffect();
		assertFalse(pu.isActive());
		
		//Making sure the PowerUp is no longer ticking in the PlayingField.
		GameThread gameThread = getPlayingField().getGameThread();
		verify(gameThread).unregisterListener(pu);
	}
	
	/**
	 * Tests the off-point of the tickCounter in the postTick 
	 * method while the PowerUp is active.
	 */
	@Test
	public void testPostTickActiveOffPoint() {
		PowerUpDuration pu = getDurationPowerUp();
		
		pu.setActive(true);
		pu.setTickCounter(pu.getTimeTicks() - 2); // The off-point
		
		pu.postTick();
		
		assertEquals(pu.getTimeTicks() - 1, pu.getTickCounter());
		verify(pu, never()).endEffect();
		assertTrue(pu.isActive());
		
		//Making sure the PowerUp is still longer ticking in the PlayingField.
		GameThread gameThread = getPlayingField().getGameThread();
		verify(gameThread, never()).unregisterListener(pu);
	}
	
	/**
	 * Tests the getTarget method.
	 */
	@Test
	public void testGetTarget() {
		PowerUpDuration pu = getDurationPowerUp();
		
		PlayerFish pf = mock(PlayerFish.class);
		
		pu.executeEffect(pf);
		
		assertSame(pf, pu.getTarget());
	}
	
	/**
	 * Tests both the setter and the getter of the target in DurationPowerUp.
	 */
	@Test
	public void testGetSetTarget() {
		PowerUpDuration pu = getDurationPowerUp();
		
		PlayerFish pf = mock(PlayerFish.class);
		
		pu.executeEffect(mock(PlayerFish.class));
		pu.setTarget(pf);
		
		assertSame(pf, pu.getTarget());
	}
}
