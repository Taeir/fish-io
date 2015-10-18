package com.github.fishio.audio;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test class for {@link SoundEffect}.
 */
@PrepareForTest(AudioClip.class)
@RunWith(PowerMockRunner.class)
public class TestSoundEffect {

	private AudioClip clip;
	private SoundEffect soundEffect;
	
	/**
	 * Create a new soundEffect and clip before each test.
	 */
	@Before
	public void setUp() {
		DoubleProperty property = new SimpleDoubleProperty();
		clip = PowerMockito.mock(AudioClip.class);
		when(clip.volumeProperty()).thenReturn(property);
		
		soundEffect = new SoundEffect(clip);
	}

	/**
	 * Test for {@link SoundEffect#play()}.
	 */
	@Test
	public void testPlay() {
		soundEffect.play();
		
		verify(clip).play();
	}

	/**
	 * Test for {@link SoundEffect#stop()}.
	 */
	@Test
	public void testStop() {
		soundEffect.play();
		soundEffect.stop();
		
		verify(clip).stop();
	}

}
