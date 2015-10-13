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
	private ObservableList<Music> music = FXCollections.observableArrayList();
	private ConcurrentHashMap<String, SoundEffect> soundEffects = new ConcurrentHashMap<>();
	private volatile boolean loading;
	private boolean musicLoaded;
	private boolean soundEffectsLoaded;
	
	/**
	 * Creates a new DefaultAudioFactory.
	 */
	public DefaultAudioFactory() { }
	
	@Override
	public void startLoading() {
		if (loading || isDoneLoading()) {
			return;
		}
		
		loading = true;
		startLoadingMusic();
		startLoadingSoundEffects();
	}
	
	/**
	 * Starts a new thread to load music.
	 */
	protected void startLoadingMusic() {
		new Thread(() -> {
			try {
				int i = 0;
				
				List<Path> paths = AudioUtil.getAudioFiles(false);
				for (Path path : paths) {
					try {
						Music musicTrack = new Music(path.toUri().toString());
						
						Log.getLogger().log(LogLevel.DEBUG, "[Audio Factory] Loaded music " + path);
						music.add(musicTrack);
						i++;
					} catch (Exception ex) {
						Log.getLogger().log(LogLevel.DEBUG, "[Audio Factory] Unable to load music " + path);
					}
				}
				
				Log.getLogger().log(LogLevel.INFO, "[Audio Factory] Loaded " + i + " music files");
			} finally {
				musicLoaded = true;
			}
		}).start();
	}
	
	/**
	 * Starts a new thread to load sound effects.
	 */
	protected void startLoadingSoundEffects() {
		new Thread(() -> {
			try {
				int i = 0;
				
				List<Path> paths = AudioUtil.getAudioFiles(true);
				for (Path path : paths) {
					String name = AudioUtil.getSoundEffectName(path.toString());
					
					try {
						SoundEffect sound = new SoundEffect(path.toUri().toString());
						
						Log.getLogger().log(LogLevel.DEBUG,
								"[Audio Factory] Loaded soundeffect " + name + " from " + path);
						
						soundEffects.put(name, sound);
					} catch (Exception ex) {
						Log.getLogger().log(LogLevel.DEBUG, "[Audio Factory] Unable to load soundeffect " + path);
					}
				}
	
				Log.getLogger().log(LogLevel.INFO, "[Audio Factory] Loaded " + i + " sound effects");
			} finally {
				this.soundEffectsLoaded = true;
			}
		}).start();
	}
	
	@Override
	public boolean isDoneLoading() {
		return musicLoaded && soundEffectsLoaded;
	}

	@Override
	public ObservableList<Music> getAllMusic() {
		return music;
	}

	@Override
	public ConcurrentHashMap<String, SoundEffect> getAllSoundEffects() {
		return soundEffects;
	}

	@Override
	public Music getMusic(int nr) {
		if (nr < 0 || nr >= music.size()) {
			return null;
		}
		
		return music.get(nr);
	}

	@Override
	public SoundEffect getSoundEffect(String name) {
		return soundEffects.get(name);
	}
}
