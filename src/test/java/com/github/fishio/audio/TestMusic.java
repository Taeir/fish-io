/**
 * 
 */
package com.github.fishio.audio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.gui.SlimGuiTest;

/**
 * Test class for {@link Music}.
 */
public class TestMusic extends SlimGuiTest {

	private boolean playing = false;
	private boolean state = false;
	private boolean stopped = false;

	/**
	 * Reset the playing and state boolean.
	 */
	@Before
	public void setUp() {
		playing = false;
		state = false;
		stopped = false;
	}

	/**
	 * @return
	 * 		a new Music for a test song, with a listener that will set
	 * 		playing to true when the music starts playing.
	 * 
	 * @see #waitForPlaying(long)
	 */
	private Music getTestMusic() {
		Path path = AudioUtil.getAudioFiles(false).get(0);
		Music music = new Music(path.toUri().toString());
		
		music.getPlayer().setOnPlaying(() -> {
			synchronized (this) {
				this.playing = true;
				notifyAll();
			}
		});
		
		music.setOnStop(() -> this.stopped = true);
		return music;
	}
	
	/**
	 * Wait until music has started playing.
	 * 
	 * @param time
	 * 		the maximum amount of time to wait, in milliseconds.
	 */
	private void waitForPlaying(long time) {
		long left = time;
		long interval = (long) Math.ceil(time / 50D);
		synchronized (this) {
			while (!playing && left > 0) {
				try {
					wait(interval);
				} catch (InterruptedException ex) { }
				
				left -= interval;
			}
		}
	}

	/**
	 * Wait until music has stopped playing.
	 * 
	 * @param time
	 * 		the maximum amount of time to wait, in milliseconds.
	 */
	private void waitForStop(long time) {
		long left = time;
		long interval = (long) Math.ceil(time / 50D);
		synchronized (this) {
			while (!stopped && left > 0) {
				try {
					wait(interval);
				} catch (InterruptedException ex) { }
				
				left -= interval;
			}
		}
	}
	
	/**
	 * Test for {@link Music#play()}.
	 */
	@Test
	public void testPlay() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		Music music = getTestMusic();
		
		music.play();
		waitForPlaying(7500L);
		
		assertTrue(playing);
	}

	/**
	 * Test method for {@link Music#isPlaying()}, when playing.
	 */
	@Test
	public void testIsPlaying_True() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		Music music = getTestMusic();
		
		//When playing is started, we set state to isPlaying.
		//If everything goes well, isPlaying should return true at this point.
		music.getPlayer().setOnPlaying(() -> {
			synchronized (this) {
				this.playing = true;
				this.state = music.isPlaying();
				notifyAll();
			}
		});
		
		music.play();
		waitForPlaying(7500L);
		
		assertTrue(this.state);
	}
	
	/**
	 * Test method for {@link Music#isPlaying()} and {@link Music#stop()}.
	 */
	@Test
	public void testIsPlaying_Stop() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		Music music = getTestMusic();
		
		music.play();
		waitForPlaying(7500L);
		music.stop();
		waitForStop(7500L);
		
		assertFalse(music.isPlaying());
	}

	/**
	 * Test method for {@link Music#setOnStop(Runnable)}.
	 */
	@Test
	public void testSetOnStop() {
		if (!AudioTestUtil.checkMusic()) {
			return;
		}
		
		Music music = getTestMusic();
		Runnable r = mock(Runnable.class);
		
		Runnable old = music.getPlayer().getOnStopped();
		music.getPlayer().setOnStopped(() -> {
			//Also call the old runnable.
			old.run();
			r.run();
		});
		
		//Start and stop music.
		music.play();
		waitForPlaying(7500L);
		music.stop();
		waitForStop(7500L);
		
		//Our runnable should have been called once.
		verify(r).run();
	}
}
