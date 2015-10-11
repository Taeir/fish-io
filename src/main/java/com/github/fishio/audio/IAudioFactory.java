package com.github.fishio.audio;

import java.util.Map;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

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
	 * 		an observable list of all music this AudioFactory has loaded.
	 * 		Attach a changelistener to this list to get notified when
	 * 		music is loaded/unloaded.
	 */
	ObservableList<Sound> getAllMusic();
	
	/**
	 * @return
	 * 		a map of all sound effects this AudioFactory has loaded.
	 */
	Map<String, Sound> getAllSoundEffects();
	
	/**
	 * @param nr
	 * 		the number of the music to get.
	 * 
	 * @return
	 * 		the music with the given number, or <code>null</code> if no
	 * 		music with the given number exists.
	 */
	Sound getMusic(int nr);
	
	/**
	 * @param name
	 * 		the name of the sound effect to get.
	 * 
	 * @return
	 * 		the sound effect with the given name, or <code>null</code> if
	 * 		no effect with that name is known.
	 */
	Sound getSoundEffect(String name);
	
	/**
	 * Creates a new clip for the music with the given number.
	 * 
	 * @param nr
	 * 		the number of the music to create a clip for.
	 * 
	 * @return
	 * 		a clip for the music with the given number, or
	 * 		<code>null</code> if it doesn't exist or if an error occurred
	 * 		while creating the clip.
	 */
	default FishClip createMusicClip(int nr) {
		Sound music = getMusic(nr);
		
		if (music == null) {
			return null;
		}
		
		try {
			return music.getClip();
		} catch (Exception ex) {
			Log.getLogger().log(LogLevel.WARNING, "[Audio Factory] Unable to create clip for music #" + nr + "!");
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates a new clip for the sound effect with the given name.
	 * 
	 * @param name
	 * 		the name of the sound effect to create a clip for.
	 * 
	 * @return
	 * 		a clip for the sound effect with the given name, or
	 * 		<code>null</code> if it doesn't exist or if an error occurred
	 * 		while creating the clip.
	 */
	default FishClip createSoundEffectClip(String name) {
		Sound effect = getSoundEffect(name);
		
		if (effect == null) {
			return null;
		}
		
		try {
			return effect.getClip();
		} catch (Exception ex) {
			Log.getLogger().log(LogLevel.WARNING,
					"[Audio Factory] Unable to create clip for sound effect " + name + "!");
			ex.printStackTrace();
			return null;
		}
	}
}
