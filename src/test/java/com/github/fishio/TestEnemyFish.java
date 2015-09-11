package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the EnemyFish class.
 */
public class TestEnemyFish {
	
	private BoundingBox bb1;
	private EnemyFish enemy1;
	
	/**
	 * An @Before method which creates an EnemyFish object which will be used in
	 * most of the tests.
	 */
	@Before
	public void createfishbefore() {
		bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		enemy1 = new EnemyFish(bb1, null, 3.0, 5.0);
	}
	
	/**
	 * Test for {@link EnemyFish#getSpeedVector()}.
	 *
	 * 
	 */
	@Test
	public void testgetSpeedVector() {
		Vec2d vec1 = new Vec2d(3.0, 5.0);
		Vec2d vec2 = enemy1.getSpeedVector();
		assertEquals(vec1, vec2);
	}
	
	/**
	 * Test for {@link EnemyFish#setSpeedVector(Vec2d)}.
	 */
	@Test
	public void testsetSpeedVector() {
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
		enemy1.hitWall();
		assertTrue(enemy1.isDead());
	}
	
	/**
	 * Test for {@link EnemyFish#canMoveThroughWall()}.
	 */
	@Test
	public void testcanMoveThroughWall() {
		assertTrue(enemy1.canMoveThroughWall());
	}
	
	/**
	 * Test for {@link EnemyFish#doesCollides(ICollidable)}.
	 */
	@Test
	public void testonCollide() {
		ICollidable col1 = new EnemyFish(bb1, null, 5.0, 8.0);
		enemy1.onCollide(col1);
		assertFalse(enemy1.isDead());
	}
	
	/**
	 * Test for {@link EnemyFish#limitVx()} with vx > 0 and vx >
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitVx1() {
		Vec2d vec1 = new Vec2d(LevelBuilder.MAX_EFISH_SPEED + 1, 2);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVx();
		assertEquals(LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVx()} with vx &lt; 0 and vx &lt; minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitVx2() {
		Vec2d vec1 = new Vec2d(-LevelBuilder.MAX_EFISH_SPEED - 1, 2);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVx();
		assertEquals(-LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVy()} with vy > 0 and vy >
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testlimitVy1() {
		Vec2d vec1 = new Vec2d(2, LevelBuilder.MAX_EFISH_SPEED + 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVy();
		assertEquals(LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVy()} with vy &lt; 0 and vy &lt;
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testlimitVy2() {
		Vec2d vec1 = new Vec2d(2, -LevelBuilder.MAX_EFISH_SPEED - 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVy();
		assertEquals(-LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitSpeed()} with vx and vy > 0 and
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed1() {
		Vec2d vec1 = new Vec2d(LevelBuilder.MAX_EFISH_SPEED + 1, LevelBuilder.MAX_EFISH_SPEED + 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitSpeed();
		assertEquals(LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
		assertEquals(LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitSpeed()} with vx and vy &lt; 0 and minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed2() {
		Vec2d vec1 = new Vec2d(-LevelBuilder.MAX_EFISH_SPEED - 1, -LevelBuilder.MAX_EFISH_SPEED - 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitSpeed();
		assertEquals(-LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
		assertEquals(-LevelBuilder.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
}
