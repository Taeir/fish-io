package com.github.fishio.logging;

/**
 * Log handler that outputs in the console.
 */
public class ConsoleHandler implements IHandler {

	private IFormatter format = new DefaultFormat();
	
	/**
	 * Create ConsoleHandler with default formatter.
	 */
	public ConsoleHandler() { }
	
	/**
	 * Create ConsoleHandler with custom formatter.
	 * @param formatter
	 * 		The custom format the handler should adapt.
	 */
	public ConsoleHandler(IFormatter formatter) {
		format = formatter;
	}
	
	@Override
	public void output(LogLevel logLvl, String logMessage) {
		System.out.println(format.formatOutput(logLvl, logMessage));
	}

	@Override
	public void setFormat(IFormatter formatter) {
		format = formatter;
	}

	@Override
	public IFormatter getFormat() {
		return format;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result;
		if (format == null) {
			return result;
		} else {
			return result + format.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConsoleHandler other = (ConsoleHandler) obj;
		if (format == null) {
			if (other.format != null) {
				return false;
			}
		} else if (format.getClass() != other.format.getClass()) {
			return false;
		}
		return true;
	}
}
