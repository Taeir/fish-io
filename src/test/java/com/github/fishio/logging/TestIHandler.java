package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * The class that should test methods that are the same in all implementations. 
 * 
 */
public abstract class TestIHandler {

	private IHandler handler;
	
	/**
	 * Test the set and get method for the formatter.
	 */
	@Test
	public void testGetSetFormatter() {
		IFormatter formatter = Mockito.mock(DefaultFormat.class);
		when(formatter.formatOutput(LogLevel.ERROR, "Test")).thenReturn("Test Output");
		
		handler.setFormat(formatter);
		assertEquals(formatter, handler.getFormat());
	}

}
