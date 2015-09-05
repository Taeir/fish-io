package com.github.fishio.listeners;

/**
 * An interface for listening to ticks.
 */
public interface TickListener {
	/**
	 * Called at the start of the tick, before rendering, collisions checking and cleanup.
	 */
	void preTick();
	
//	/**
//	 * Called after rendering, but before collisions checking and cleanup.
//	 */
//	void preCollisions();
//	
//	/**
//	 * Called after rendering and collisions checking, but before cleaning up dead entities.
//	 */
//	void preCleanup();
	
	/**
	 * Called at the end of a tick, after rendering, collisions checking, and cleaning up dead entities.
	 */
	void postTick();
}
