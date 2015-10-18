package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

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
	public void testPreMove() {
		behaviour.preMove();
		
		// The speedVector should not have changed as a result of calling the preMove method.
		assertEquals(new Vec2d(0, 0), behaviour.getSpeedVector());
	}
	
	/**
	 * Test for {@link FrozenBehaviour#updateTo(IMoveBehaviour)}.
	 */
	@Test
	public void testUpdateTo() {
		behaviour.updateTo(new FrozenBehaviour());
		
		//Nothing should happen.
	}
	
	/**
	 * Test for {@link FrozenBehaviour#updateTo(IMoveBehaviour)}, when
	 * updating with an incompatible type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateTo2() {
		behaviour.updateTo(mock(IMoveBehaviour.class));
	}
	
	/**
	 * Test for {@link FrozenBehaviour#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		assertEquals(0, behaviour.hashCode());
	}
	
	/**
	 * Test for {@link FrozenBehaviour#equals(Object)}.
	 */
	@Test
	public void testEqualsSelf() {
		assertEquals(behaviour, behaviour);
	}
	
	/**
	 * Test for {@link FrozenBehaviour#equals(Object)}.
	 */
	@Test
	public void testEqualsNull() {
		assertNotEquals(behaviour, null);
	}
	
	/**
	 * Test for {@link FrozenBehaviour#equals(Object)}.
	 */
	@Test
	public void testEquals() {
		//All frozen behaviours should be equal.
		assertEquals(new FrozenBehaviour(), new FrozenBehaviour());
	}
	
}
