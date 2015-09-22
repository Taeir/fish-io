package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.stage.Stage;

/**
 * Tests the PlayerFish class.
 */
public class TestPlayerFish {
	//TODO Change some of the Mockito mocks and testing calls with verify(never), to simple getters.
	//TODO - Comment made by Taeir - 2015/09/18
	private PlayerFish pf;
	
	/**
	 * Creates a new PlayerFish before each test
	 * case.
	 */
	@Before
	public void setUp() {
		pf = Mockito.spy(new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Stage.class), null));
		when(pf.getBoundingArea().getSize()).thenReturn(5.0);
	}
	
	/**
	 * Tests {@link PlayerFish#getSpeedVector()}.
	 */
	@Test
	public void testGetSpeedVector() {
		pf.setSpeedX(3.0);
		pf.setSpeedY(5.0);
		
		assertEquals(new Vec2d(3.0, 5.0), pf.getSpeedVector());
	}
	
	/**
	 * Tests {@link PlayerFish#setSpeedVector(Vec2d)}.
	 */
	@Test
	public void testSetSpeedVector() {
		pf.setSpeedVector(new Vec2d(3.0, 5.0));
		
		assertEquals(new Vec2d(3.0, 5.0), pf.getSpeedVector());
	}
	
	/**
	 * Tests {@link PlayerFish#preMove()}.
	 */
	@Test
	public void testPreMove() {
		pf.preMove();
		
		Mockito.verify(pf).adjustXSpeed();
		Mockito.verify(pf).adjustYSpeed();
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
		PlayerFish pf2 = Mockito.spy(new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Stage.class), null));
		
		pf.onCollide(pf2);
		
		Mockito.verify(pf, never()).kill();
		Mockito.verify(pf2, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a larger EnemyFish to collide with, when the player has one
	 * life left.
	 */
	@Test
	public void testCollideWithLargerEnemyFish() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(6.1);
		
		//Set the amount of lives to 1
		pf.livesProperty().set(1);
		
		//Collide with an enemy
		pf.onCollide(ef);
		
		//The player should have no more lives left.
		assertEquals(0, pf.getLives());
		
		//The player should be dead.
		assertTrue(pf.isDead());
		
		Mockito.verify(ef, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a larger EnemyFish to collide with, when the player has
	 * multiple lives left.
	 */
	@Test
	public void testCollideWithLargerEnemyFish2() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(6.1);
		
		//Set the amount of lives to 2.
		pf.livesProperty().set(2);
		
		//Collide with an enemy
		pf.onCollide(ef);
		
		//The player should have one less life.
		assertEquals(1, pf.getLives());
		
		//The player should not be dead.
		assertFalse(pf.isDead());
		Mockito.verify(ef, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a smaller EnemyFish to collide with.
	 */
	@Test
	public void testCollideWithSmallerEnemyFish() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(3.9);
		ICollisionArea bb = pf.getBoundingArea();
		
		pf.onCollide(ef);
		
		Mockito.verify(ef).kill();
		Mockito.verify(pf, never()).kill();
		Mockito.verify(bb).increaseSize(pf.getGrowthSpeed() * 3.9 / 5.0);
	}
	
	/**
	 */
	@Test
	public void testCollideWithSameSizeEnemyFish() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(5.0);

		pf.onCollide(ef);
		
		Mockito.verify(ef, never()).kill();
		Mockito.verify(pf, never()).kill();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a dead EnemyFish.
	 */
	@Test
	public void testCollideWithDeadEnemyFish() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(5.1);
		ef.kill();

		pf.onCollide(ef);
		
		Mockito.verify(pf, never()).kill();
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
		pf.livesProperty().set(PlayerFish.MAX_LIVES);
		pf.addLife();
		
		assertEquals(PlayerFish.MAX_LIVES, pf.getLives());
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
}
