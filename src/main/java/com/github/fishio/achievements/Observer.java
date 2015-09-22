package com.github.fishio.achievements;

/**
 * Represents an object that wants to observe a subject class.
 *
 */
public interface Observer {
	/**
	 * Updates the observer with a change to the subject.
	 */
	void update();
	
}
