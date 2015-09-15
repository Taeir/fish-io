package com.github.fishio.logging;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 * Test class for ConsoleHandler.
 *
 */
public class TestConsoleHandler extends TestIHandler {

	private ConsoleHandler handler;
	private IFormatter formatter;
	
	//TODO add factory methods for get and set methods
	
	/**
	 * Set up Mockito and handler.
	 */
	@Before
	public void setUp() {
		formatter = Mockito.mock(DefaultFormat.class);
		when(formatter.format(LogLevel.ERROR, "Test")).thenReturn("Test Output");
		handler = new ConsoleHandler();
	}
	
	/**
	 * Test if output calls correct method.
	 */
	@Test
	public void testOutput() {
		handler.setFormat(formatter);
		handler.output(LogLevel.ERROR, "Test");
		Mockito.verify(formatter).format(LogLevel.ERROR, "Test");
	}

}
