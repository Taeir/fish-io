package com.github.fishio.achievements;

import java.util.HashMap;
import java.util.Set;

/**
 * State class for the Subjects, that is used by Observers.
 */
public class State {
	private HashMap<String, Object> properties = new HashMap<>();
	
	/**
	 * Adds a property to this state.
	 * 
	 * @param property
	 *            the name of the property
	 * @param value
	 *            the value of the property
	 * 			
	 * @return this state, for chaining.
	 */
	public State add(String property, Object value) {
		properties.put(property, value);
		return this;
	}
	
	/**
	 * @param property
	 *            the name of the property to get.
	 * @param <T>
	 *            the type of the property. Used for convenience.
	 * 			
	 * @return the property of this state with the given name, or
	 *         <code>null</code> if it doesn't exist.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String property) {
		return (T) properties.get(property);
	}
	
	/**
	 * @return all the properties that this state has.
	 */
	public Set<String> getProperties() {
		return properties.keySet();
	}
	
}
