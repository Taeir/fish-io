package com.github.fishio.logging;

import static org.junit.Assert.assertSame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * The class that should test methods that are the same in all implementations. 
 * 
 */
public abstract class TestIHandler {
	
	/**
	 * Create temporary folder for testing.
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	/**
	 * Return the correct handler object.
	 * @return
	 * 		the correct handler object.
	 */
	public abstract IHandler getIHandler();
	
	/**
	 * Test the set and get method for the formatter.
	 */
	@Test
	public void testGetSetFormatter() {
		IHandler handler = getIHandler();
		DefaultFormat df = new DefaultFormat();
		System.out.println(handler);
		handler.setFormat(df);
		assertSame(df, handler.getFormat());
	}

}
