package com.github.fishio.logging;

import java.io.BufferedWriter;
import java.io.IOException;

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
public class TestTxtFileHandler extends TestIHandler {

	private TxtFileHandler handler;
	
	//TODO add factory methods for get and set methods
	
	/**
	 * Set up handler.
	 */
	@Before
	public void setUp() {
		handler = new TxtFileHandler("test");
	}
	
	/**
	 * Test if output calls correct method.
	 */
	@Test
	public void testOutput() {
		IFormatter formatter = Mockito.mock(DefaultFormat.class);
		when(formatter.formatOutput(LogLevel.ERROR, "Test")).thenReturn("Test Output");
		BufferedWriter mockedBW = Mockito.mock(BufferedWriter.class);
		
		handler.setFormat(formatter);
		handler.setBufferedWriter(mockedBW);
		handler.output(LogLevel.ERROR, "Test");
		Mockito.verify(formatter).formatOutput(LogLevel.ERROR, "Test");
		try {
			Mockito.verify(mockedBW).write("Test Output");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test Hashcode formatter null.
	 */
	//@Test
	public void testHashcodeNull() {
		handler.setFormat(null);
		assertEquals(31, handler.hashCode());
	}
	
	/**
	 * Test Hashcode formatter with default.
	 */
	//@Test
	public void testHashcodeDefault() {
		assertEquals(31 + handler.getFormat().hashCode(), handler.hashCode());
	}
	
	/**
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		assertTrue(handler.equals(handler));
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
	
	
}
