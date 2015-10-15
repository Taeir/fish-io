package com.github.fishio.audio;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * Represents one music track (song).
 */
public class Music implements IPlayable {
	private Media media;
	private MediaPlayer player;
	
	/**
	 * Creates a new Music with the given source.
	 * 
	 * @param source
	 * 		the source URL of the music.
	 */
	public Music(String source) {
		media = new Media(source);
		player = new MediaPlayer(media);

		//Bind the volume to the music volume
		player.volumeProperty().bind(AudioEngine.getInstance().getMusicVolumeBinding());
	}

	@Override
	public void play() {
		this.player.seek(Duration.ZERO);
		this.player.play();
	}

	@Override
	public boolean isPlaying() {
		return this.player.getStatus() == Status.PLAYING;
	}

	@Override
	public void stop() {
		this.player.stop();
	}

	@Override
	public DoubleProperty getVolumeProperty() {
		return this.player.volumeProperty();
	}
	
	/**
	 * Set the runnable that will be executed when this Music is stopped,
	 * or when it is done playing.
	 * 
	 * @param runnable
	 * 		the runnable to execute.
	 */
	public void setOnStop(Runnable runnable) {
		this.player.setOnStopped(runnable);
		this.player.setOnEndOfMedia(runnable);
	}
	
	/**
	 * @return
	 * 		the MediaPlayer for this Music.
	 */
	public MediaPlayer getPlayer() {
		return this.player;
	}
}
