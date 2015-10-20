package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.Vec2d;
import com.github.fishio.settings.Settings;

/**
 * Tests the RandomBehaviour class.
 */
public class TestRandomBehaviour {
	private final double maxSpeed = Settings.getInstance().getDouble("MAX_EFISH_SPEED");
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
		behaviour = new RandomBehaviour(maxSpeed + 1, 2, 0.1);
		
		behaviour.limitVx();
		
		assertEquals(maxSpeed, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vx &lt; 0 and vx &lt; minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testLimitVx2() {
		behaviour = new RandomBehaviour(-maxSpeed - 1, 2, 0.1);
		
		behaviour.limitVx();
		
		assertEquals(-maxSpeed, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vy > 0 and vy >
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testLimitVy1() {
		behaviour = new RandomBehaviour(2, maxSpeed + 1, 0.1);
		
		behaviour.limitVy();
		
		assertEquals(maxSpeed, behaviour.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for the limitVx method with vy &lt; 0 and vy &lt;
	 * MIN_EFISH_SPEED.
	 */
	@Test
	public void testLimitVy2() {
		behaviour = new RandomBehaviour(2, -maxSpeed - 1, 0.1);
		
		behaviour.limitVy();
		
		assertEquals(-maxSpeed, behaviour.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for the limitSpeed method with vx and vy > 0 and
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed1() {
		behaviour = new RandomBehaviour(maxSpeed + 1, maxSpeed + 1, 0.1);
		
		behaviour.limitSpeed();
		
		assertEquals(maxSpeed, behaviour.getSpeedVector().y, 0.0);
		assertEquals(maxSpeed, behaviour.getSpeedVector().x, 0.0);
	}
	
	/**
	 * Test for the limitSpeed method with vx and vy &lt; 0 and minus
	 * MAX_EFISH_SPEED.
	 */
	@Test
	public void testlimitSpeed2() {
		behaviour = new RandomBehaviour(-maxSpeed - 1, -maxSpeed - 1, 0.1);
		
		behaviour.limitSpeed();
		
		assertEquals(-maxSpeed, behaviour.getSpeedVector().x, 0.0);
		assertEquals(-maxSpeed, behaviour.getSpeedVector().y, 0.0);
	}
	
	/**
	 * Test for {@link RandomBehaviour#updateTo(IMoveBehaviour)}.
	 */
	@Test
	public void testUpdateTo() {
		behaviour.updateTo(new RandomBehaviour(2.0, 4.0, 0.2));
		
		assertEquals(2.0D, behaviour.getSpeedVector().x, 0D);
		assertEquals(4.0D, behaviour.getSpeedVector().y, 0D);
	}
	
	/**
	 * Test for {@link RandomBehaviour#updateTo(IMoveBehaviour)}, when
	 * updating with an incompatible type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateTo2() {
		behaviour.updateTo(mock(IMoveBehaviour.class));
	}
	
}
