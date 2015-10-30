package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.KeyListenerBehaviour;
import com.github.fishio.settings.Settings;

import javafx.scene.Scene;

/**
 * Tests the PlayerFish class.
 */
public class TestPlayerFish extends TestIEatable {
	private PlayerFish pf;
	private Settings settings = Settings.getInstance();
	
	/**
	 * Creates a new PlayerFish before each test
	 * case.
	 */
	@Before
	public void setUp() {
		pf = spy(new PlayerFish(mock(CollisionMask.class), mock(Scene.class), null));
		when(pf.getBoundingArea().getSize()).thenReturn(5.0);
	}
	
	/**
	 * Tests {@link PlayerFish#getSize()}.
	 */
	@Test
	@Override
	public void testGetSize() {
		assertEquals(5.0, pf.getSize(), 1E-8);
	}
	
	/**
	 * Tests {@link PlayerFish#eat()}.
	 */
	@Test
	@Override
	public void testEat() {
		pf.eat();
		verify(pf).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#kill()} for a PlayerFish already dead.
	 */
	@Test
	public void testKillDead() {
		int lives = pf.getLives();
		when(pf.isDead()).thenReturn(true);
		pf.kill();
		assertEquals(pf.getLives(), lives);
	}

	/**
	 * Tests {@link PlayerFish#kill()} for a PlayerFish that is invincible.
	 */
	@Test
	public void testKillInvincible() {
		int lives = pf.getLives();
		when(pf.isInvincible()).thenReturn(true);
		pf.kill();
		assertEquals(pf.getLives(), lives);
	}

	/**
	 * Tests {@link PlayerFish#setDead()}.
	 */
	@Test
	public void testSetDead() {
		pf.setDead();
		assertTrue(pf.isDead());
	}
	
	/**
	 * Tests {@link PlayerFish#setDead()}.
	 * Test with a lot of lives.
	 */
	@Test
	public void testSetDeadLives() {
		pf.livesProperty().set(Integer.MAX_VALUE);
		pf.setDead();
		assertTrue(pf.isDead());		
	}
	
	/**
	 * Tests {@link PlayerFish#setInvincible()}.
	 */
	@Test
	public void testInvincibleOver() {
		pf.setInvincible(System.currentTimeMillis() - 100);
		assertFalse(pf.isInvincible());
	}
	
	/**
	 * Tests {@link PlayerFish#getInvincible()}.
	 */
	@Test
	public void testGetInvincibleOver() {
		pf.setInvincible(System.currentTimeMillis() - 100);
		assertEquals(0, pf.getInvincible());
	}
	
	/**
	 * Tests {@link PlayerFish#getInvincible()}.
	 */
	@Test
	public void testGetInvincibleActive() {
		long time = System.currentTimeMillis() + 100;
		pf.setInvincible(time);
		assertEquals(time, pf.getInvincible());
	}
	
	@Override
	public void testCanBeEatenBy() {
		testCanBeEatenByLargerEnemy();
		testCanBeEatenBySameEnemy();
		testCanBeEatenBySmallerEnemy();
		testCanBeEatenByInvincible();
	}
	
	/**
	 * Tests {@link PlayerFish#canBeEatenBy(IEatable).
	 * Test for larger enemy.
	 */
	public void testCanBeEatenByLargerEnemy() {
		IEatable other = mock(IEatable.class);
		when(other.getSize()).thenReturn(10.0);
		assertTrue(pf.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link PlayerFish#canBeEatenBy(IEatable).
	 * Test with enemy with same size.
	 */
	public void testCanBeEatenBySameEnemy() {
		IEatable other = mock(IEatable.class);
		when(other.getSize()).thenReturn(5.0);
		assertFalse(pf.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link PlayerFish#canBeEatenBy(IEatable).
	 * Test with a smaller enemy.
	 */
	public void testCanBeEatenBySmallerEnemy() {
		IEatable other = mock(IEatable.class);
		when(other.getSize()).thenReturn(4.0);
		assertFalse(pf.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link PlayerFish#canBeEatenBy(IEatable).
	 * Test for when the player fish is invincible
	 */
	public void testCanBeEatenByInvincible() {
		IEatable other = mock(IEatable.class);
		when(other.getSize()).thenReturn(4.0);
		pf.setInvincible(-1);
		assertFalse(pf.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}.
	 */
	@Test
	public void testCanMoveThroughWall() {
		assertFalse(pf.canMoveThroughWall());
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using another PlayerFish to collide with.
	 */
	@Test
	public void testCollideWithOtherPlayerFish() {
		PlayerFish pf2 = spy(new PlayerFish(mock(CollisionMask.class), mock(Scene.class), null));
		when(pf2.getBoundingArea().getSize()).thenReturn(1000000.0);
		pf.livesProperty().set(1);
		pf.onCollide(pf2);
	
		assertTrue(pf.isDead());
		verify(pf2, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a larger EnemyFish to collide with, when the player has one
	 * life left.
	 */
	@Test
	public void testCollideWithLargerEnemyFish() {
		EnemyFish ef = spy(new EnemyFish(mock(CollisionMask.class), null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(6.1);
		
		//Set the amount of lives to 1
		pf.livesProperty().set(1);
		
		//Collide with an enemy
		pf.onCollide(ef);
		
		//The player should have no more lives left.
		assertEquals(0, pf.getLives());
		
		//The player should be dead.
		assertTrue(pf.isDead());
		
		verify(ef, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a larger EnemyFish to collide with, when the player has
	 * multiple lives left.
	 */
	@Test
	public void testCollideWithLargerEnemyFish2() {
		EnemyFish ef = spy(new EnemyFish(mock(CollisionMask.class), null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(6.1);
		
		//Set the amount of lives to 2.
		pf.livesProperty().set(2);
		
		//Collide with an enemy
		pf.onCollide(ef);
		
		//The player should have one less life.
		assertEquals(1, pf.getLives());
		
		//The player should not be dead.
		assertFalse(pf.isDead());
		verify(ef, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a smaller EnemyFish to collide with.
	 */
	@Test
	public void testCollideWithSmallerEnemyFish() {
		EnemyFish ef = spy(new EnemyFish(mock(CollisionMask.class), null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(3.9);
		ICollisionArea bb = pf.getBoundingArea();
		
		pf.onCollide(ef);
		
		verify(ef).kill();
		verify(pf, never()).kill();
		verify(bb).increaseSize(pf.getGrowthSpeed() * 3.9 / 5.0);
	}
	
	/**
	 */
	@Test
	public void testCollideWithSameSizeEnemyFish() {
		EnemyFish ef = spy(new EnemyFish(mock(CollisionMask.class), null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(5.0);

		pf.onCollide(ef);
		
		verify(ef, never()).kill();
		verify(pf, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a dead EnemyFish.
	 */
	@Test
	public void testCollideWithDeadEnemyFish() {
		EnemyFish ef = spy(new EnemyFish(mock(CollisionMask.class), null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(5.1);
		ef.kill();

		pf.onCollide(ef);
		
		verify(pf, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#addLife()}.
	 */
	@Test
	public void testAddLife1() {
		pf.livesProperty().set(1);
		pf.addLife();
		
		assertEquals(2, pf.getLives());
	}
	
	/**
	 * Tests {@link PlayerFish#addLife()} when attempting to add a life
	 * above the maximum.
	 */
	@Test
	public void testAddLife2() {
		pf.livesProperty().set(settings.getInteger("MAX_LIVES"));
		pf.addLife();
		
		assertEquals(settings.getInteger("MAX_LIVES"), pf.getLives());
	}
	
	/**
	 * Tests {@link PlayerFish#removeLife()}.
	 */
	@Test
	public void testRemoveLife() {
		pf.livesProperty().set(2);
		
		//The fish should not be dead.
		pf.kill();
		assertFalse(pf.isDead());
		
		//The amount of lives should have been decreased.
		assertEquals(1, pf.getLives());
	}
	
	/**
	 * Tests {@link PlayerFish#removeLife()} when the Player has only one
	 * life left.
	 */
	@Test
	public void testRemoveLife2() {
		pf.livesProperty().set(1);
		
		//The fish should be dead.
		pf.kill();
		assertTrue(pf.isDead());
		
		//The amount of lives should have been decreased.
		assertEquals(0, pf.getLives());
	}

	/**
	 * Tests the getBehaviour method.
	 */
	@Test
	public void testGetBehaviour() {
		IMoveBehaviour behaviour = pf.getBehaviour();
		
		assertTrue(behaviour instanceof KeyListenerBehaviour);
	}
	
	/**
	 * Tests the setBehaviour method.
	 */
	@Test
	public void testSetBehaviour() {
		IMoveBehaviour behaviour = new FrozenBehaviour();
		
		pf.setBehaviour(behaviour);
		
		assertSame(behaviour, pf.getBehaviour());
	}
	
	@Override
	public IEatable getTestObject() {
		return pf;
	}
}
