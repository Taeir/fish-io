package com.github.fishio.logging;

/**
 * The logging class.
 * This class can be called from other classes to log events. The
 * output of this class is then send to a handler which handles the 
 * log and deposits it in the appropriate places.
 */
public class Log {

	private static final Log log = new Log();
	
	/**
	 * Private constructor to prevent initialization,
	 */
	private Log() {}
	
	/**
	 * Return logger instance.
	 * @return logging
	 */
	public static Log getLogger() {
		return log;
	}
}
