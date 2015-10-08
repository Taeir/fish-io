package com.github.fishio.audio;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Singleton class for managing playing audio.
 */
public final class AudioEngine {
	/**
	 * The maximum amount of threads that can play sounds concurrently.
	 */
	private static final int MAX_THREAD_COUNT = 11;
	
	public static final int NO_MUTE = 0;
	public static final int MUTE_MUSIC = 1;
	public static final int MUTE_ALL = 2;

	private static AudioEngine instance = new AudioEngine();
	
	private ConcurrentHashMap<String, Sound> effects;
	private ObservableList<Sound> music;
	
	private ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
	
	private ConcurrentHashMap<VolumeListener, ChangeListener<Number>> listeners = new ConcurrentHashMap<>();
	
	private ClipRunnable background;
	private int backgroundNr = -1;
	private SimpleBooleanProperty musicRunningProperty = new SimpleBooleanProperty();
	
	private SimpleDoubleProperty masterVolumeProperty = new SimpleDoubleProperty(1.0);
	private SimpleDoubleProperty musicVolumeProperty = new SimpleDoubleProperty(0.8);
	private SimpleDoubleProperty effectsVolumeProperty = new SimpleDoubleProperty(0.8);
	
	private SimpleIntegerProperty muteStateProperty = new SimpleIntegerProperty(NO_MUTE);
	
	private AudioEngine() {
		music = AudioLoader.loadMusicAsynchronous();
		effects = AudioLoader.loadSoundEffectsAsynchronous();
	}
	
	/**
	 * @return
	 * 		the AudioEngine instance.
	 */
	public static AudioEngine getInstance() {
		return instance;
	}
	
	/**
	 * @return
	 * 		the master volume.
	 */
	public double getMasterVolume() {
		return Math.max(0, masterVolumeProperty.doubleValue());
	}
	
	/**
	 * @return
	 * 		the music volume (already multiplied with the master volume).
	 */
	public double getMusicVolume() {
		return Math.max(0, getMasterVolume() * musicVolumeProperty.doubleValue());
	}
	
	/**
	 * @return
	 * 		the effects volume (already multiplied with the master volume).
	 */
	public double getEffectsVolume() {
		return Math.max(0, getMasterVolume() * effectsVolumeProperty.doubleValue());
	}
	
	/**
	 * @return the masterVolume Property
	 */
	public SimpleDoubleProperty getMasterVolumeProperty() {
		return masterVolumeProperty;
	}
	
	/**
	 * @return the musicVolume Property
	 */
	public SimpleDoubleProperty getMusicVolumeProperty() {
		return musicVolumeProperty;
	}
	
	/**
	 * @return the effectsVolume Property
	 */
	public SimpleDoubleProperty getEffectsVolumeProperty() {
		return effectsVolumeProperty;
	}
	
	/**
	 * Can be {@link #NO_MUTE}, {@link #MUTE_MUSIC} or {@link #MUTE_ALL}.
	 * 
	 * @return
	 * 		the current mute state.
	 */
	public int getMuteState() {
		return muteStateProperty.get();
	}
	
	/**
	 * @return
	 * 		the mute state property.
	 */
	public SimpleIntegerProperty getMuteStateProperty() {
		return muteStateProperty;
	}
	
	/**
	 * Toggle between the different mute states:
	 * {@link #NO_MUTE}, {@link #MUTE_MUSIC} and {@link #MUTE_ALL}.
	 */
	public void toggleMuteState() {
		muteStateProperty.set((muteStateProperty.get() + 1) % 3);

		switch (muteStateProperty.get()) {
			case NO_MUTE:
				setMute(masterVolumeProperty, false);
				setMute(musicVolumeProperty, false);
				break;
			case MUTE_MUSIC:
				setMute(masterVolumeProperty, false);
				setMute(musicVolumeProperty, true);
				break;
			case MUTE_ALL:
				setMute(masterVolumeProperty, true);
				setMute(musicVolumeProperty, false);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Mute or unmute a volume property.
	 * 
	 * @param property
	 * 		the volume property to change the mute of.
	 * @param mute
	 * 		<code>true</code> to mute the property,
	 * 		<code>false</code> to unmute the property.
	 */
	private static void setMute(SimpleDoubleProperty property, boolean mute) {
		double old = property.get();
		if (!mute && old < 0) {
			property.set(-1 * old);
		} else if (mute && old > 0) {
			property.set(-1 * old);
		}
	}
	
	/**
	 * Register a volume listener for either effect or music volume.
	 * 
	 * @param effect
	 * 		<code>true</code> to listen for effect volume updates,
	 * 		<code>false</code> to listen for music volume updates.
	 * @param listener
	 * 		the listener that is called when the volume changes.
	 */
	public void registerVolumeListener(boolean effect, VolumeListener listener) {
		ChangeListener<Number> cl = (obs, oldV, newV) -> listener.changed();
		
		listeners.put(listener, cl);
		
		if (effect) {
			effectsVolumeProperty.addListener((obs, oldV, newV) -> listener.changed());
		} else {
			musicVolumeProperty.addListener((obs, oldV, newV) -> listener.changed());
		}
		
		//Also add the listener to the master
		masterVolumeProperty.addListener((obs, oldV, newV) -> listener.changed());
	}
	
	/**
	 * Unregister a volume listener.
	 * 
	 * @param listener
	 * 		the volume listener to unregister.
	 */
	public void unregisterVolumeListener(VolumeListener listener) {
		ChangeListener<Number> cl = listeners.remove(listener);
		
		if (cl != null) {
			effectsVolumeProperty.removeListener(cl);
			musicVolumeProperty.removeListener(cl);
			masterVolumeProperty.removeListener(cl);
		}
	}
	
	/**
	 * @return
	 * 		the observable list containing all the music sounds that have
	 * 		been cached.<br>
	 * 		Attach a changelistener to this list to get notified when
	 * 		music is loaded/unloaded.
	 */
	public ObservableList<Sound> getMusic() {
		return music;
	}
	
	/**
	 * Start the background music as soon as a music track is loaded.
	 */
	public void startBackgroundMusicWhenLoaded() {
		ListChangeListener<Sound> cl = new ListChangeListener<Sound>() {
			@Override
			public void onChanged(Change<? extends Sound> c) {
				while (c.next()) {
					if (!c.wasAdded()) {
						continue;
					}
					
					//Remove this listener, we are no longer needed
					c.getList().removeListener(this);
					
					//Start playing background music
					startBackgroundMusic();
				}
			}
		};
		
		getMusic().addListener(cl);
	}
	
	/**
	 * Start playing background music.<br>
	 * <br>
	 * If background music is already playing, this will start the next
	 * song.
	 */
	public void startBackgroundMusic() {
		//Already running, so we go to the next song
		if (musicRunningProperty.get()) {
			background.stop();
			return;
		}
		
		//Get the number of the next music
		int next = getNextMusic(backgroundNr);
		
		//There is no next music
		if (next == -1) {
			return;
		}
		
		try {
			background = new ClipRunnable(getMusic(next).getClip());
			backgroundNr = next;
		} catch (Exception ex) {
			Log.getLogger().log(LogLevel.WARNING, "[Audio Engine] Unable to start background music!");
			ex.printStackTrace();
			return;
		}
		
		background.getRunningProperty().addListener((o, oldV, newV) -> {
			//Background music should no longer be running.
			if (!musicRunningProperty.get()) {
				return;
			}
			
			//We are done with the last song.
			if (!newV) {
				int nextMusicNr = getNextMusic(backgroundNr);
				
				if (nextMusicNr == -1) {
					return;
				}
				
				//Set the new music
				try {
					background.setClip(getMusic(nextMusicNr).getClip());
					backgroundNr = nextMusicNr;
				} catch (Exception ex) {
					Log.getLogger().log(LogLevel.WARNING, "[Audio Engine] Unable to start next background music!");
					ex.printStackTrace();
					return;
				}
				
				//Start playback again.
				executor.submit(background);
			}
		});
		
		musicRunningProperty.set(true);
		
		//Start the new background music
		executor.submit(background);
	}
	
	/**
	 * Stops any background music that is running.
	 */
	public void stopBackgroundMusic() {
		//We are not running background music, so we can simply return.
		if (!musicRunningProperty.get()) {
			return;
		}
		
		//Mark that we are not running music
		musicRunningProperty.set(false);
		
		//Stop background music
		background.stop();
	}
	
	/**
	 * Plays the sound effect with the given name.
	 * 
	 * @param effectName
	 * 		the name of the sound effect to play.
	 * 
	 * @return
	 * 		<code>true</code> if the effect will be played,
	 * 		<code>false</code> if not (not found or error).
	 */
	public boolean playEffect(String effectName) {
		Sound sound = getEffect(effectName);
		if (sound == null) {
			return false;
		}
		
		try {
			ClipRunnable cr = new ClipRunnable(sound.getClip());
			executor.submit(cr);
		} catch (Exception ex) {
			Log.getLogger().log(LogLevel.WARNING,
					"[Audio Engine] Error while trying to get clip for sound effect " + effectName);
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method that should be called when shutting down the application.
	 */
	public void shutdown() {
		stopBackgroundMusic();
		executor.shutdown();
		try {
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			//We don't need to do anything if shutdown fails,
		}
		executor.shutdownNow();
	}
	
	/**
	 * @param nr
	 * 		the number of the music to get.
	 * 
	 * @return
	 * 		the sound corresponding to the music with the given number,
	 * 		or <code>null</code> if there is no music with this number.
	 */
	public Sound getMusic(int nr) {
		return music.get(nr);
	}
	
	/**
	 * @param name
	 * 		the name of the sound effect to get.
	 * 
	 * @return
	 * 		the sound effect with the given name, or <code>null</code>
	 * 		if it does not exist.
	 */
	public Sound getEffect(String name) {
		return effects.get(name);
	}
	
	/**
	 * Returns the number of the music track that should come after the
	 * given one.
	 * 
	 * @param nr
	 * 		the number of the current music track
	 * 
	 * @return
	 * 		the number of the next music track, or -1 if there is no
	 * 		music.
	 */
	public int getNextMusic(int nr) {
		if (music.isEmpty()) {
			return -1;
		}
		
		int next = (nr + 1) % music.size();

		//Ensure we don't keep looping
		for (int i = 0; i < music.size(); i++) {
			Sound sound = getMusic(next);
			if (sound != null) {
				return next;
			}
			
			next = (next + 1) % music.size();
		}
		
		return -1;
	}
}
