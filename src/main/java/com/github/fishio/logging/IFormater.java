package com.github.fishio.logging;

/**
 * Interface for Formater classes.
 * These classes create different formats.
 */
public interface IFormater {

	/**
	 * Returns a string format of the the log message.
	 * @param logLvl
	 * 		The log level of the log.
	 * @param message
	 * 		The log message of the log.
	 * @return
	 * 		String that is formated in a specific way.
	 */
	String format(LogLevel logLvl, String message);
	
}
