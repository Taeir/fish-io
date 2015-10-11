package com.github.fishio.audio;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Default implementation for the IAudioFactory.
 */
public class DefaultAudioFactory implements IAudioFactory {
	private ObservableList<Sound> music = FXCollections.observableArrayList();
	private ConcurrentHashMap<String, Sound> soundEffects = new ConcurrentHashMap<>();
	
	/**
	 * Creates a new DefaultAudioFactory.
	 */
	public DefaultAudioFactory() { }
	
	@Override
	public void startLoading() {
		startLoadingMusic();
		startLoadingSoundEffects();
	}
	
	/**
	 * Starts a new thread to load music.
	 */
	protected void startLoadingMusic() {
		new Thread(() -> {
			int i = 0;
			
			List<Path> paths = AudioUtil.getAudioFiles(false);
			for (Path path : paths) {
				Sound sound = AudioUtil.loadSound(path, false);
				
				if (sound != null) {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Loaded music " + path);
					music.add(sound);
					
					i++;
				} else {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Unable to load music " + path);
				}
			}
			
			Log.getLogger().log(LogLevel.INFO, "[Audio Loader] Loaded " + i + " music files");
		}).start();
	}
	
	/**
	 * Starts a new thread to load sound effects.
	 */
	protected void startLoadingSoundEffects() {
		new Thread(() -> {
			int i = 0;
			
			List<Path> paths = AudioUtil.getAudioFiles(true);
			for (Path path : paths) {
				Sound sound = AudioUtil.loadSound(path, true);
				String name = AudioUtil.getSoundEffectName(path.toString());
				
				if (sound != null) {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Loaded soundeffect " + name + " from " + path);
					soundEffects.put(name, sound);
					
					i++;
				} else {
					Log.getLogger().log(LogLevel.DEBUG, "[Audio Loader] Unable to load soundeffect " + path);
				}
			}

			Log.getLogger().log(LogLevel.INFO, "[Audio Loader] Loaded " + i + " sound effects");
		}).start();
	}

	@Override
	public ObservableList<Sound> getAllMusic() {
		return music;
	}

	@Override
	public ConcurrentHashMap<String, Sound> getAllSoundEffects() {
		return soundEffects;
	}

	@Override
	public Sound getMusic(int nr) {
		if (nr < 0 || nr >= music.size()) {
			return null;
		}
		
		return music.get(nr);
	}

	@Override
	public Sound getSoundEffect(String name) {
		return soundEffects.get(name);
	}
}
