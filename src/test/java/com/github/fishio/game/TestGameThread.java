package com.github.fishio.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.listeners.Listenable;
import com.github.fishio.listeners.TestListenable;

/**
 * Test for the {@link GameThread} class.
 */
public class TestGameThread extends TestListenable {

	private static GameThread gameThread;
	private static SinglePlayerPlayingField sppf;
	
	/**
	 * Called before any tests are run. Creates a gameThread for a mock
	 * playing field.
	 */
	@BeforeClass
	public static void setUpClass() {
		sppf = Mockito.mock(SinglePlayerPlayingField.class);
		gameThread = new GameThread(sppf);
	}
	
	/**
	 * Stop the game thread after the tests.
	 * 
	 * @throws Exception
	 * 		if we are interrupted while waiting for the game thread to
	 * 		stop.
	 */
	@After
	public void breakDown() throws Exception {
		gameThread.stopAndWait();
	}
	
	@Override
	public Listenable getListenable() {
		return new GameThread(sppf);
	}

	/**
	 * Test for {@link GameThread#start()}.
	 */
	@Test
	public void testStart() {
		//Start the game thread.
		gameThread.start();
		
		//The game state should now be STARTING. If the thread starts really fast, it could also be
		//RUNNING, so we accept that as well.
		GameState state = gameThread.getState();
		assertTrue(state == GameState.STARTING || state == GameState.RUNNING);
	}

	/**
	 * Test for {@link GameThread#startAndWait()}.
	 */
	@Test
	public void testStartAndWait() {
		//Start the game thread, and wait for it to start.
		try {
			gameThread.startAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		//The game state should now be fully started, so RUNNING
		assertSame(GameState.RUNNING, gameThread.getState());
	}

	/**
	 * Test for {@link GameThread#stop()}.
	 */
	@Test
	public void testStop() {
		//Start the game thread, and wait for it to start.
		try {
			gameThread.startAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		//The game should now be RUNNING
		assertSame(GameState.RUNNING, gameThread.getState());
		
		//Stop the game thread.
		gameThread.stop();
		
		//The game state should now be STOPPING. If the thread reacts really fast, it could also be
		//STOPPED, so we accept that as well.
		GameState state = gameThread.getState();
		assertTrue(state == GameState.STOPPING || state == GameState.STOPPED);
	}

	/**
	 * Test for {@link GameThread#stopAndWait()}.
	 */
	@Test
	public void testStopAndWait() {
		//Start the game thread, and wait for it to start.
		try {
			gameThread.startAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		//The game should now be RUNNING
		assertSame(GameState.RUNNING, gameThread.getState());
		
		//Stop the game thread, and wait for it to stop.
		try {
			gameThread.stopAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		//The state should now be STOPPED.
		assertSame(GameState.STOPPED, gameThread.getState());
	}

	/**
	 * Test for {@link GameThread#isStopped()}, when stopped.
	 */
	@Test
	public void testIsStopped() {
		assertTrue(gameThread.isStopped());
	}
	
	/**
	 * Test for {@link GameThread#isStopped()}, when running.
	 */
	@Test
	public void testIsStopped2() {
		//Start the game thread, and wait for it to start.
		try {
			gameThread.startAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		//The game thread should now not be stopped.
		assertFalse(gameThread.isStopped());
	}

	/**
	 * Test for {@link GameThread#isRunning()}, when not running.
	 */
	@Test
	public void testIsRunning() {
		//Game thread should not be running by default.
		assertFalse(gameThread.isRunning());
	}
	
	/**
	 * Test for {@link GameThread#isRunning()}, when running.
	 */
	@Test
	public void testIsRunning2() {
		//Start the game thread, and wait for it to start.
		try {
			gameThread.startAndWait();
		} catch (InterruptedException ex) {
			fail(ex.getMessage());
		}
		
		assertTrue(gameThread.isRunning());
	}
}
