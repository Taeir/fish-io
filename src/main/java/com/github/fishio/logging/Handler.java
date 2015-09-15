package com.github.fishio.logging;

/**
 * The handler class for logging.
 * This class handles the logging output.
 *
 */
public final class Handler {

	private static final Handler HANDLER = new Handler();
	//private boolean commandLineLog = true;
	
	/**
	 * Private constructor to prevent initialization.
	 */
	private Handler() {
		//To prevent initialization.
	}
	
	/**
	 * Return logger instance.
	 * @return logging
	 */
	public static Handler getHandler() {
		return HANDLER;
	}
	
	/**
	 * Output the log message in the appropriate channels.
	 * @param logLvl
	 * 		The Log Level of the log.
	 * @param message
	 * 		The log message of the log.
	 */
	public void outPut(LogLevel logLvl, String message) {
		//TODO
	}
}
