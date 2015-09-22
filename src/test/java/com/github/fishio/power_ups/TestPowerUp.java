package com.github.fishio.power_ups;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * Test class for the PowerUp class.
 */
public abstract class TestPowerUp {
	
	private PowerUp pu = getPowerUp();
	
	/**
	 * @return
	 * 		The PowerUp object used for testing.
	 */
	public abstract PowerUp getPowerUp();
	
	/**
	 * @return
	 * 		The PlayingField used to initialise the PowerUp.
	 */
	public abstract PlayingField getPlayingField();
	
	/**
	 * Tests the getPField method.
	 */
	@Test
	public void testGetPField() {
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
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		pu.onCollide(pf);
		
		Mockito.verify(pu).executeEffect(pf);
	}
}
