package com.github.fishio.logging;

/**
 * Interface for Formater classes.
 * These classes create different formats.
 */
public interface IFormatter {

	/**
	 * Returns a string format of the the log message.
	 * @param logLvl
	 * 		The log level of the log.
	 * @param logMessage
	 * 		The log message of the log.
	 * @return
	 * 		String that is formated in a specific way.
	 */
	String format(LogLevel logLvl, String logMessage);
	
}
