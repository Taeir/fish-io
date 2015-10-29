package com.github.fishio.audio;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * AudioFactory that does not provide any audio.
 * Used for testing only.
 */
public class NoAudioFactory implements IAudioFactory {
	private static ObservableList<Music> music = FXCollections.observableArrayList();
	private static Map<String, SoundEffect> soundEffects = new ConcurrentHashMap<>();
	
	@Override
	public void startLoading() { }

	@Override
	public boolean isDoneLoading() {
		return true;
	}

	@Override
	public ObservableList<Music> getAllMusic() {
		return music;
	}

	@Override
	public Map<String, SoundEffect> getAllSoundEffects() {
		return soundEffects;
	}

	@Override
	public Music getMusic(int nr) {
		return null;
	}

	@Override
	public SoundEffect getSoundEffect(String name) {
		return null;
	}

}
