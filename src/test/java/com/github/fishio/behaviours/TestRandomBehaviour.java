package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.EnemyFishFactory;
import com.github.fishio.Vec2d;

/**
 * Tests the RandomBehaviour class.
 */
public class TestRandomBehaviour {

	private RandomBehaviour behaviour;
	
	/**
	 * Initialises the RandomBehaviour field.
	 */
	@Before
	public void setUp() {
		behaviour = new RandomBehaviour(3.0, 5.0, 0.1);
	}
	
	/**
	 * Tests the getSpeedVector method.
	 */
	@Test
	public void testGetSpeedVector() {
		Vec2d vec1 = new Vec2d(3.0, 5.0);
		Vec2d vec2 = behaviour.getSpeedVector();
		assertEquals(vec1, vec2);
	}
	
	/**
	 * Test for the limitVx method with vx > 0 and vx >
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testLimitVx1() {
		behaviour = new RandomBehaviour(EnemyFishFactory.MAX_EFISH_SPEED + 1, 2, 0.1);
		
		behaviour.limitVx();
		
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vx &lt; 0 and vx &lt; minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testLimitVx2() {
		behaviour = new RandomBehaviour(-EnemyFishFactory.MAX_EFISH_SPEED - 1, 2, 0.1);
		
		behaviour.limitVx();
		
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vy > 0 and vy >
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testLimitVy1() {
		behaviour = new RandomBehaviour(2, EnemyFishFactory.MAX_EFISH_SPEED + 1, 0.1);
		
		behaviour.limitVy();
		
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vy &lt; 0 and vy &lt;
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testLimitVy2() {
		behaviour = new RandomBehaviour(2, -EnemyFishFactory.MAX_EFISH_SPEED - 1, 0.1);
		
		behaviour.limitVy();
		
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for the limitSpeed method with vx and vy > 0 and
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed1() {
		behaviour = new RandomBehaviour(EnemyFishFactory.MAX_EFISH_SPEED + 1,
				EnemyFishFactory.MAX_EFISH_SPEED + 1, 0.1);
		
		behaviour.limitSpeed();
		
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().y, 0.0);
		assertEquals(EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitSpeed method with vx and vy &lt; 0 and minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed2() {
		behaviour = new RandomBehaviour(-EnemyFishFactory.MAX_EFISH_SPEED - 1,
				-EnemyFishFactory.MAX_EFISH_SPEED - 1, 0.1);
		
		behaviour.limitSpeed();
		
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().x, 0.0);
		assertEquals(-EnemyFishFactory.MAX_EFISH_SPEED, behaviour.getSpeedVector().y, 0.0);
	}
	
}
