package com.github.fishio.logging;

/**
 * The logging class.
 * This class can be called from other classes to log events. The
 * output of this class is then send to a handler which handles the 
 * log and deposits it in the appropriate places.
 */
public final class Log {

	private static final Log LOG = new Log();
	
	/**
	 * Private constructor to prevent initialization.
	 */
	private Log() {
		//To prevent initialization.
	}
	
	/**
	 * Return logger instance.
	 * @return logging
	 */
	public static Log getLogger() {
		return LOG;
	}
	
	
}
