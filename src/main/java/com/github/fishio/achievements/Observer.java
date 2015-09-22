package com.github.fishio.achievements;

import java.util.Collection;

/**
 * Represents an object that wants to observe a subject class.
 *
 */
public interface Observer {
	/**
	 * Updates the observer with a change to the subject.
	 * 
	 * @param oldState
	 *            the previous state.
	 * @param newState
	 *            the new state.
	 * @param properties
	 *            the properties that were changed.
	 */
	void update(State oldState, State newState, Collection<String> properties);
	
}
