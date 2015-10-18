package com.github.fishio.logging;

import java.util.ArrayList;

/**
 * The logging class.
 * This class can be called from other classes to log events. The
 * output of this class is then send to a handler which handles the 
 * log.
 */
public final class Log {

	private static final Log LOGGER = new Log();
	private LogLevel logLevel = LogLevel.WARNING;
	private ArrayList<IHandler> handlers = new ArrayList<IHandler>();
	
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
		return LOGGER;
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
	 * Get the set log Level.
	 * @return 
	 * 		The current log level.
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	/**
	 * Add a unique handler to the logging. 
	 * @param handler
	 * 		The handler to be added.
	 */
	public void addHandler(IHandler handler) {
		handlers.add(handler);
	}
	
	/**
	 * Return current Handler.
	 * @return
	 * 		Current handler, that might be null if not set.
	 */
	public ArrayList<IHandler> getHandler() {
		return handlers;
	}
	
	/**
	 * Reset handler ArrayList.
	 */
	public void removeAllHandlers() {
		handlers = new ArrayList<IHandler>();
	}
	
	/**
	 * Log this message.
	 * Checks whether the log level is set low enough for the message to pass.
	 * 
	 * @param logLvl
	 * 		The log level of the log.
	 * @param logMessage
	 * 		The log message of the log.
	 */
	public void log(LogLevel logLvl, String logMessage) {
		// If handler is not set the application will not log.
		if (handlers.size() == 0) {
			return;
		}
		// Check log level
		if (logLvl.ordinal() <= logLevel.ordinal()) {
			// Activate all attached handlers
			for (IHandler handler : handlers) {
				handler.output(logLvl, logMessage);
			}
		}
	}
	
	/**
	 * Log this exception.
	 * Checks whether the log level is set low enough for the message to pass.
	 * 
	 * @param logLvl
	 * 		The log level of the log.
	 * @param exception
	 * 		the exception to log.
	 */
	public void log(LogLevel logLvl, Exception exception) {
		// If handler is not set the application will not log.
		if (handlers.size() == 0) {
			return;
		}
		
		// Check log level
		if (logLvl.ordinal() <= logLevel.ordinal()) {
			// Activate all attached handlers
			for (IHandler handler : handlers) {
				handler.output(logLvl, exception);
			}
		}
	}
	
}
