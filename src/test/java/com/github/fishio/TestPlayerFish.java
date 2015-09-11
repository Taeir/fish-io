package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		
		Mockito.verify(pf, never()).setDead();
		Mockito.verify(pf2, never()).setDead();
	}
	
	/**
	 * Tests {@link PlayerFish#canMoveThroughWall()}
	 * using a larger EnemyFish to collide with.
	 */
	@Test
	public void testCollideWithLargerEnemyFish() {
		EnemyFish ef = Mockito.spy(new EnemyFish(Mockito.mock(BoundingBox.class), 
				null, 0.0, 0.0));
		when(ef.getBoundingArea().getSize()).thenReturn(6.1);
		
		pf.onCollide(ef);
		
		Mockito.verify(pf).setDead();
		Mockito.verify(ef, never()).setDead();
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
		
		Mockito.verify(ef).setDead();
		Mockito.verify(pf, never()).setDead();
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
		
		Mockito.verify(ef, never()).setDead();
		Mockito.verify(pf, never()).setDead();
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
		ef.setDead();

		pf.onCollide(ef);
		
		Mockito.verify(pf, never()).setDead();
	}
}
