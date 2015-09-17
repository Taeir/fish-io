package com.github.fishio.gui;

import javafx.application.Platform;
import javafx.scene.Scene;

import com.github.fishio.Preloader;

/**
 * ScreenSwitcher that can be used to switch screens from threads other than the
 * JavaFX thread.
 */
public class ScreenSwitcher {
	/**
	 * The default time to sleep for between checking again when waiting
	 * until the screen switch is done.
	 */
	private static final long DEFAULT_GRANULARITY = 50L;
	
	private final String screen;
	private final int transition;
	private final long granularity;
	
	private volatile boolean queued = false;
	private volatile boolean done = false;
	private volatile boolean fail = false;
	private volatile Exception exception;
	private volatile Scene scene;
	
	
	/**
	 * Creates a new ScreenSwitcher with the default granularity
	 * ({@link #DEFAULT_GRANULARITY}).
	 * 
	 * @param screen
	 * 		the screen to switch to.
	 * @param transition
	 * 		The length in milliseconds of the FadeTransition.
	 */
	public ScreenSwitcher(String screen, int transition) {
		this(screen, transition, DEFAULT_GRANULARITY);
	}
	
	/**
	 * Creates a new ScreenSwitcher.
	 * 
	 * @param screen
	 * 		the screen to switch to.
	 * @param transition
	 * 		The length in milliseconds of the FadeTransition.
	 * @param granularity
	 * 		the amount of time to sleep for between checking if we are done.
	 */
	public ScreenSwitcher(String screen, int transition, long granularity) {
		this.screen = screen;
		this.transition = transition;
		this.granularity = granularity;
	}
	
	/**
	 * Queues up the screen switch with {@link Platform#runLater(Runnable)}.<br>
	 * <br>
	 * If this screen switch has already been queued, this method will have
	 * no effect.
	 */
	public void queueSwitch() {
		if (queued) {
			return;
		}
		
		queued = true;
		Platform.runLater(() -> {
			try {
				scene = Preloader.switchTo(screen, transition);
			} catch (Exception ex) {
				fail = true;
				exception = ex;
				throw ex;
			} finally {
				done = true;
			}
		});
	}
	
	/**
	 * Waits until the screen switching is done, and returns it's success.
	 * 
	 * @return
	 * 		<code>true</code> if succeeded, <code>false</code> if an exception
	 * 		was thrown while executing this screen switch.
	 * 		
	 * @throws InterruptedException
	 * 		if this thread is interrupted while waiting.
	 */
	public boolean waitUntilDone() throws InterruptedException {
		if (done) {
			return fail;
		}
		
		//If not yet queued, we queue it.
		if (!queued) {
			queueSwitch();
		}
		
		//We wait until we are done.
		while (!done) {
			Thread.sleep(granularity);
		}
		
		return fail;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if the screen switching is done,
	 * 		<code>false</code> otherwise.
	 */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if an exception occurred while switching
	 * 		screens, <code>false</code> otherwise.
	 * 
	 * @see #getException()
	 */
	public boolean failed() {
		return fail;
	}

	/**
	 * @return
	 * 		the exception that was thrown while switching the sreen.
	 * 		If {@link #failed()} returns <code>false</code>, this method 
	 * 		will return <code>null</code>.
	 * 
	 * @see #failed()
	 */
	public Exception getException() {
		return exception;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this screen switch is queued and not done,
	 * 		<code>false</code> otherwise.
	 */
	public boolean isQueued() {
		return queued && !done;
	}
	
	/**
	 * Resets the queued status and the scene, so that this screen switch 
	 * can be queued again.
	 */
	public void reset() {
		queued = false;
		scene = null;
	}
	
	/**
	 * @return
	 * 		the scene of the screen that was switched to, if this screen
	 * 		switch is done. <code>null</code> otherwise.
	 */
	public Scene getScene() {
		return scene;
	}
}
