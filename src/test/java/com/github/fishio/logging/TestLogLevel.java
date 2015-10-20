package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for {@link LogLevel}.
 */
public class TestLogLevel {
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel NONE.
	 */
	@Test
	public void testFromInt_NONE() {
		assertEquals(LogLevel.NONE, LogLevel.fromInt(0));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel ERROR.
	 */
	@Test
	public void testFromInt_ERROR() {
		assertEquals(LogLevel.ERROR, LogLevel.fromInt(1));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel WARNING.
	 */
	@Test
	public void testFromInt_WARNING() {
		assertEquals(LogLevel.WARNING, LogLevel.fromInt(2));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel INFO.
	 */
	@Test
	public void testFromInt_INFO() {
		assertEquals(LogLevel.INFO, LogLevel.fromInt(3));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel DEBUG.
	 */
	@Test
	public void testFromInt_DEBUG() {
		assertEquals(LogLevel.DEBUG, LogLevel.fromInt(4));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for LogLevel DEBUG.
	 */
	@Test
	public void testFromInt_TRACE() {
		assertEquals(LogLevel.TRACE, LogLevel.fromInt(5));
	}
	
	/**
	 * Test for {@link LogLevel#fromInt(int)} for non existing LogLevel.
	 */
	@Test
	public void testFromIntWrong() {
		assertEquals(LogLevel.NONE, LogLevel.fromInt(120));
	}
}
