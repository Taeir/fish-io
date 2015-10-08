package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.settings.Settings;

/**
 * This class tests the EnemyFish class.
 */
public class TestEnemyFish extends TestIEatable {
	
	private final double maxSpeed = Settings.getInstance().getDouble("MAX_EFISH_SPEED");
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
	 * Tests {@link EnemyFish#eat()}.
	 */
	@Test
	@Override
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
