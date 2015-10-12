package com.github.fishio.audio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;

import org.junit.Test;

/**
 * Test class for {@link SoundEffect}.
 */
public class TestSoundEffect {

	/**
	 * @return
	 * 		a new SoundEffect for a test effect file.
	 */
	private SoundEffect getTestEffect() {
		Path path = AudioUtil.getAudioFiles(true).get(0);
		SoundEffect se = new SoundEffect(path.toUri().toString());
		return se;
	}

	/**
	 * Test for {@link SoundEffect#play()}.
	 */
	@Test
	public void testPlay() {
		if (!AudioTestUtil.checkSoundEffects()) {
			return;
		}
		SoundEffect se = getTestEffect();
		
		se.play();
		
		assertTrue(se.isPlaying());
	}

	/**
	 * Test for {@link SoundEffect#stop()}.
	 */
	@Test
	public void testStop() {
		if (!AudioTestUtil.checkSoundEffects()) {
			return;
		}
		SoundEffect se = getTestEffect();
		
		se.play();
		se.stop();
		
		assertFalse(se.isPlaying());
	}

}
