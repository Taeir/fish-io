package com.github.fishio.logging;

/**
 * The logging class.
 * This class can be called from other classes to log events. The
 * output of this class is then send to a handler which handles the 
 * log.
 */
public final class Log {

	private static final Log LOG = new Log();
	private LogLevel logLevel = LogLevel.WARNING;
	private Handler handler;
	
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
	 * Get the set log Level.
	 * @return 
	 * 		The current log level.
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	/**
	 * Set which handler should handle the log output.
	 * @param handlr
	 * 		Handler of log output to be set.
	 */
	public void setHandler(Handler handlr) {
		handler = handlr;
	}
	
	/**
	 * Return current Handler.
	 * @return
	 * 		Current handler, that might be null if not set.
	 */
	public Handler getHandler() {
		return handler;
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
		if (handler == null) {
			return;
		}
		// Check log level
		if (logLvl.ordinal() <= logLevel.ordinal()) {
			handler.outPut(logLvl, logMessage);
		}
	}
	
}
