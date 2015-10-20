package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.Vec2d;

import javafx.scene.Scene;

/**
 * Test class for the KeyListenerBehaviour class.
 */
public class TestKeyListenerBehaviour {

	private KeyListenerBehaviour behaviour;
	
	/**
	 * Initialises the behaviour field before every test.
	 */
	@Before
	public void setUp() {
		this.behaviour = spy(new KeyListenerBehaviour(mock(Scene.class), 
				null, null, null, null, 0.1, 4));
	}
	
	/**
	 * Tests the getSpeedVector method. 
	 */
	@Test
	public void testGetSpeedVector() {
		// The initial speedVector should be 0, 0.
		assertEquals(new Vec2d(0, 0), behaviour.getSpeedVector());
	}
	
	/**
	 * Tests the preMove method.
	 */
	@Test
	public void testPreMove() {
		behaviour.preMove();
		
		verify(behaviour).adjustXSpeed();
		verify(behaviour).adjustYSpeed();
	}
	
	/**
	 * Tests the getAcceleration method.
	 */
	@Test
	public void testGetAcceleration() {
		assertEquals(0.1, behaviour.getAcceleration(), 0.0D);
	}
	
	/**
	 * Tests the setAcceleration method.
	 */
	@Test
	public void testSetAcceleration() {
		behaviour.setAcceleration(0.2);
		
		assertEquals(0.2, behaviour.getAcceleration(), 0.0D);
	}
	
	/**
	 * Tests the getMaxSpeed method.
	 */
	@Test
	public void testGetMaxSpeed() {
		assertEquals(4.0, behaviour.getMaxSpeed(), 0.0D);
	}
	
	/**
	 * Tests the setMaxSpeed method.
	 */
	@Test
	public void testSetMaxSpeed() {
		behaviour.setMaxSpeed(3.0);
		
		assertEquals(3.0, behaviour.getMaxSpeed(), 0.0D);
	}
	
	/**
	 * Test for {@link KeyListenerBehaviour#updateTo(IMoveBehaviour)}.
	 */
	@Test
	public void testUpdateTo() {
		behaviour.updateTo(new KeyListenerBehaviour(0.2, 3.0));
		
		assertEquals(0.2D, behaviour.getAcceleration(), 0.0D);
		
		//Max speed should not have changed
		assertEquals(4.0D, behaviour.getMaxSpeed(), 0.0D);
	}
	
	/**
	 * Test for {@link KeyListenerBehaviour#updateTo(IMoveBehaviour)}, when
	 * updating with an incompatible type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateTo2() {
		behaviour.updateTo(mock(IMoveBehaviour.class));
	}
	
}
