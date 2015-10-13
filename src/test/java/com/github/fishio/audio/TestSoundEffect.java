package com.github.fishio.audio;

import javafx.scene.media.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for {@link SoundEffect}.
 */
public class TestSoundEffect {

	private AudioClip clip;
	private SoundEffect soundEffect;
	
	/**
	 * Create a new soundEffect and clip before each test.
	 */
	@Before
	public void setUp() {
		clip = Mockito.mock(AudioClip.class);
		soundEffect = new SoundEffect(clip);
	}

	/**
	 * Test for {@link SoundEffect#play()}.
	 */
	@Test
	public void testPlay() {
		soundEffect.play();
		
		Mockito.verify(clip).play();
	}

	/**
	 * Test for {@link SoundEffect#stop()}.
	 */
	@Test
	public void testStop() {
		soundEffect.play();
		soundEffect.stop();
		
		Mockito.verify(clip).stop();
	}

}
