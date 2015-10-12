/**
 * 
 */
package com.github.fishio.audio;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Abstract test class for {@link IAudioFactory}.
 */
public abstract class TestIAudioFactory {
	private IAudioFactory factory;
	private Sound mockedMusicSound;
	private FishClip mockedMusicClip;
	private Sound mockedSoundEffectSound;
	private FishClip mockedSoundEffectClip;
	
	/**
	 * Other test classes have to override this method and return a new
	 * instance of the IAudioFactory implementation they are testing.
	 * 
	 * @return
	 * 		a new IAudioFactory.
	 */
	public abstract IAudioFactory createAudioFactory();

	/**
	 * Ensures that the AudioFactory is done loading audio files.
	 * 
	 * @throws Exception
	 * 		if an Exception occurs while waiting for the Audio Factory
	 * 		to load it's files.
	 */
	@Before
	public void setUp() throws Exception {
		factory = createAudioFactory();
		
		//Create new mocks
		mockedMusicClip = Mockito.mock(FishClip.class);
		Mockito.when(mockedMusicClip.isSoundEffect()).thenReturn(false);
		
		mockedMusicSound = Mockito.mock(Sound.class);
		Mockito.when(mockedMusicSound.getClip()).thenReturn(mockedMusicClip);
		
		mockedSoundEffectClip = Mockito.mock(FishClip.class);
		Mockito.when(mockedSoundEffectClip.isSoundEffect()).thenReturn(true);
		
		mockedSoundEffectSound = Mockito.mock(Sound.class);
		Mockito.when(mockedSoundEffectSound.getClip()).thenReturn(mockedSoundEffectClip);
		
		//Add new mocks
		factory.getAllMusic().add(mockedMusicSound);
		factory.getAllSoundEffects().put("MOCKED_EFFECT", mockedSoundEffectSound);
	}

	/**
	 * Test for {@link IAudioFactory#getAllMusic()}.
	 */
	@Test
	public void testGetAllMusic() {
		assertTrue(factory.getAllMusic().contains(mockedMusicSound));
	}

	/**
	 * Test for {@link IAudioFactory#getAllSoundEffects()}.
	 */
	@Test
	public void testGetAllSoundEffects() {
		assertTrue(factory.getAllSoundEffects().containsValue(mockedSoundEffectSound));
	}

	/**
	 * Test for {@link IAudioFactory#getMusic(int)}.
	 */
	@Test
	public void testGetMusic() {
		int index = factory.getAllMusic().indexOf(mockedMusicSound);
		assertSame(mockedMusicSound, factory.getMusic(index));
	}

	/**
	 * Test for {@link IAudioFactory#getSoundEffect(String)}.
	 */
	@Test
	public void testGetSoundEffect() {
		assertSame(mockedSoundEffectSound, factory.getSoundEffect("MOCKED_EFFECT"));
	}

	/**
	 * Test for {@link IAudioFactory#createMusicClip(int)}.
	 */
	@SuppressWarnings("resource")
	@Test
	public void testCreateMusicClip() {
		int index = factory.getAllMusic().indexOf(mockedMusicSound);
		
		FishClip fc = factory.createMusicClip(index);
		assertSame(mockedMusicClip, fc);
	}

	/**
	 * Test for {@link IAudioFactory#createSoundEffectClip(String)}.
	 */
	@SuppressWarnings("resource")
	@Test
	public void testCreateSoundEffectClip() {
		FishClip fc = factory.createSoundEffectClip("MOCKED_EFFECT");
		assertSame(mockedSoundEffectClip, fc);
	}

}
