package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class tests the EnemyFish class.
 */
public class TestEnemyFish extends TestIEatable {
	
	private BoundingBox bb1;
	private EnemyFish enemy1;
	
	/**
	 * An @Before method which creates an EnemyFish object which will be used in
	 * most of the tests.
	 */
	@Before
	public void createfishbefore() {
		bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		enemy1 = Mockito.spy(new EnemyFish(bb1, null, 3.0, 5.0));
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
		Vec2d vec1 = new Vec2d(EnemyFishFactory.MAX_EFISH_SPEED + 1, 2);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVx();
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVx()} with vx &lt; 0 and vx &lt; minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitVx2() {
		Vec2d vec1 = new Vec2d(-EnemyFishFactory.MAX_EFISH_SPEED - 1, 2);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVx();
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVy()} with vy > 0 and vy >
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testlimitVy1() {
		Vec2d vec1 = new Vec2d(2, EnemyFishFactory.MAX_EFISH_SPEED + 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVy();
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitVy()} with vy &lt; 0 and vy &lt;
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testlimitVy2() {
		Vec2d vec1 = new Vec2d(2, -EnemyFishFactory.MAX_EFISH_SPEED - 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitVy();
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitSpeed()} with vx and vy > 0 and
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed1() {
		Vec2d vec1 = new Vec2d(EnemyFishFactory.MAX_EFISH_SPEED + 1, EnemyFishFactory.MAX_EFISH_SPEED + 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitSpeed();
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for {@link EnemyFish#limitSpeed()} with vx and vy &lt; 0 and minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed2() {
		Vec2d vec1 = new Vec2d(-EnemyFishFactory.MAX_EFISH_SPEED - 1, -EnemyFishFactory.MAX_EFISH_SPEED - 1);
		enemy1.setSpeedVector(vec1);
		enemy1.limitSpeed();
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().x, 0.0);
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, enemy1.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Tests whether an EnemyFish moves or not when it has been frozen.
	 */
	@Test
	public void testFrozen() {
		enemy1.setFrozen(true);
		enemy1.setSpeedVector(new Vec2d(1, 1));
		assertEquals(new Vec2d(0, 0), enemy1.getSpeedVector());
	}
	
	/**
	 * Tests whether an EnemyFish moves or not when it has been frozen
	 * and then unfrozen again.
	 */
	@Test
	public void testUnFreeze() {
		enemy1.setFrozen(true);
		enemy1.setSpeedVector(new Vec2d(1, 1));
		
		enemy1.setFrozen(false);
		assertEquals(new Vec2d(1, 1), enemy1.getSpeedVector());
	}
	/**
	 * Tests {@link EnemyFish#eat()}.
	 */
	@Test
	public void testEat() {
		enemy1.eat();
		assertTrue(enemy1.isDead());
	}
	
	/**
	 * Tests {@link EnemyFish#kill()}.
	 */
	@Test
	public void testKill() {
		enemy1.kill();
		assertTrue(enemy1.isDead());
	}
	
	@Override
	public void testCanBeEatenBy() {
		testCanBeEatenByLarger();
		testCanBeEatenBySame();
		testCanBeEatenBySmaller();
	}
	
	/**
	 * Tests {@link EnemyFish#canBeEatenBy(IEatable)}.
	 * Test for larger enemy.
	 */
	public void testCanBeEatenByLarger() {
		IEatable other = Mockito.mock(IEatable.class);
		when(other.getSize()).thenReturn(10.0);
		assertTrue(enemy1.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link EnemyFish#canBeEatenBy(IEatable)}.
	 * Test with fish with same size.
	 */
	public void testCanBeEatenBySame() {
		IEatable other = Mockito.mock(IEatable.class);
		when(other.getSize()).thenReturn(4.0);
		assertFalse(enemy1.canBeEatenBy(other));
	}
	
	/**
	 * Tests {@link EnemyFish#canBeEatenBy(IEatable)}.
	 * Test with a smaller fish.
	 */
	public void testCanBeEatenBySmaller() {
		IEatable other = Mockito.mock(IEatable.class);
		when(other.getSize()).thenReturn(3.0);
		assertFalse(enemy1.canBeEatenBy(other));
	}
	
	@Override
	public void testGetSize() {
		assertEquals(4, enemy1.getSize(), 0.0000001D);
	}

	@Override
	public IEatable getTestObject() {
		return enemy1;
	}
}
