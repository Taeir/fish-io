package com.github.fishio.audio;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

/**
 * Class for playing background music.
 */
public class BackgroundMusic implements IPlayable {
	private Random random = new Random();

	private SimpleBooleanProperty stopProperty = new SimpleBooleanProperty();

	private ArrayList<Music> history = new ArrayList<>();
	private Music current;
	
	/**
	 * Create a new BackgroundMusic object.
	 */
	public BackgroundMusic() { }
	
	@Override
	public void play() {
		playNextSong();
	}
	
	@Override
	public boolean isPlaying() {
		return current != null && current.isPlaying();
	}
	
	@Override
	public void stop() {
		stopProperty.set(true);
		if (current != null) {
			current.stop();
		}
	}
	
	@Override
	public DoubleProperty getVolumeProperty() {
		return AudioEngine.getInstance().getMusicVolumeProperty();
	}
	
	/**
	 * Starts playback of the next song.<br>
	 * If background music was not playing, it is started.
	 */
	public synchronized void playNextSong() {
		playNextSong(false);
	}
	
	/**
	 * Switches to the next song.
	 * 
	 * @param automatic
	 * 		set to true if this method call is done automatically (when
	 * 		another music track finishes, it starts the next one
	 * 		automatically).
	 */
	private synchronized void playNextSong(boolean automatic) {
		if (current != null) {
			//If the current song is still playing, we stop it.
			if (current.isPlaying()) {
				current.setOnStop(null);
				current.stop();
			}
			
			//Add the old current song to the history.
			history.add(current);
		}
		
		//If we are not called automatically, we set stop to false.
		if (!automatic) {
			stopProperty.set(false);
		}
		//Music is stopped, so we dont start new music.
		else if (stopProperty.get()) {
			return;
		}
		
		//Select a random music track and play it.
		Music musicTrack = getRandomMusic();
		if (musicTrack != null) {
			current = musicTrack;
			musicTrack.play();
		}
	}
	
	/**
	 * Gets a random music track that is not in the history.
	 * 
	 * @return
	 * 		a random music track or <code>null</code> if there are no
	 * 		music tracks loaded.
	 */
	private synchronized Music getRandomMusic() {
		ObservableList<Music> allMusic = AudioEngine.getInstance().getAudioFactory().getAllMusic();
		//There is no next song
		if (allMusic.isEmpty()) {
			return null;
		}
		
		//Select all music tracks that are not in our history
		ArrayList<Music> al = new ArrayList<>(allMusic);
		al.removeAll(history);
		
		//If we have played everything, we clear our history
		if (al.isEmpty()) {
			history.clear();
			al.addAll(allMusic);
		}
		
		//Select a random music track
		Music musicTrack = al.get(random.nextInt(al.size()));
		Runnable oldr = musicTrack.getPlayer().getOnStopped();
		musicTrack.setOnStop(() -> {
			if (oldr != null) {
				oldr.run();
			}
			playNextSong(true);
			musicTrack.setOnStop(oldr);
		});
		
		return musicTrack;
	}
}
