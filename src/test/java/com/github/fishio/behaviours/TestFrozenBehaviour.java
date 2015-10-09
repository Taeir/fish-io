package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.Vec2d;

/**
 * Tests the FrozenBehaviour class.
 */
public class TestFrozenBehaviour {

	private FrozenBehaviour behaviour;
	
	/**
	 * Initialises the behaviour field before each test case.
	 */
	@Before
	public void setUp() {
		behaviour = new FrozenBehaviour();
	}
	
	/**
	 * Tests the getSpeedVector method.
	 */
	@Test
	public void testGetSpeedVector() {
		// The speedVector should always be 0, 0.
		assertEquals(new Vec2d(0, 0), behaviour.getSpeedVector());
	}
	
	/**
	 * Tests the preMove method.
	 */
	@Test
	public void preMove() {
		behaviour.preMove();
		
		// The speedVector should not have changed as a result of calling the preMove method.
		assertEquals(new Vec2d(0, 0), behaviour.getSpeedVector());
	}
	
}
