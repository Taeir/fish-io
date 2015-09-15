package com.github.fishio.logging;

/**
 * The default format class.
 * Contains a default format for logging.
 */
public class DefaultFormat implements IFormatter {

	/**
	 * Constructor.
	 * Creates DefaultFormat.
	 */
	public DefaultFormat() { }
	
	@Override
	public String format(LogLevel logLvl, String logMessage) {
		return "[" + logLvl.toString() + "]:- " + logMessage;
	}

}
