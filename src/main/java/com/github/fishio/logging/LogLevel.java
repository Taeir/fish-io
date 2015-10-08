package com.github.fishio.logging;

/**
 * Enum for log levels. 
 *
 */
public enum LogLevel {

	NONE, ERROR, WARNING, INFO, DEBUG, TRACE;

	/**
	 * Converts a numeric log level to the enum.
	 * @param level
	 * 		the int value for a log level.
	 * @return
	 * 		the corresponding log level, NONE for invalid input.
	 */
	public static LogLevel fromInt(int level) {
		switch (level) {
			case 0: return NONE;
			case 1: return ERROR;
			case 2: return WARNING;
			case 3: return INFO;
			case 4: return DEBUG;
			case 5: return TRACE;
			default: return NONE;
		}
	}
}
