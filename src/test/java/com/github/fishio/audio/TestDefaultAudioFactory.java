package com.github.fishio.audio;

/**
 * Test class for {@link DefaultAudioFactory}.
 */
public class TestDefaultAudioFactory extends TestIAudioFactory {

	@Override
	public IAudioFactory createAudioFactory() {
		return new DefaultAudioFactory();
	}

}
