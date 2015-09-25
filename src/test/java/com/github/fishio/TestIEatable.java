package com.github.fishio;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for the default methods in IEatable.
 */
public class TestIEatable {
	private IEatable eatable;
	
	/**
	 * Before method for this class.
	 */
	@Before
	public void before() {
		eatable = new IEatable() {
			@Override
			public boolean canBeEatenBy(IEatable other) {
				return false;
			}
			@Override
			public void eat() {
			}
			@Override
			public double getSize() {
				return 0;
			}
		};
	}
	
	/**
	 * Tests {@link IEatable#eat(IEatable)}.
	 */
	@Test
	public void testDefaultEat() {
		IEatable other = Mockito.mock(IEatable.class);
		eatable.eat(other);
		Mockito.verify(other).eat();
	}

}
