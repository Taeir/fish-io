package com.github.fishio.listeners;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Interface for classes that TickListeners can be attached to.
 */
public interface Listenable {
	/**
	 * Registers the given TickListener for this Listenable.
	 * 
	 * @param listener
	 * 		the TickListener to register.
	 */
	default void registerListener(TickListener listener) {
		getListeners().add(listener);
	}
	
	/**
	 * Unregisters the given TickListener for this Listenable.
	 * 
	 * @param listener
	 * 		the TickListener to unregister.
	 */
	default void unregisterListener(TickListener listener) {
		getListeners().remove(listener);
	}
	
	/**
	 * Call the preTick method of all registered listeners.
	 */
	default void callPreTick() {
		for (TickListener tl : getListeners()) {
			try {
				tl.preTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				Log.getLogger().log(LogLevel.ERROR, "Error in preTick:\t" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Call the postTick method of all registered listeners.
	 */
	default void callPostTick() {
		for (TickListener tl : getListeners()) {
			try {
				tl.preTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				Log.getLogger().log(LogLevel.ERROR, "Error in preTick:\t" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @return
	 * 		the ConcurrentLinkedQueue of the registered listeners.
	 */
	ConcurrentLinkedQueue<TickListener> getListeners();
}
