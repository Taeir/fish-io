package com.github.fishio.gui;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fishio.audio.AudioEngine;
import com.github.fishio.audio.IAudioFactory;
import com.github.fishio.audio.NoAudioFactory;

/**
 * Class for running GUI tests without sounds.<br>
 * <br>
 * This makes it so that GUI tests on travis do not constantly throw
 * exceptions because of lacking sound support.
 */
public abstract class NoSoundSlimGuiTest extends SlimGuiTest {
	private static IAudioFactory oldFactory;
	
	/**
	 * Before running any tests, sets the AudioFactory to the NoAudioFactory
	 * so travis does not give errors during the GUI tests.
	 */
	@BeforeClass
	public static void setUpClass() {
		//Store the old audio factory
		oldFactory = AudioEngine.getInstance().getAudioFactory();
		
		NoAudioFactory naf = new NoAudioFactory();
		AudioEngine.getInstance().setAudioFactory(naf);
	}
	
	/**
	 * Restore the old AudioFactory after all tests are done.
	 */
	@AfterClass
	public static void tearDownClass() {
		AudioEngine.getInstance().setAudioFactory(oldFactory);
	}
}
