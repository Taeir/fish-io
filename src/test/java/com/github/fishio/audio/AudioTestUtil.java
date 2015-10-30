package com.github.fishio.audio;

import java.nio.file.Path;

/**
 * Utility containing some useful methods for Testing Audio.
 */
public final class AudioTestUtil {
	private AudioTestUtil() { }
	
	/**
	 * @return
	 * 		if this execution environment supports playing music. This
	 * 		method will return <code>false</code> when running on travis.
	 */
	public static boolean supportsMusic() {
		try {
			Path path = AudioUtil.getAudioFiles(false).get(0);
			Music music = new Music(path.toUri().toString());
			
			if (music.getPlayer().getError() != null) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * @return
	 * 		if this execution environment supports playing music. This
	 * 		method will return <code>false</code> when running on travis.
	 */
	public static boolean supportsSoundEffects() {
		try {
			Path path = AudioUtil.getAudioFiles(true).get(0);
			new SoundEffect(path.toUri().toString()).isPlaying();
		} catch (Exception ex) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if music is supported. This method will print a warning to
	 * System.err if it is not.
	 * 
	 * @return
	 * 		if music is supported.
	 * 
	 * @see #supportsMusic()
	 */
	public static boolean checkMusic() {
		if (supportsMusic()) {
			return true;
		}
		System.err.println("Cannot test music: Music is not supported on this system!");
		return false;
	}
	
	/**
	 * Checks if sound effects are supported. This method will print a
	 * warning to System.err if it is not.
	 * 
	 * @return
	 * 		if sound effects are supported.
	 * 
	 * @see #supportsSoundEffects()
	 */
	public static boolean checkSoundEffects() {
		if (supportsSoundEffects()) {
			return true;
		}
		System.err.println("Cannot test sound effects: Sound Effects are not supported on this system!");
		return false;
	}
}
