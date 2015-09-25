package com.github.fishio;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test interface for the default methods in IEatable.
 */
public interface TestIEatable {
	
	/**
	 * @return
	 * 		The IEatable Object used for testing.
	 */
	IEatable getTestObject();
	
	/**
	 * Tests the canBeEatenBy method.
	 */
	@Test
	void testCanBeEatenBy();
	
	/**
	 * Tests the eat method.
	 */
	@Test
	void testEat();
	
	/**
	 * Tests {@link IEatable#eat(IEatable)}.
	 */
	@Test
	default void testEatOther() {
		IEatable eatable = getTestObject();
		IEatable other = Mockito.mock(IEatable.class);
		eatable.eat(other);
		Mockito.verify(other).eat();
	}
	
	/**
	 * Tests the getSize method.
	 */
	@Test
	void testGetSize();
}
