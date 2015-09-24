package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testing class for the Log class.
 *
 */
public class TestLog {

	private Log log; 
	private ConsoleHandler mockedHandler1;
	private ConsoleHandler mockedHandler2;
	private String test = "LogMessage";
	
	/**
	 * Initialize the logger and handler.
	 */
	@Before
	public void init() {
		mockedHandler1 = Mockito.mock(ConsoleHandler.class);
		mockedHandler2 = Mockito.mock(ConsoleHandler.class);
		log = Log.getLogger();
		//Clean up logger.
		log.removeAllHandlers();
		log.setLogLevel(LogLevel.WARNING);
	}
	
	/**
	 * Remove any remaining Handlers in the logger.
	 */
	@After
	public void breakDown() {
		log.removeAllHandlers();
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
	 * Test default handler array.
	 */
	@Test
	public void testNoHandler() {
		assertEquals(0, log.getHandler().size());
	}
	
	/**
	 * Test log method with no handlers.
	 * The method should exit without anything else happening if there are no handlers.
	 */
	@Test
	public void testLogNoHandlers() {
		log.log(LogLevel.ERROR, test);
	}
	
	/**
	 * Test addHandler method(1).
	 */
	@Test
	public void testAddHandler1() {
		log.addHandler(mockedHandler1);
		assertEquals(1, log.getHandler().size());
	}
	
	/**
	 * Test addHandler method(2).
	 */
	@Test
	public void testAddHandler2() {
		log.addHandler(mockedHandler1);
		log.addHandler(mockedHandler2);
		assertEquals(2, log.getHandler().size());
	}
	
	/**
	 * Test removeHandlers method(1).
	 */
	@Test
	public void testRemoveAllHandler1() {
		log.addHandler(mockedHandler1);
		log.removeAllHandlers();
		assertEquals(0, log.getHandler().size());
	}
	
	/**
	 * Test removeHandlers method(2).
	 */
	@Test
	public void testRemoveAllHandler2() {
		log.addHandler(mockedHandler1);
		log.addHandler(mockedHandler2);
		log.removeAllHandlers();
		assertEquals(0, log.getHandler().size());
	}
	
	/**
	 * Test log method with a single Handler and one log message.
	 */
	@Test
	public void testLogSingleHandlersSingleLog() {
		//Attach handler
		log.addHandler(mockedHandler1);
		log.log(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler1).output(LogLevel.ERROR, test);
	}
	
	/**
	 * Test log method with a single Handler and multiple log messages.
	 */
	@Test
	public void testLogSingleHandlersMultipleLogs() {
		//Attach handler
		log.addHandler(mockedHandler1);
		log.log(LogLevel.ERROR, test);
		log.log(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler1, times(2)).output(LogLevel.ERROR, test);
	}
	
	/**
	 * Test log method with a Multiple Handlers and one log message.
	 */
	@Test
	public void testLogMultipleHandlersSingleLog() {
		//Attach handlers
		log.addHandler(mockedHandler1);
		log.addHandler(mockedHandler2);
		log.log(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler1).output(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler2).output(LogLevel.ERROR, test);
	}
	
	/**
	 * Test log method with a Multiple Handlers and multiple log messages.
	 * And a too high log level.
	 */
	@Test
	public void testLogMultipleHandlersMultipleLogsLowLevel() {
		//Attach handlers
		log.addHandler(mockedHandler1);
		log.addHandler(mockedHandler2);
		
		log.log(LogLevel.DEBUG, test);
		log.log(LogLevel.DEBUG, test);
		Mockito.verify(mockedHandler1, never()).output(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler2, never()).output(LogLevel.ERROR, test);
	}
	
	/**
	 * Test log method with a Multiple Handlers and multiple log messages.
	 */
	@Test
	public void testLogMultipleHandlersMultipleLogs() {
		//Attach handlers
		log.addHandler(mockedHandler1);
		log.addHandler(mockedHandler2);
		
		log.log(LogLevel.ERROR, test);
		log.log(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler1, times(2)).output(LogLevel.ERROR, test);
		Mockito.verify(mockedHandler2, times(2)).output(LogLevel.ERROR, test);
	}
	
}
