package com.github.fishio.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.beans.property.SimpleObjectProperty;

import com.github.fishio.PlayingField;
import com.github.fishio.listeners.Listenable;
import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * A stateful runnable that represents the game thread.
 */
public class GameThread implements Runnable, Listenable {
	private ConcurrentLinkedQueue<TickListener> listeners = new ConcurrentLinkedQueue<TickListener>();
	
	private final PlayingField playingField;
	
	private volatile Thread thread;
	private volatile boolean stop;
	private volatile boolean done;
	
	//We keep a reset counter so that we can track if we missed resets.
	private int resetCounter;
	
	private SimpleObjectProperty<GameState> stateProperty = new SimpleObjectProperty<>(GameState.STOPPED);
	
	/**
	 * @param playingField
	 * 		the playingField to use this GameRunnable for.
	 */
	public GameThread(PlayingField playingField) {
		this.playingField = playingField;
	}
	
	/**
	 * @param playingField
	 * 		the playingField to use this GameRunnable for.
	 * @param fake
	 * 		if <code>true</code>, done and stop are set to <code>true</code>.
	 */
	public GameThread(PlayingField playingField, boolean fake) {
		this.playingField = playingField;
		this.done = fake;
		this.stop = fake;
	}
	
	/**
	 * Starts this GameRunnable, by creating a new thread for itself and
	 * starting it.<br>
	 * <br>
	 * If this GameRunnable is STARTING or RUNNING, this method has no 
	 * effect.
	 * <br>
	 * If this GameRunnable is STOPPING, this method will wait until the
	 * state changes to STOPPED, and then starts a new game thread.
	 */
	public void start() {
		//We are asked to stop (already), so we 
		if (stop) {
			//Wait until we have stopped
			try {
				stopAndWait();
			} catch (InterruptedException ex) {
				//Log interruptions, but don't do anything with them.
				Log.getLogger().log(LogLevel.ERROR,
						"[GameThread] Error while trying to start GameThread: interrupted while waiting for game "
						+ "thread to stop.");
			}
		}
		
		//Reset, so that we can start again.
		reset();
		
		//Set that we are starting
		stateProperty.set(GameState.STARTING);
		
		//Create a new thread and start it.
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Start the Game Thread and wait until it is started.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting for the game thread to 
	 * 		start.
	 */
	public void startAndWait() throws InterruptedException {
		//Start the game thread
		start();
		
		//Store reset counter for thread safety.
		int curReset = resetCounter;
		
		//Wait for the game thread to start.
		while (!isRunning() && curReset == resetCounter) {
			Thread.sleep(25L);
		}
	}

	/**
	 * Set the stop status of this GameRunnable to true.
	 */
	public void stop() {
		//If we have already stopped, or are not running, we don't have to do anything.
		if (thread == null || stop || done) {
			return;
		}
		
		this.stop = true;
		
		//If we are RUNNING, we are now STOPPING
		if (isRunning()) {
			stateProperty.set(GameState.STOPPING);
		}
	}
	
	/**
	 * Sets the stop status of this GameRunnable to true, and waits until
	 * it has actually stopped.<br>
	 * <br>
	 * If this Game Runnable is not STARTING or RUNNING, this method has 
	 * no effect.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	public void stopAndWait() throws InterruptedException {
		//Store resetCounter.
		int curReset = resetCounter;
		
		//Mark that we want to stop
		stop();
		
		//Wait until we have stopped, or reset has been called.
		while (!isStopped() && curReset == resetCounter) {
			if (thread != null) {
				thread.join(25L);
			} else {
				Thread.sleep(25L);
			}
		}
	}
	
	/**
	 * Resets this GameRunnables so that it can be started again.<br>
	 * <br>
	 * This method will have no effect if the state of this GameRunnable
	 * is STARTING, RUNNING or STOPPING.
	 */
	public void reset() {
		if (thread == null) {
			stop = false;
			done = false;
		} else if (done) {
			thread = null;
			stop = false;
			done = false;
		} else {
			return;
		}
		
		resetCounter++;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this game runnable has stopped.
	 */
	public boolean isStopped() {
		return this.thread == null || this.done;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this game runnable is still running.
	 * 		<code>false</code> otherwise.
	 */
	public boolean isRunning() {
		return !this.done && getState() == GameState.RUNNING;
	}
	
	/**
	 * @return
	 * 		the current state of the game.
	 */
	public GameState getState() {
		return stateProperty.get();
	}
	
	/**
	 * The state property can be used to add listeners to the game state.
	 * 
	 * @return
	 * 		the state property of this game.
	 */
	public SimpleObjectProperty<GameState> stateProperty() {
		return stateProperty;
	}
	
	@Override
	public void run() {
		//If we have already ran before, we throw an exception.
		if (isStopped()) {
			throw new IllegalStateException("This GameRunnable has already been stopped!");
		} else if (stop) {
			done = true;
			
			//We are now in the state STOPPED
			stateProperty.set(GameState.STOPPED);
			return;
		}
		
		//We are now in the state RUNNING
		stateProperty.set(GameState.RUNNING);
		
		try {
			long start, end;
			double waitTime = 1000.0 / PlayingField.GAME_TPS;
			
			while (!stop) {
				start = System.currentTimeMillis();

				//Call listeners pretick
				callPreTick("GameThread");

				//Move all movables
				playingField.moveMovables();

				//Add new entities
				playingField.addEntities();

				//Check for stop again halfway.
				if (stop) {
					break;
				}
				
				//Check for collisions
				playingField.checkPlayerCollisions();

				//Cleanup dead entities.
				playingField.cleanupDead();

				//Call listeners posttick
				callPostTick("GameThread");
				
				end = System.currentTimeMillis();
				
				//Sleep if we have time to spare.
				long dur = end - start;
				if (dur < waitTime) {
					try {
						Thread.sleep(Math.round(waitTime - dur));
					} catch (InterruptedException ex) { }
				}
			}
		} finally {
			//Reset stopping
			done = true;
			
			//We are now in the state STOPPED
			stateProperty.set(GameState.STOPPED);
		}
	}
	
	@Override
	public ConcurrentLinkedQueue<TickListener> getListeners() {
		return listeners;
	}
}
