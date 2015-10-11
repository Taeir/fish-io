package com.github.fishio.audio;

import java.io.IOException;
import java.util.Random;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * A class representing a runnable for background music.
 */
public class BackgroundMusic implements Runnable {
	
	private Random random = new Random();
	private int last = -1;
	private SimpleBooleanProperty stopProperty = new SimpleBooleanProperty();
	private SimpleBooleanProperty runningProperty = new SimpleBooleanProperty();
	private Thread thread;
	
	/**
	 * Create a new BackgroundMusicRunnable.
	 */
	public BackgroundMusic() {
		thread = new Thread(this);
	}
	
	/**
	 * Create a new BackgroundMusicRunnable.
	 * 
	 * @param stopProperty
	 * 		an observable value that, when set to true, stops this
	 * 		background music runnable.
	 */
	public BackgroundMusic(ObservableValue<Boolean> stopProperty) {
		this();
		
		stopProperty.addListener((o, oVal, nVal) -> {
			if (nVal) {
				this.stopProperty.set(true);
			}
		});
	}
	
	/**
	 * Starts the background music.<br>
	 * <br>
	 * If it is already running (and not stopping), this method will cause
	 * a switch to a different song.
	 */
	public void start() {
		//If not stopping and already running, we call interrupt to switch to a new song
		if (!stopProperty.get() && runningProperty.get()) {
			thread.interrupt();
			return;
		}
		
		stopProperty.set(false);
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Stops the background music.
	 */
	public void stop() {
		stopProperty.set(true);
		thread.interrupt();
	}
	
	@Override
	public void run() {
		runningProperty.set(true);
		
		try {
			while (!stopProperty.get()) {
				FishClip clip = getRandomClip();
				
				//If the clip is invalid, we sleep for a second and try again.
				if (clip == null) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException ex) {
						//Don't do anything if we are interrupted here.
					}
					
					continue;
				}
				
				try {
					clip.getClip().start();
					clip.waitUntilDone();
				} catch (InterruptedException ex) {
					//If we should stop, we do
					if (stopProperty.get()) {
						break;
					}
				} finally {
					try {
						clip.close();
					} catch (IOException ex) {
						Log.getLogger().log(LogLevel.WARNING, "[Background Music] Error while closing clip!");
						ex.printStackTrace();
					}
				}
			}
		} finally {
			runningProperty.set(false);
		}
	}
	
	/**
	 * @return
	 * 		a randomly selected FishClip.
	 */
	private FishClip getRandomClip() {
		IAudioFactory iaf = AudioEngine.getInstance().getAudioFactory();
		
		//Select a random song from the AudioFactory.
		while (iaf.getAllMusic().size() > 1) {
			int nr = random.nextInt(iaf.getAllMusic().size());
			if (nr != last) {
				last = nr;
				return iaf.createMusicClip(nr);
			}
		}
		
		//If we only have 1 clip, we simply return it.
		if (iaf.getAllMusic().size() == 1) {
			return iaf.createMusicClip(0);
		}
		
		//We end up here if clips is empty.
		return null;
	}
}
