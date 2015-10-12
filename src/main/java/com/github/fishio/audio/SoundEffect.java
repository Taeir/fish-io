package com.github.fishio.audio;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.AudioClip;

/**
 * Class for representing SoundEffects.
 */
public class SoundEffect implements IPlayable {
	private AudioClip clip;
	
	/**
	 * Creates a new SoundEffect using the given source.
	 * 
	 * @param source
	 * 		the source URL of the audio.
	 */
	public SoundEffect(String source) {
		this.clip = new AudioClip(source);
		
		//Set lowered priority, so that music is not interrupted.
		this.clip.setPriority(-1);
		
		//Bind the volume to the sound effects volume
		this.clip.volumeProperty().bind(AudioEngine.getInstance().getEffectsVolumeBinding());
	}
	
	@Override
	public void play() {
		clip.play();
	}

	@Override
	public boolean isPlaying() {
		return clip.isPlaying();
	}

	@Override
	public void stop() {
		clip.stop();
	}
	
	@Override
	public DoubleProperty getVolumeProperty() {
		return clip.volumeProperty();
	}
}
