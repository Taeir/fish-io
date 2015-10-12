package com.github.fishio.audio;

import java.util.Map;

import javafx.collections.ObservableList;

/**
 * Abstract Factory interface for providing audio.
 */
public interface IAudioFactory {
	
	/**
	 * This method makes the audio factory start (pre)loading its sounds.<br>
	 * <br>
	 * If this IAudioFactory does any form of caching, it should be done
	 * after this method is called. This caching must then happen on a
	 * separate thread, if possible.
	 */
	void startLoading();
	
	/**
	 * @return
	 * 		<code>true</code> if this IAudioFactory is done loading.
	 * 		<code>false</code> otherwise.
	 */
	boolean isDoneLoading();
	
	/**
	 * @return
	 * 		an observable list of all music this AudioFactory has loaded.
	 * 		Attach a changelistener to this list to get notified when
	 * 		music is loaded/unloaded.
	 */
	ObservableList<Music> getAllMusic();
	
	/**
	 * @return
	 * 		a map of all sound effects this AudioFactory has loaded.
	 */
	Map<String, SoundEffect> getAllSoundEffects();
	
	/**
	 * @param nr
	 * 		the number of the music to get.
	 * 
	 * @return
	 * 		the music with the given number, or <code>null</code> if no
	 * 		music with the given number exists.
	 */
	Music getMusic(int nr);
	
	/**
	 * @param name
	 * 		the name of the sound effect to get.
	 * 
	 * @return
	 * 		the sound effect with the given name, or <code>null</code> if
	 * 		no effect with that name is known.
	 */
	SoundEffect getSoundEffect(String name);
}
