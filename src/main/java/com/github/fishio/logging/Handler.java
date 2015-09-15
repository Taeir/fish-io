package com.github.fishio.logging;

/**
 * The handler class for logging.
 * This class handles the logging output.
 *
 */
public final class Handler {

	private static final Handler HANDLER = new Handler();
	
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
}
