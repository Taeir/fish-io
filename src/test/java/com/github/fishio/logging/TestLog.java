package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Testing class for the Log class.
 *
 */
public class TestLog {

	private static Log log; 
	
	/**
	 * initialize the logger.
	 */
	@BeforeClass
	public static void init() {
		log = Log.getLogger();
	}
	
	/**
	 * Test initialize.
	 */
	@Test
	public void testInitialize() {
		assertNotNull(Log.getLogger());
	}
	
	/**
	 * Test default LogLevel.
	 */
	@Test
	public void testDefaultLogLevel() {
		assertEquals(LogLevel.WARNING, log.getLogLevel());
	}
	
	/**
	 * Test default LogLevel.
	 */
	@Test
	public void testSetGetLogLevel() {
		log.setLogLevel(LogLevel.ERROR);
		assertEquals(LogLevel.ERROR, log.getLogLevel());
	}
}
