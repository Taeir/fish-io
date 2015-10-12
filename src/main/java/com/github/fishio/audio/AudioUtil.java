package com.github.fishio.audio;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.fishio.Util;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Utility class for locating and loading audio files.
 */
public final class AudioUtil {
	private AudioUtil() { }
	
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
	public static String getSoundEffectName(String path) {
		Matcher m = pathPattern.matcher(path);
		if (!m.matches()) {
			Log.getLogger().log(LogLevel.WARNING, "[Util] Unable to get name from path: " + path);
			return path;
		}
		
		return m.group("path") + m.group("name");
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
	public static boolean isAudioFile(String path) {
		return path.endsWith(".mp3") || path.endsWith(".wav");
	}
}
