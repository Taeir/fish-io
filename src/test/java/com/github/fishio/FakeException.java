package com.github.fishio;

import java.io.PrintStream;

/**
 * Fake exception class that is used by tests.<br>
 * This exception has no stack trace when printed.
 */
public class FakeException extends RuntimeException {
	private static final long serialVersionUID = 756350476830801027L;

	/**
	 * @param msg
	 * 		the message to use for the exception.
	 */
	public FakeException(String msg) {
		super(msg, null, true, true);
	}
	
	@Override
	public void printStackTrace(PrintStream s) {
		setStackTrace(new StackTraceElement[0]);
		super.printStackTrace(s);
	}
}
