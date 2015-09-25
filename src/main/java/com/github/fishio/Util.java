package com.github.fishio;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
	
	/**
	 * Calls the given EventHandler with a newly constructed ActionEvent.
	 * 
	 * @param handler
	 * 		an EventHandler, can be <code>null</code>.
	 */
	public static void callEventHandler(EventHandler<ActionEvent> handler) {
		if (handler != null) {
			handler.handle(new ActionEvent());
		}
	}
}
