package com.github.fishio;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test interface for the default methods in IEatable.
 */
public abstract class TestIEatable {
	
	/**
	 * @return
	 * 		The IEatable Object used for testing.
	 */
	public abstract IEatable getTestObject();
	
	/**
	 * Tests the canBeEatenBy method.
	 */
	@Test
	public abstract void testCanBeEatenBy();
	
	/**
	 * Tests the eat method.
	 */
	@Test
	public abstract void testEat();
	
	/**
	 * Tests {@link IEatable#eat(IEatable)}.
	 */
	@Test
	public void testEatOther() {
		IEatable eatable = getTestObject();
		IEatable other = Mockito.mock(IEatable.class);
		eatable.eat(other);
		Mockito.verify(other).eat();
	}
	
	/**
	 * Tests the getSize method.
	 */
	@Test
	public abstract void testGetSize();
}
