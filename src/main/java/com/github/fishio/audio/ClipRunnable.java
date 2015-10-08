package com.github.fishio.audio;

import java.io.IOException;

import javafx.beans.property.SimpleBooleanProperty;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Runnable that starts playing an audio clip, and blocks until it is
 * done playing.
 */
public class ClipRunnable implements Runnable {
	private FishClip clip;
	private SimpleBooleanProperty runningProperty = new SimpleBooleanProperty();
	
	/**
	 * Create a new ClipRunnable for the given clip.
	 * 
	 * @param clip
	 * 		the clip to play when this Runnable is ran.
	 */
	public ClipRunnable(FishClip clip) {
		this.clip = clip;
	}
	
	@Override
	public void run() {
		try {
			try {
				clip.getClip().start();
				runningProperty.set(true);
				clip.waitUntilDone();
			} finally {
				clip.close();
			}
		} catch (InterruptedException ex) {
			//Don't do anything if we are interrupted.
		} catch (IOException ex) {
			Log.getLogger().log(LogLevel.WARNING, "[AudioEngine] Error while closing clip!");
			ex.printStackTrace();
		} finally {
			runningProperty.set(false);
		}
	}
	
	/**
	 * @return
	 * 		the clip in this ClipRunnable.
	 */
	public FishClip getClip() {
		return clip;
	}
	
	/**
	 * Sets the clip of this ClipRunnable.<br>
	 * <br>
	 * If the current clip is still being played, it is stopped first.
	 * 
	 * @param clip
	 * 		the new clip to play.
	 */
	public void setClip(FishClip clip) {
		if (isRunning()) {
			stop();
		}
		
		this.clip = clip;
	}
	
	/**
	 * @return
	 * 		the running property.
	 * 
	 * @see #isRunning()
	 */
	public SimpleBooleanProperty getRunningProperty() {
		return runningProperty;
	}

	/**
	 * @return
	 * 		if this ClipRunnable is currently running.
	 */
	public boolean isRunning() {
		return runningProperty.get();
	}
	
	/**
	 * Stop this clip from running.
	 */
	public void stop() {
		clip.setDone();
	}
}
