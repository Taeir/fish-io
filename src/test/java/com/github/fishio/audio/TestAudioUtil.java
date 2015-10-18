/**
 * 
 */
package com.github.fishio.audio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link AudioUtil}.
 */
public class TestAudioUtil {
	/**
	 * Test for {@link AudioUtil#getSoundEffectName(String)}.
	 */
	@Test
	public void testGetSoundEffectName() {
		assertEquals("hello", AudioUtil.getSoundEffectName("sound/effects/hello.mp3"));
	}
	
	/**
	 * Test for {@link AudioUtil#getSoundEffectName(String)}.
	 */
	@Test
	public void testGetSoundEffectName2() {
		assertEquals("sub/hey", AudioUtil.getSoundEffectName("sound/effects/sub/hey.wav"));
	}
	
	/**
	 * Test for {@link AudioUtil#getSoundEffectName(String)}.
	 */
	@Test
	public void testGetSoundEffectName3() {
		assertEquals("something/sub/hey.wav", AudioUtil.getSoundEffectName("something/sub/hey.wav"));
	}

	/**
	 * Test for {@link AudioUtil#getAudioFiles(boolean)}.
	 */
	@Test
	public void testGetAudioFiles() {
		List<Path> paths = AudioUtil.getAudioFiles(true);
		
		assertFalse(paths.isEmpty());
		assertEquals("test.mp3", paths.get(0).toFile().getName());
	}

	/**
	 * Test for {@link AudioUtil#isAudioFile(String)}.
	 */
	@Test
	public void testIsAudioFile() {
		assertTrue(AudioUtil.isAudioFile("test.mp3"));
	}
	
	/**
	 * Test for {@link AudioUtil#isAudioFile(String)}.
	 */
	@Test
	public void testIsAudioFile2() {
		assertTrue(AudioUtil.isAudioFile("test.wav"));
	}
	
	/**
	 * Test for {@link AudioUtil#isAudioFile(String)}.
	 */
	@Test
	public void testIsAudioFile3() {
		assertFalse(AudioUtil.isAudioFile("test.wav.jpg"));
	}

}
