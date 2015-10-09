package com.github.fishio.audio;

/**
 * Listener for listening to volume level changes.
 */
@FunctionalInterface
public interface VolumeListener {
	/**
	 * Called when the volume level is changed.
	 */
	void changed();
}
