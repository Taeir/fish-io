package com.github.fishio.logging;

/**
 * The logging class.
 * This class can be called from other classes to log events. The
 * output of this class is then send to a handler which handles the 
 * log.
 */
public final class Log {

	private static final Log LOG = new Log();
	private LogLevel logLevel = LogLevel.ERROR;
	
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
	/**
	 * Set the lowest log level.
	 * @param logLvl
	 * 		The lowest log level that should be set.
	 */
	public void setLogLevel(LogLevel logLvl) {
		logLevel = logLvl;
	}
	
	/**
	 * Log this message.
	 * Checks whether the log level is set low enough for the message to pass.
	 * 
	 * @param logLevel
	 * 		The log level of the log.
	 * @param logMessage
	 * 		The log message of the log.
	 */
	public void log(LogLevel logLvl, String logMessage) {
		//TODO
	}
	
}
