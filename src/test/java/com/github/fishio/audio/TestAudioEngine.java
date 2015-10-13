package com.github.fishio.audio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.gui.SlimGuiTest;

/**
 * Test class for the {@link AudioEngine}.
 */
public class TestAudioEngine extends SlimGuiTest {
	private static AudioEngine engine = AudioEngine.getInstance();
	private IAudioFactory factory;
	private ObservableList<Music> list;
	private boolean started = false;
	private boolean stopped = false;
	
	/**
	 * Recreates the IAudioFactory and resets the properties in the
	 * AudioEngine.
	 */
	@Before
	public void setUp() {
		started = false;
		stopped = false;
		list = FXCollections.observableArrayList();
		
		factory = Mockito.mock(IAudioFactory.class);
		Mockito.when(factory.getAllMusic()).thenReturn(list);
		engine.setAudioFactory(factory);
		
		engine.getMuteStateProperty().set(AudioEngine.NO_MUTE);
		
		engine.getMasterVolumeProperty().set(0.80);
		engine.getEffectsVolumeProperty().set(0.75);
		engine.getMusicVolumeProperty().set(0.70);
	}
	
	/**
	 * Stop background music after each test.
	 */
	@After
	public void tearDown() {
		engine.stopBackgroundMusic();
	}

	/**
	 * Test for {@link AudioEngine#getAudioFactory()}.
	 */
	@Test
	public void testGetAudioFactory() {
		assertSame(factory, engine.getAudioFactory());
	}

	/**
	 * Test for {@link AudioEngine#getMasterVolume()}.
	 */
	@Test
	public void testGetMasterVolume() {
		assertEquals(0.80, engine.getMasterVolume(), 0.0);
	}

	/**
	 * Test for {@link AudioEngine#getMusicVolume()}.
	 */
	@Test
	public void testGetMusicVolume() {
		assertEquals(0.80 * 0.70, engine.getMusicVolume(), 0.0);
	}

	/**
	 * Test for {@link AudioEngine#getEffectsVolume()}.
	 */
	@Test
	public void testGetEffectsVolume() {
		assertEquals(0.80 * 0.75, engine.getEffectsVolume(), 0.0);
	}

	/**
	 * Test for {@link AudioEngine#getMusicVolumeBinding()}.
	 */
	@Test
	public void testGetMusicVolumeBinding() {
		assertEquals(engine.getMusicVolume(), engine.getMusicVolumeBinding().get(), 0.0);
	}

	/**
	 * Test for {@link AudioEngine#getEffectsVolumeBinding()}.
	 */
	@Test
	public void testGetEffectsVolumeBinding() {
		assertEquals(engine.getEffectsVolume(), engine.getEffectsVolumeBinding().get(), 0.0);
	}

	/**
	 * Test for {@link AudioEngine#getMuteState()}.
	 */
	@Test
	public void testGetMuteState() {
		assertEquals(AudioEngine.NO_MUTE, engine.getMuteState());
	}
	
	/**
	 * Test for {@link AudioEngine#getMuteState()}.
	 */
	@Test
	public void testGetMuteState2() {
		engine.toggleMuteState();
		assertEquals(AudioEngine.MUTE_MUSIC, engine.getMuteState());
	}
	
	/**
	 * Test for {@link AudioEngine#getMuteState()}.
	 */
	@Test
	public void testGetMuteState3() {
		engine.toggleMuteState();
		engine.toggleMuteState();
		
		assertEquals(AudioEngine.MUTE_ALL, engine.getMuteState());
	}

	/**
	 * Test for {@link AudioEngine#toggleMuteState()}.
	 */
	@Test
	public void testToggleMuteState() {
		assertEquals(AudioEngine.NO_MUTE, engine.getMuteState());
		engine.toggleMuteState();
		assertEquals(AudioEngine.MUTE_MUSIC, engine.getMuteState());
	}
	
	/**
	 * Test for {@link AudioEngine#startBackgroundMusicWhenLoaded()},
	 * when music is loaded after the call.
	 */
	@Test
	public void testStartBackgroundMusicWhenLoaded() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		engine.startBackgroundMusicWhenLoaded();
		
		//Music should not have started if there are no songs to play.
		assertFalse(engine.isBackgroundMusicPlaying());
		
		Music music = createMusic();
		
		//When music starts playing, we set started to true.
		music.getPlayer().setOnPlaying(() -> this.started = true);

		list.add(music);
		
		//We wait for a maximum of 5000 milliseconds
		waitForStart(100, 50L);
		
		assertTrue(started);
	}
	
	/**
	 * Test for {@link AudioEngine#startBackgroundMusicWhenLoaded()},
	 * when there is already a music track loaded.
	 */
	@Test
	public void testStartBackgroundMusicWhenLoaded2() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		//Load one song
		Music music = createMusic();
		
		//When music starts playing, we set started to true.
		music.getPlayer().setOnPlaying(() -> this.started = true);
		
		list.add(music);
		
		//Start when there is already a song loaded.
		engine.startBackgroundMusicWhenLoaded();
		
		//We wait for a maximum of 5000 milliseconds
		waitForStart(100, 50L);
		
		assertTrue(started);
	}

	/**
	 * Test for {@link AudioEngine#startBackgroundMusic()}, when no music
	 * is loaded.
	 */
	@Test
	public void testStartBackgroundMusic_noMusic() {
		engine.startBackgroundMusic();
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException ex) {
			fail();
		}
		
		assertFalse(engine.isBackgroundMusicPlaying());
	}
	
	/**
	 * Test for {@link AudioEngine#startBackgroundMusic()}, when music is
	 * loaded.
	 */
	@Test
	public void testStartBackgroundMusic() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		//Load one song
		Music music = createMusic();
		music.getPlayer().setOnPlaying(() -> this.started = true);
		
		list.add(music);
		
		//Start background music
		engine.startBackgroundMusic();
		
		//Wait for a maximum of 5000 milliseconds
		waitForStart(100, 50L);
		
		assertTrue(started);
	}

	/**
	 * Test for {@link AudioEngine#stopBackgroundMusic()}.
	 */
	@Test
	public void testStopBackgroundMusic() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		Music music = createMusic();
		
		//When music starts playing, we set started to true.
		music.getPlayer().setOnPlaying(() -> this.started = true);
		//When music stops playing, we set stopped to true.
		music.setOnStop(() -> this.stopped = true);

		list.add(music);
		
		//Start background music
		engine.startBackgroundMusic();
		
		//Wait for a maximum of 5000 milliseconds
		waitForStart(100, 50L);
		assertTrue(started);
		
		engine.stopBackgroundMusic();
		
		waitForStop(100, 50L);
		assertTrue(stopped);
	}

	/**
	 * Test for {@link AudioEngine#playEffect(String)}.
	 */
	@Test
	public void testPlayEffect() {
		assertFalse(engine.playEffect("HELLO"));
		
		SoundEffect se = Mockito.mock(SoundEffect.class);
		Mockito.when(factory.getSoundEffect("TEST_PLAY_EFFECT")).thenReturn(se);
		
		assertTrue(engine.playEffect("TEST_PLAY_EFFECT"));
		
		Mockito.verify(se).play();
	}

	/**
	 * Test for {@link AudioEngine#getMusic(int)}.
	 */
	@Test
	public void testGetMusic() {
		assertNull(engine.getMusic(5));
		
		Mockito.verify(factory).getMusic(5);
	}

	/**
	 * Test for {@link AudioEngine#getEffect(String)}.
	 */
	@Test
	public void testGetEffect() {
		assertNull(engine.getEffect("HELLO"));
		
		Mockito.verify(factory).getSoundEffect("HELLO");
	}

	/**
	 * @return
	 * 		creates a music from a test music file.
	 */
	private Music createMusic() {
		Path path = AudioUtil.getAudioFiles(false).get(0);
		Music music = new Music(path.toUri().toString());
		return music;
	}
	
	/**
	 * Waits for started to be true.
	 * 
	 * @param slices
	 * 		the maximum amount of times to wait.
	 * @param interval
	 * 		the amount of time in milliseconds to wait per slice.
	 */
	private void waitForStart(int slices, long interval) {
		for (int i = 0; !started && i < slices; i++) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException ex) { }
		}
	}
	
	/**
	 * Waits for stopped to be true.
	 * 
	 * @param slices
	 * 		the maximum amount of times to wait.
	 * @param interval
	 * 		the amount of time in milliseconds to wait per slice.
	 */
	private void waitForStop(int slices, long interval) {
		for (int i = 0; !stopped && i < slices; i++) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException ex) { }
		}
	}
}
