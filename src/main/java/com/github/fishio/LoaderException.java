package com.github.fishio;

/**
 * Loader Exception for the preloader.
 */
public class LoaderException extends RuntimeException {
	private static final long serialVersionUID = -1280370704024661386L;

	/**
	 * @param msg
	 * 		the exception message.
	 * @param cause
	 * 		the cause for the exception.
	 */
	public LoaderException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	/**
	 * @param msg
	 * 		the exception message.
	 */
	public LoaderException(String msg) {
		super(msg);
	}
}
