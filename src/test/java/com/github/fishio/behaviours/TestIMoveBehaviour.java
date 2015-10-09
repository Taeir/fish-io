package com.github.fishio.behaviours;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.Vec2d;

/**
 * Tests the default methods of IMoveBehaviour interface.
 */
public class TestIMoveBehaviour {

	/**
	 * Tests the getSpeed method.
	 */
	@Test
	public void testGetSpeed() {
		IMoveBehaviour behaviour = Mockito.spy(new FrozenBehaviour());
		
		Mockito.when(behaviour.getSpeedVector()).thenReturn(new Vec2d(-3, 5));
		
		assertEquals((new Vec2d(-3, 5)).length(), behaviour.getSpeed(), 0.0D);
	}
	
}
