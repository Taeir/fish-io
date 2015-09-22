package com.github.fishio.logging;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * The class that should test methods that are the same in all implementations. 
 * 
 */
public abstract class TestIHandler {

	private IHandler handler = getIHandler();
	
	/**
	 * Return the correct handler object.
	 * @return
	 * 		the correct handler object.
	 */
	abstract IHandler getIHandler();
	
	/**
	 * Test the set and get method for the formatter.
	 */
	@Test
	public void testGetSetFormatter() {
		DefaultFormat df = new DefaultFormat();
		System.out.println(handler);
		handler.setFormat(df);
		assertSame(df, handler.getFormat());
	}

}
