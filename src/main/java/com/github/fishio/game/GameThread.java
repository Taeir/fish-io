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
	 * If this GameRunnable is STARTING, RUNNING or STOPPING, this method 
	 * has no effect and returns <code>false</code>.<br>
	 * <br>
	 * If this GameRunnable
	 * @return
	 * 		<code>true</code> if this GameRunnable was started.
	 * 		<code>false</code> otherwise.
	 */
	public boolean start() {
		if (thread != null) {
			return false;
		}
		
		//We are asked to stop (already), so we 
		if (stop) {
			done = true;
			stateProperty.set(GameState.STOPPED);
			return false;
		}
		
		//Set that we are starting
		stateProperty.set(GameState.STARTING);
		
		//Create a new thread and start it.
		thread = new Thread(this);
		thread.start();
		return true;
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
		
		//If we are in state STOPPING, we don't have to change it.
		//We cannot be in state STOPPED, since stop and done are both false.
		//If we are in state STARTING, the thread will switch the state to
		//STOPPED immediately.
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
		//We have already stopped.
		if (getState() == GameState.STOPPED) {
			return;
		}
		
		//Store resetCounter.
		int curReset = resetCounter;
		
		//Mark that we want to stop
		stop();
		
		//Wait until we have stopped, or reset has been called.
		while (!isStopped() && curReset == resetCounter) {
			if (thread != null) {
				thread.join(25L);
			}
		}
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
					} catch (InterruptedException ex) {
						Log.getLogger().log(LogLevel.DEBUG, "[GameThread] Exception while sleeping until next cycle");
					}
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
