package com.github.fishio.logging;

/**
 * Handler interface.
 * This interface is for handlers that interact with the log class.
 * 
 */
public interface IHandler {
	
	/**
	 * Outputs the log message.
	 * @param logLvl
	 * 		The log level of the log.
	 * @param logMessage
	 * 		The log message of the log.
	 */
	void output(LogLevel logLvl, String logMessage);
	
}
