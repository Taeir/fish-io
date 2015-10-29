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
	 * 
	 * @param logPrefix
	 * 		the prefix to use for logging error messages.
	 */
	default void callPreTick(String logPrefix) {
		for (TickListener tl : getListeners()) {
			try {
				tl.preTick();
			} catch (Exception ex) {
				if (logPrefix != null) {
					Log.getLogger().log(LogLevel.ERROR, "[" + logPrefix + "] Error in preTick:\t" + ex.getMessage());
				} else {
					Log.getLogger().log(LogLevel.ERROR, "Error in preTick:\t" + ex.getMessage());
				}
				
				Log.getLogger().log(LogLevel.DEBUG, ex);
			}
		}
	}
	
	/**
	 * Call the postTick method of all registered listeners.
	 * 
	 * @param logPrefix
	 * 		the prefix to use for logging error messages.
	 */
	default void callPostTick(String logPrefix) {
		for (TickListener tl : getListeners()) {
			try {
				tl.postTick();
			} catch (Exception ex) {
				if (logPrefix != null) {
					Log.getLogger().log(LogLevel.ERROR, "[" + logPrefix + "] Error in postTick:\t" + ex.getMessage());
				} else {
					Log.getLogger().log(LogLevel.ERROR, "Error in postTick:\t" + ex.getMessage());
				}
				
				Log.getLogger().log(LogLevel.DEBUG, ex);
			}
		}
	}
	
	/**
	 * @return
	 * 		the ConcurrentLinkedQueue of the registered listeners.
	 */
	ConcurrentLinkedQueue<TickListener> getListeners();
}
