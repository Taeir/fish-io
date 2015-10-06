package com.github.fishio.audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Represents a playable sound.
 * The bytes representing the sound are cached for quick playback.
 */
public class Sound {
	/**
	 * The size of the buffers for reading sounds.
	 */
	private static final int READ_BUFFER_SIZE = 16384;
	
	private AudioFormat format;
	private boolean effect;
	private byte[] contents;
	
	/**
	 * Creates a new sound for the given InputStream.
	 * 
	 * @param inputStream
	 * 		the stream to read the sound from.
	 * @param mp3
	 * 		if the stream is in mp3 format, this parameter should be true.
	 * @param effect
	 * 		<code>true</code> if this is a sound effect,
	 * 		<code>false</code> if this is (background) music.
	 * 
	 * @throws IOException
	 * 		If an IOException occurs while reading the given inputstream.
	 * @throws UnsupportedAudioFileException
	 * 		If the audio format is unsupported.
	 */
	public Sound(InputStream inputStream, boolean mp3, boolean effect)
			throws IOException, UnsupportedAudioFileException {
		this.effect = effect;
		if (mp3) {
			readMp3Stream(inputStream);
		} else {
			readAudioStream(inputStream);
		}
	}
	
	/**
	 * Reads the given input stream as an audio file.
	 * 
	 * @param inputStream
	 * 		the stream to read from.
	 * 
	 * @throws IOException
	 * 		If an IOException occurs while reading the given input stream.
	 * @throws UnsupportedAudioFileException
	 * 		If the audio format is unsupported.
	 */
	protected void readAudioStream(InputStream inputStream) throws IOException, UnsupportedAudioFileException {
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream)) {
			//Load the format
			this.format = ais.getFormat();
			
			//Read the bytes from the audio file.
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
			int nRead;
			byte[] data = new byte[READ_BUFFER_SIZE];
	
			while ((nRead = ais.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
	
			buffer.flush();
	
			this.contents = buffer.toByteArray();
		}
	}
	
	/**
	 * Read an input stream in mp3 format.
	 * 
	 * @param inputStream
	 * 		the stream to read from.
	 * 
	 * @throws IOException
	 * 		If an IOException occurs while reading the given input stream.
	 * @throws UnsupportedAudioFileException
	 * 		If the audio format is unsupported.
	 */
	protected void readMp3Stream(InputStream inputStream)
			throws IOException, UnsupportedAudioFileException {
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream)) {
			//We want the file in the following format, so we can play it.
			this.format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
					ais.getFormat().getSampleRate(),
                    16,
                    ais.getFormat().getChannels(),
                    ais.getFormat().getChannels() * 2,
                    ais.getFormat().getSampleRate(),
                    false);
			
			try (AudioInputStream ais2 = AudioSystem.getAudioInputStream(this.format, ais)) {
				//Read the bytes from the audio file.
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[READ_BUFFER_SIZE];

				try {
					while ((nRead = ais2.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
					//If the MP3 is malformed, we can get an ArrayIndexOutOfBoundsException.
					Log.getLogger().log(LogLevel.INFO, "[Sound] Malformed MP3");
				}

				buffer.flush();

				this.contents = buffer.toByteArray();
			}
		}
	}
	
	/**
	 * @return
	 * 		a new FishClip that hold a clip for the audio fragment 
	 * 		corresponding to this Sound object.
	 * 
	 * @throws LineUnavailableException
	 * 		See {@link Clip#open()}.
	 */
	@SuppressWarnings("resource")
	public FishClip getClip() throws LineUnavailableException {
		Clip clip = AudioSystem.getClip();
		clip.open(format, contents, 0, contents.length);
		
		return new FishClip(clip, effect);
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this FishSound represents a Sound Effect.<br>
	 * 		<code>false</code> if it represents (background) music.
	 */
	public boolean isEffect() {
		return effect;
	}
}
