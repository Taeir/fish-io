package com.github.fishio.logging;

/**
 * Handler interface. Used for handlers that interact with the log class.
 */
public interface IHandler {
	
	/**
	 * Outputs the log message.
	 * 
	 * @param logLvl
	 * 		the log level of the log.
	 * @param logMessage
	 * 		the log message of the log.
	 */
	void output(LogLevel logLvl, String logMessage);
	
	/**
	 * Set the format that the handler should use.
	 * 
	 * @param format
	 * 		the formatter that should be used.
	 */
	void setFormat(IFormatter format);
	
	/**
	 * Get current format.
	 * @return
	 * 		Return current format.
	 */
	IFormatter getFormat();
	
	/**
	 * Equals method for IHandlers. 
	 * Handlers that are equal must be the same kind of IHandlers that have the same formatting.
	 * 
	 * @param that
	 * 		The other object.
	 * 
	 * @return
	 * 		whether handlers are equal.
	 */
	@Override
	boolean equals(Object that);
}
