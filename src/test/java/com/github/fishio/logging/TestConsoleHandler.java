package com.github.fishio.logging;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for ConsoleHandler.
 *
 */
public class TestConsoleHandler extends TestIHandler {

	private ConsoleHandler handler;
	
	//TODO add factory methods for get and set methods
	
	/**
	 * Set up Mockito and handler.
	 */
	@Before
	public void setUp() {
		handler = new ConsoleHandler();
	}
	
	/**
	 * Test initialization with default formatter.
	 * Test formatter.
	 */
	@Test
	public void testConsoleHandlerDefault() {
		assertTrue(handler.getFormat() instanceof DefaultFormat);
	}
	
	/**
	 * Test if output calls correct method.
	 */
	@Test
	public void testOutput() {
		IFormatter formatter = Mockito.mock(DefaultFormat.class);
		when(formatter.formatOutput(LogLevel.ERROR, "Test")).thenReturn("Test Output");
		
		handler.setFormat(formatter);
		handler.output(LogLevel.ERROR, "Test");
		Mockito.verify(formatter).formatOutput(LogLevel.ERROR, "Test");
	}

	/**
	 * Test Hashcode formatter null.
	 */
	@Test
	public void testHashcodeNull() {
		handler.setFormat(null);
		assertEquals(31, handler.hashCode());
	}
	
	/**
	 * Test Hashcode formatter with default.
	 */
	@Test
	public void testHashcodeDefault() {
		assertEquals(31 + handler.getFormat().hashCode(), handler.hashCode());
	}
	
	/**
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		assertEquals(handler, handler);
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsNull() {
		assertFalse(handler.equals(null));
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsOtherClass() {
		assertFalse(handler.equals(new Double(1.0)));
	}
	
	/**
	 * Test equals with null formatter.
	 */
	@Test
	public void testEqualsNullFormatter1() {
		ConsoleHandler ch2 = new ConsoleHandler();
		handler.setFormat(null);
		assertFalse(handler.equals(ch2));
	}
	
	/**
	 * Test equals with double null formatter.
	 */
	@Test
	public void testEqualsNullFormatter2() {
		ConsoleHandler ch2 = new ConsoleHandler(null);
		handler.setFormat(null);
		assertEquals(handler, ch2);
	}
	
	
	
	/**
	 * Test equals with different formatter in second object.
	 */
	@Test
	public void testEqualsDifferentFormatter() {
		ConsoleHandler ch2 = new ConsoleHandler(new TimeStampFormat());
		assertFalse(handler.equals(ch2));
	}
	
	/**
	 * Test equals with equal ConsoleHandler.
	 */
	@Test
	public void testEqualsEqual() {
		ConsoleHandler ch2 = new ConsoleHandler();
		assertEquals(handler, ch2);
	}

	@Override
	public IHandler getIHandler() {
		return new ConsoleHandler();
	}
}
