package com.github.fishio.test.util;

import static org.mockito.Mockito.mock;

import java.util.HashSet;

import org.mockito.Mockito;

import com.github.fishio.logging.IHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Utility class for testing.
 */
public final class TestUtil {
	private static HashSet<IHandler> handlers = new HashSet<>();
	private static LogLevel level = LogLevel.WARNING;
	private static IHandler mockedHandler = mock(IHandler.class);
	private static boolean logTestMode = false;
	
	private TestUtil() { }
	
	/**
	 * @param object
	 * 		the object to check
	 * 
	 * @return
	 * 		if the given object is a mock.
	 */
	public static boolean isMock(Object object) {
		return Mockito.mockingDetails(object).isMock();
	}
	
	/**
	 * Sets up the logger for testing.
	 * 
	 * @param level
	 * 		the level to log at.
	 * 
	 * @see #restoreLogger()
	 */
	public static void setUpLoggerForTesting(LogLevel level) {
		Log log = Log.getLogger();
		
		//If already in log test mode, we only set the level and reset the mocked handler.
		if (logTestMode) {
			log.setLogLevel(level);
			return;
		}
		
		//Store the old handlers and level
		TestUtil.handlers.addAll(log.getHandler());
		TestUtil.level = log.getLogLevel();
		
		//Set the new handlers and level
		log.setLogLevel(level);
		log.getHandler().clear();
		log.addHandler(mockedHandler);
		
		logTestMode = true;
	}
	
	/**
	 * Restores the logger after testing.
	 * 
	 * @see #setUpLoggerForTesting(LogLevel)
	 */
	public static void restoreLogger() {
		//If not in log test mode, we do nothing
		if (!logTestMode) {
			return;
		}
		
		Log log = Log.getLogger();
		
		//Restore the level and the handlers
		log.setLogLevel(level);
		log.getHandler().clear();
		log.getHandler().addAll(handlers);
		
		//Reset the mocked handler.
		resetMockHandler();
		
		//Restore our fields to defaults
		handlers.clear();
		level = LogLevel.WARNING;
		
		logTestMode = false;
	}
	
	/**
	 * The mocked handler can be used to verify that certain messages were
	 * logged.<br>
	 * <br>
	 * Be sure that {@link #setUpLoggerForTesting(LogLevel)} has been
	 * called before trying to use this mocked handler.
	 * 
	 * @return
	 * 		the mocked log handler.
	 */
	public static IHandler getMockHandler() {
		return mockedHandler;
	}
	
	/**
	 * Resets the mocked log handler.<br>
	 * <br>
	 * This method should be called in the &#64;Before method.
	 */
	public static void resetMockHandler() {
		Mockito.reset(mockedHandler);
	}
}
