package com.github.fishio.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The time stamp format class.
 * Contains a default format for logging that adds time stamps to logs.
 */
public class TimeStampFormat implements IFormatter {

	/**
	 * Constructor.
	 * Creates DefaultFormat.
	 */
	public TimeStampFormat() { }
	
	@Override
	public String formatOutput(LogLevel logLvl, String logMessage) {
		return "[" + getTimeStamp() + "] [" + logLvl.toString() + "]:-\t" + logMessage;
	}

	/**
	 * Method that returns a string time stamp of current system time.
	 * @return
	 * 		Current System Time.
	 */
	public String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());		
	}
}

