package com.github.fishio.audio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;

/**
 * Interface for representing playable audio.
 */
public interface IPlayable {
	/**
	 * Starts playing this IPlayable.
	 */
	void play();
	
	/**
	 * @return
	 * 		if this playable is currently playing.
	 */
	boolean isPlaying();
	
	/**
	 * Stop all instances of this playable currently playing.
	 */
	void stop();
	
	/**
	 * @return
	 * 		the property linked to the volume of this IPlayable.
	 */
	ObservableValue<Number> getVolumeProperty();
}
