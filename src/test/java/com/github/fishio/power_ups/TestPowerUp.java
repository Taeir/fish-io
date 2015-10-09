package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.EnemyFish;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.VerticalBehaviour;

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
	
	/**
	 * Tests the eat method. 
	 */
	@Test
	public void testEat() {
		PowerUp pu = getPowerUp();
		
		pu.eat();
		
		assertTrue(pu.isDead());
	}
	
	/**
	 * Tests the getSize method.
	 */
	@Test
	public void testGetSize() {
		assertEquals(0, getPowerUp().getSize(), 0.0D);
	}
	
	/**
	 * Tests the getName method.
	 */
	@Test
	public abstract void testGetName();
	
	/**
	 * Tests the getBehaviour method.
	 */
	@Test
	public void testGetBehaviour() {
		IMoveBehaviour behaviour = getPowerUp().getBehaviour();
		
		assertTrue(behaviour instanceof VerticalBehaviour);
		assertEquals(2, behaviour.getSpeed(), 0.0D);
	}
	
	/**
	 * Tests the setBehaviour method.
	 */
	@Test
	public void testSetBehaviour() {
		IMoveBehaviour behaviour = new FrozenBehaviour();
		
		getPowerUp().setBehaviour(behaviour);
		
		assertEquals(behaviour, getPowerUp().getBehaviour());
	}
	
	/**
	 * Tests the canBeEatenBy method using a playerFish.
	 */
	@Test
	public void testCanBeEatenBy1() {
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		assertTrue(getPowerUp().canBeEatenBy(pf));
	}
	
	/**
	 * Tests the canBeEatenBy method using a non-PlayerFish.
	 */
	@Test
	public void testCanBeEatenBy2() {
		EnemyFish ef = Mockito.mock(EnemyFish.class);
		
		assertFalse(getPowerUp().canBeEatenBy(ef));
	}
}
