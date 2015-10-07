package com.github.fishio.audio;

import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;

/**
 * A decorator for {@link Clip}, which adds functionality to check if the
 * clip is done playing.
 */
public class FishClip implements LineListener, AutoCloseable {
	private Clip clip;
	private volatile boolean done;
	private boolean soundEffect;
	
	/**
	 * @param clip
	 * 		the clip this FishClip should represent.
	 * @param soundEffect
	 * 		if this clip is a sound effect (true) or music (false).
	 */
	public FishClip(Clip clip, boolean soundEffect) {
		this.clip = clip;
		this.soundEffect = soundEffect;
		clip.addLineListener(this);
		AudioEngine.getInstance().registerVolumeListener(soundEffect, () -> updateVolume());
		
		//Set the initial volume
		updateVolume();
	}
	
	/**
	 * @return
	 * 		the clip this FishClip represents.
	 */
	public Clip getClip() {
		return clip;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this clip is a sound effect.<br>
	 * 		<code>false</code> if it is (background) music.
	 */
	public boolean isSoundEffect() {
		return soundEffect;
	}
	
	/**
	 * @return
	 * 		if this clip is done playing.
	 */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * Update the volume of this FishClip.
	 */
	public void updateVolume() {
		FloatControl fc = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
		
		double percent;
		if (this.soundEffect) {
			percent = AudioEngine.getInstance().getEffectsVolume();
		} else {
			percent = AudioEngine.getInstance().getMusicVolume();
		}

		fc.setValue((float) (20.0 * (Math.log10(Math.exp(6.908 * percent)) - 3.0)));
	}

	@Override
	public synchronized void update(LineEvent event) {
		Type eventType = event.getType();
		if (eventType == Type.START) {
			done = false;
		} else if (eventType == Type.STOP || eventType == Type.CLOSE) {
			done = true;
			notifyAll();
		}
	}
	
	/**
	 * Waits until this FishClip is done playing.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	public synchronized void waitUntilDone() throws InterruptedException {
		while (!done) {
			wait();
		}
	}
	
	/**
	 * Marks this clip as done playing (even if that might not be the
	 * case).
	 */
	public synchronized void setDone() {
		done = true;
		notifyAll();
	}

	@Override
	public void close() throws IOException {
		clip.close();
	}
}
