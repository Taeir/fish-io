package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the EnemyFish class.
 * 
 * @author Jesse Arens
 * 		
 */
public class TestEnemyFish {
	/**
	 * Test for {@link EnemyFish#getSpeedVector()}.
	 *
	 * 
	 */
	@Test
	public void testgetSpeedVector() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		EnemyFish enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
		Vec2d vec1 = new Vec2d(3.0, 5.0);
		Vec2d vec2 = enemy1.getSpeedVector();
		assertEquals(vec1, vec2);
	}
	
	/**
	 * Test for {@link EnemyFish#setSpeedVector(Vec2d)}.
	 */
	@Test
	public void testsetSpeedVector() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		EnemyFish enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
		
		Vec2d vec1 = new Vec2d(5.0, 7.0);
		enemy1.setSpeedVector(vec1);
		Vec2d vec2 = enemy1.getSpeedVector();
		assertEquals(vec1, vec2);
		
	}
	
	/**
	 * Test for {@link EnemyFish#hitWall()}.
	 */
	@Test
	public void testhitWall() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		EnemyFish enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
		enemy1.hitWall();
		assertTrue(enemy1.isDead());
	}
	
	/**
	 * Test for {@link EnemyFish#canMoveThroughWall()}.
	 */
	@Test
	public void testcanMoveThroughWall() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		EnemyFish enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
		assertTrue(enemy1.canMoveThroughWall());
	}
	
	/**
	 * Test for {@link EnemyFish#doesCollides(ICollidable)}.
	 */
	@Test
	public void testdoesCollides() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		EnemyFish enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
		ICollidable col1 = new EnemyFish(bb1, null, 5.0, 8.0);
		enemy1.onCollide(col1);
		assertFalse(enemy1.isDead());
	}
	
	// TODO:
	// Testrender, Testlimitspeed.
}
