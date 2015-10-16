package com.github.fishio.audio;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

import com.github.fishio.settings.Settings;

/**
 * Singleton class for managing playing audio.
 */
public final class AudioEngine {
	public static final int NO_MUTE = 0;
	public static final int MUTE_MUSIC = 1;
	public static final int MUTE_ALL = 2;

	private static AudioEngine instance = new AudioEngine();
	
	private IAudioFactory audioFactory;
	
	private BackgroundMusic backgroundMusic = new BackgroundMusic();
	
	private SimpleIntegerProperty muteStateProperty = new SimpleIntegerProperty(NO_MUTE);
	
	private SimpleDoubleProperty masterVolumeProperty;
	private SimpleDoubleProperty musicVolumeProperty;
	private SimpleDoubleProperty effectsVolumeProperty;	
	
	private AudioEngine() {
		//Get volume properties from settings.
		masterVolumeProperty = Settings.getInstance().getSliderProperty("MASTER_VOLUME");
		musicVolumeProperty = Settings.getInstance().getSliderProperty("MUSIC_VOLUME");
		effectsVolumeProperty = Settings.getInstance().getSliderProperty("EFFECTS_VOLUME");
		
		//Use the DefaultAudioFactory initially.
		this.audioFactory = new DefaultAudioFactory();
		
		bindMuteState();
	}
	
	/**
	 * Adds a listener to the mute state that updates the mute levels.
	 */
	private void bindMuteState() {
		muteStateProperty.addListener((o, oVal, nVal) -> {
			switch (nVal.intValue()) {
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
		});
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
	 * 		the Audio factory used by the audio engine.
	 */
	public IAudioFactory getAudioFactory() {
		return audioFactory;
	}
	
	/**
	 * Sets the audio factory used by the audio engine.
	 * 
	 * @param audioFactory
	 * 		the audio factory to use.
	 */
	public void setAudioFactory(IAudioFactory audioFactory) {
		this.audioFactory = audioFactory;
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
	 * @return
	 * 		a DoubleBinding representing the actual music volume as a
	 * 		double from 0 to 1.
	 */
	public DoubleBinding getMusicVolumeBinding() {
		return masterVolumeProperty.multiply(musicVolumeProperty);
	}
	
	/**
	 * @return
	 * 		a DoubleBinding representing the actual sound effects volume
	 * 		as a double from 0 to 1.
	 */
	public DoubleBinding getEffectsVolumeBinding() {
		return masterVolumeProperty.multiply(effectsVolumeProperty);
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
	 * Start the background music as soon as a music track is loaded.
	 */
	public void startBackgroundMusicWhenLoaded() {
		if (audioFactory.getAllMusic().isEmpty()) {
			ListChangeListener<Music> cl = new ListChangeListener<Music>() {
				@Override
				public void onChanged(Change<? extends Music> c) {
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
			
			audioFactory.getAllMusic().addListener(cl);
		} else {
			startBackgroundMusic();
		}
	}
	
	/**
	 * Start playing background music.<br>
	 * <br>
	 * If background music is already playing, this will start the next
	 * song.
	 */
	public void startBackgroundMusic() {
		backgroundMusic.play();
	}
	
	/**
	 * @return
	 * 		if the background music is currently playing.
	 */
	public boolean isBackgroundMusicPlaying() {
		return backgroundMusic.isPlaying();
	}
	
	/**
	 * Stops any background music that is running.
	 */
	public void stopBackgroundMusic() {
		backgroundMusic.stop();
	}
	
	/**
	 * Plays the button sound effect.
	 * 
	 * @return
	 * 		<code>true</code> if the effect will be played,
	 * 		<code>false</code> if not (not found or error).
	 */
	public boolean playButtonSound() {
		return playEffect("button");
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
		SoundEffect se = audioFactory.getSoundEffect(effectName);
		if (se == null) {
			return false;
		}
		
		se.play();
		return true;
	}
	
	/**
	 * Method that should be called when shutting down the application.
	 */
	public void shutdown() {
		stopBackgroundMusic();
		
		//Stop all sound effects
		for (SoundEffect se : audioFactory.getAllSoundEffects().values()) {
			se.stop();
		}
	}
	
	/**
	 * @param nr
	 * 		the number of the music to get.
	 * 
	 * @return
	 * 		the sound corresponding to the music with the given number,
	 * 		or <code>null</code> if there is no music with this number.
	 */
	public Music getMusic(int nr) {
		return audioFactory.getMusic(nr);
	}
	
	/**
	 * @param name
	 * 		the name of the sound effect to get.
	 * 
	 * @return
	 * 		the sound effect with the given name, or <code>null</code>
	 * 		if it does not exist.
	 */
	public SoundEffect getEffect(String name) {
		return audioFactory.getSoundEffect(name);
	}
}
