package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;

/**
 * Test class for the PowerUp class.
 */
public abstract class TestPowerUp {
	
	/**
	 * @return
	 * 		The PowerUp object used for testing.
	 */
	public abstract PowerUp getPowerUp();
	
	/**
	 * @return
	 * 		The (mocked) PlayingField used to initialise the PowerUp.
	 */
	public abstract PlayingField getPlayingField();
	
	/**
	 * Tests the getPField method.
	 */
	@Test
	public void testGetPField() {
		PowerUp pu = getPowerUp();
		
		assertSame(getPlayingField(), pu.getPField());
	}
	
	/**
	 * Tests the executeEffect method.
	 */
	@Test
	public abstract void testExecuteEffect();
	
	/**
	 * Tests the onCollide method using an EnemyFish.
	 */
	@Test
	public void testOnCollide() {
		PowerUp pu = getPowerUp();
		
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		pu.onCollide(pf);
		
		Mockito.verify(pu).executeEffect(pf);
	}
	
	/**
	 * Tests the getSpeedVector method.
	 */
	@Test
	public void testGetSpeedVector() {
		assertEquals(new Vec2d(PowerUp.DEFAULT_VX, PowerUp.DEFAULT_VY),
				getPowerUp().getSpeedVector());
	}
	
	/**
	 * Tests the setSpeedVector method.
	 */
	@Test
	public void testSetSpeedVector() {
		PowerUp pu = getPowerUp();
		
		pu.setSpeedVector(new Vec2d(42, -69));
		
		assertEquals(new Vec2d(42, -69), pu.getSpeedVector());
	}
	
	/**
	 * Tests the canMoveThroughWall method.
	 */
	@Test
	public void testCanMoveThroughWall() {
		assertTrue(getPowerUp().canMoveThroughWall());
	}
	
	/**
	 * Tests the hitWall method.
	 */
	@Test
	public void testHitWall() {
		PowerUp pu = getPowerUp();
		
		assertFalse(pu.isDead());
		
		pu.hitWall();
		
		assertTrue(pu.isDead());
	}
	
}
