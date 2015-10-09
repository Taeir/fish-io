package com.github.fishio.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.github.fishio.Util;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Class to load audio files (asynchronously).
 */
public final class AudioLoader {
	private AudioLoader() { }
	
	/**
	 * Start loading music asynchronously (on a new thread).<br>
	 * Music will be added to the returned list as it is loaded.
	 * 
	 * @return
	 * 		an ObservableList of sounds in which the music will be placed
	 * 		(one by one) when it is loaded. The returned list will 
	 * 		initially be empty, but will be filled as music is loaded.
	 */
	public static ObservableList<Sound> loadMusicAsynchronous() {
		ObservableList<Sound> tbr = FXCollections.<Sound>observableArrayList();
		
		new Thread(() -> {
			int i = 0;
			
			List<Path> paths = getAudioFiles(false);
			for (Path path : paths) {
				Sound sound = loadSound(path, false);
				
				if (sound != null) {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Loaded music " + path);
					tbr.add(sound);
					
					i++;
				} else {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Unable to load music " + path);
				}
			}
			
			Log.getLogger().log(LogLevel.INFO, "[Audio Loader] Loaded " + i + " music files");
		}).start();
		
		return tbr;
	}
	
	/**
	 * Start loading sound effects asynchronously (on a new thread).<br>
	 * The returned ConcurrentHashMap will initially be empty, but as the
	 * sounds effects are loaded, they will be added to the map.
	 * 
	 * @return
	 * 		a ConcurrentHashMap of sounds in which the sound effects will
	 * 		be placed (one by one) when it is loaded. The returned map
	 * 		will initially be empty, but will be filled as music is
	 * 		loaded.
	 */
	public static ConcurrentHashMap<String, Sound> loadSoundEffectsAsynchronous() {
		ConcurrentHashMap<String, Sound> tbr = new ConcurrentHashMap<String, Sound>();
		
		new Thread(() -> {
			int i = 0;
			
			List<Path> paths = getAudioFiles(true);
			for (Path path : paths) {
				Sound sound = loadSound(path, true);
				String name = getSoundEffectName(path.toString());
				
				if (sound != null) {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Loaded soundeffect " + name + " from " + path);
					tbr.put(name, sound);
					
					i++;
				} else {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Unable to load soundeffect " + path);
				}
			}

			Log.getLogger().log(LogLevel.INFO, "[Audio Loader] Loaded " + i + " sound effects");
		}).start();
		
		return tbr;
	}
	
	private static Pattern pathPattern =
			Pattern.compile("(.*[/\\\\])?sound[/\\\\]effects[/\\\\](?<path>(.*[/\\\\])*)(?<name>.*)\\.(?<ext>\\w{3})");
	
	/**
	 * Extracts the sound effect name from the given path.
	 * 
	 * @param path
	 * 		the path to extract the name from.
	 * 
	 * @return
	 * 		the name of the sound effect
	 */
	private static String getSoundEffectName(String path) {
		Matcher m = pathPattern.matcher(path);
		if (!m.matches()) {
			Log.getLogger().log(LogLevel.WARNING, "[Util] Unable to get name from path: " + path);
			return path;
		}
		
		return m.group("path") + m.group("name");
	}

	/**
	 * Loads a sound from a file.
	 * 
	 * @param path
	 * 		the path of the sound to load
	 * @param soundEffect
	 * 		true for sound effects, false for (background) music.
	 * 
	 * @return
	 * 		a new Sound representing the sound for the given path, or
	 * 		<code>null</code> if an exception occurred while trying to
	 * 		load the sound.
	 */
	public static Sound loadSound(Path path, boolean soundEffect) {
		try (InputStream is = Files.newInputStream(path)) {
			//Use a buffered input stream if mark is not supported.
			if (!is.markSupported()) {
				return new Sound(new BufferedInputStream(is), isMp3(path.toString()), soundEffect);
			} else {
				return new Sound(is, isMp3(path.toString()), soundEffect);
			}
		} catch (IOException ex) {
			Log.getLogger().log(LogLevel.ERROR, "[Audio Loader] Unable to load sound file " + path + "!");
			ex.printStackTrace();
		} catch (UnsupportedAudioFileException ex) {
			Log.getLogger().log(LogLevel.ERROR,
					"[Audio Loader] Unable to load sound file " + path + ": audio format not supported!");
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns a list of paths to all the audio files for sound effects
	 * or music.
	 * 
	 * @param soundEffects
	 * 		<code>true</code> to get a list of sound effects,
	 * 		<code>false</code> to get a list of music.
	 * 
	 * @return
	 * 		a list of paths to all the audio files 
	 */
	@SuppressWarnings("resource")
	public static List<Path> getAudioFiles(boolean soundEffects) {
		List<Path> list = new ArrayList<Path>();
		String stringPath;
		if (soundEffects) {
			stringPath = "/sound/effects";
		} else {
			stringPath = "/sound/music";
		}
		
		try {
			URI uri = Util.class.getResource(stringPath).toURI();
			
			//Either get the file from inside or outside the jar.
			//This ensures that running the code from eclipse also works as intended.
			Path path;
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
				
				path = fileSystem.getPath("/sound");
			} else {
				path = Paths.get(uri);
			}
			
			//Search for audio files recursively
			try (Stream<Path> walker = Files.walk(path, 10)) {
				Iterator<Path> it = walker.iterator();
				
				while (it.hasNext()) {
					Path p = it.next();
					
					if (isAudioFile(p.toString())) {
						list.add(p);
					}
				}
			}
		} catch (Exception ex) {
			Log.getLogger().log(LogLevel.WARNING, "[Util] Unable to get Audio Files from jar!");
			ex.printStackTrace();
			return list;
		}

		return list;
	}
	
	/**
	 * Returns if the file at the given path is an audio file.
	 * 
	 * @param path
	 * 		the path of the file to check.
	 * 
	 * @return
	 * 		<code>true</code> if the file is a valid audio file,
	 * 		<code>false</code> otherwise.
	 */
	private static boolean isAudioFile(String path) {
		return path.endsWith(".mp3") || path.endsWith(".wav");
	}
	
	/**
	 * @param name
	 * 		the name of the file to check.
	 * 
	 * @return
	 * 		<code>true</code> if the given name is an mp3,
	 * 		<code>false</code> otherwise.
	 */
	private static boolean isMp3(String name) {
		return name.endsWith(".mp3");
	}
}
