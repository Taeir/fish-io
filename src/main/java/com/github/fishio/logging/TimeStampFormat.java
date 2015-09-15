package com.github.fishio.logging;

/**
 * The time stamp format class.
 * Contains a default format for logging that adds timestamps to loggs.
 */
public class TimeStampFormat implements IFormatter {

	/**
	 * Constructor.
	 * Creates DefaultFormat.
	 */
	public TimeStampFormat() { }
	
	@Override
	public String format(LogLevel logLvl, String logMessage) {
		//TODO get system time and make it a good format
		return "NOTDONE[" + logLvl.toString() + "]:- " + logMessage;
	}

}

