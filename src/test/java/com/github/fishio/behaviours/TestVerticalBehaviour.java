package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.Vec2d;

/**
 * Tests the VerticalBehaviour class.
 */
public class TestVerticalBehaviour {

	private VerticalBehaviour behaviour;
	
	/**
	 * Initialises the behaviour field before each test case.
	 */
	@Before
	public void setUp() {
		behaviour = new VerticalBehaviour(3.5);
	}
	
	/**
	 * Tests the getSpeedVector method.
	 */
	@Test
	public void testGetSpeedVector() {
		// Because it's going from top to bottom, the speedVector.y should always
		// be the negative of the speed.
		assertEquals(new Vec2d(0, -3.5), behaviour.getSpeedVector());
	}
	
	/**
	 * Tests the preMove method.
	 */
	@Test
	public void testPreMove() {
		behaviour.preMove();
		
		// The speedVector should not have changed as a result of calling the preMove method.
		assertEquals(new Vec2d(0, -3.5), behaviour.getSpeedVector());
	}
	
	/**
	 * Test for {@link VerticalBehaviour#updateTo(IMoveBehaviour)}.
	 */
	@Test
	public void testUpdateTo() {
		behaviour.updateTo(new VerticalBehaviour(2.5));
		
		assertEquals(2.5D, behaviour.getSpeed(), 0D);
	}
	
	/**
	 * Test for {@link VerticalBehaviour#updateTo(IMoveBehaviour)}, when
	 * updating with an incompatible type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateTo2() {
		behaviour.updateTo(mock(IMoveBehaviour.class));
	}
	
}
