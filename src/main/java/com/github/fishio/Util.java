package com.github.fishio;

import javafx.application.Platform;

/**
 * Utility class.
 */
public final class Util {
	/**
	 * Private constructor for Utility Class.
	 */
	private Util() { }
	
	/**
	 * Runs the given runnable on the JavaFX application thread.
	 * 
	 * @param runnable
	 * 		the runnable to run.
	 */
	public static void onJavaFX(Runnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			Platform.runLater(runnable);
		}
	}
}
