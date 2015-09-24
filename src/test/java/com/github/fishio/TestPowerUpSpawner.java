package com.github.fishio;

import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.power_ups.PuFreeze;
import com.github.fishio.power_ups.PuSuperSpeed;

/**
 * Tests the PowerUpSpawer class.
 */
public class TestPowerUpSpawner {

	private PowerUpSpawner pus;
	
	private PlayingField pf;
	
	private Random rand;
	
	/**
	 * Sets the PowerUpSpawner attribute in this class
	 * before every test.
	 */
	@Before
	public void setUp() {
		this.pf = Mockito.mock(PlayingField.class);
		when(pf.getFPS()).thenReturn(60); //Making sure our PowerUpSpawner doesn't think the FPS is 0.
		
		this.pus = Mockito.spy(new PowerUpSpawner(pf));
		
		this.rand = Mockito.mock(Random.class);
		this.pus.setRandom(rand);
	}
	
	/**
	 * Tests the first case in the switch statement of the
	 * getRandomPowerUp method.
	 */
	@Test
	public void testGetRandomPowerUp1() {
		when(rand.nextInt(pus.getPowerUpCount())).thenReturn(0);
		
		PowerUp pu = pus.getRandomPowerUp();
		assertTrue(pu instanceof PuFreeze);
		
		//TODO check the correct CollisionMask of the PowerUp
		//once CollisionMasks are implemented in PowerUps.
	}
	
	/**
	 * Tests the second case in the switch statement of the
	 * getRandomPowerUp method.
	 */
	@Test
	public void testGetRandomPowerUp2() {
		when(rand.nextInt(pus.getPowerUpCount())).thenReturn(1);
		
		PowerUp pu = pus.getRandomPowerUp();
		assertTrue(pu instanceof PuSuperSpeed);
		
		//TODO check the correct CollisionMask of the PowerUp
		//once CollisionMasks are implemented in PowerUps.
	}
	
	/**
	 * Tests the default case in the switch statement of the
	 * getRandomPowerUp method.
	 */
	@Test
	public void testGetRandomPowerUpDefault() {
		int defaultIndex = pus.getPowerUpCount();
		when(rand.nextInt(pus.getPowerUpCount())).thenReturn(defaultIndex);
		
		PowerUp pu = pus.getRandomPowerUp();
		assertTrue(pu == null);
	}
	
	/**
	 * Tests the postTick method.
	 */
	@Test
	public void testPostTick() {
		int intervalTicks = pf.getFPS() * pus.getInterval();
		
		PowerUp pu = Mockito.mock(PowerUp.class);
		doReturn(pu).when(pus).getRandomPowerUp();
		
		//Making sure that no PowerUp is added after when the interval hasn't passed yet
		for (int i = 0; i < intervalTicks - 1; i++) {
			pus.postTick();
			Mockito.verify(pf, never()).add(anyObject());
		}
		
		//Now that the interval has passed, a random PowerUp should have been added.
		pus.postTick();
		Mockito.verify(pf).add(pu);
		
		//Making sure that it resets after an interval has passed
		for (int i = 0; i < intervalTicks - 1; i++) {
			pus.postTick();
			Mockito.verify(pf).add(anyObject());
		}
		
		//Again a random powerUp should have been added
		pus.postTick();
		Mockito.verify(pf, times(2)).add(pu);
	}
}
