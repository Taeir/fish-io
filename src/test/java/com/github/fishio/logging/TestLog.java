package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
/**
 * Testing class for the Log class.
 *
 */
public class TestLog {

	private Log log; 
	private Handler handle;
	
	/**
	 * Initialize the logger and handler.
	 */
	@Before
	public void init() {
		log = Log.getLogger();
		handle = Handler.getHandler();
		log.setHandler(null);
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
	 * Test set and get of log level.
	 */
	@Test
	public void testSetGetLogLevel() {
		log.setLogLevel(LogLevel.ERROR);
		assertEquals(LogLevel.ERROR, log.getLogLevel());
	}
	
	/**
	 * Test default handler.
	 */
	@Test
	public void testDefaultHandler() {
		assertEquals(null, log.getHandler());
	}
	
	/**
	 * Test set and get handler.
	 */
	@Test
	public void testSetGetHandler() {
		log.setHandler(handle);
		assertSame(handle, log.getHandler());
	}
	
}
