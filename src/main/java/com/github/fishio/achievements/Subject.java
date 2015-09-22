package com.github.fishio.achievements;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an object that is a subject for an observer.
 *
 */
public interface Subject {
	List<Observer> getObservers();
	
	/**
	 * Attaches an observer to the subject.
	 * 
	 * @param observer
	 *            the object that wants to track changes to the subject.
	 */
	default void attach(Observer observer) {
		getObservers().add(observer);
	}
	
	/**
	 * Removes an observer from the subject.
	 * 
	 * @param observer
	 *            the object that tracked changes to the subject.
	 */
	default void detach(Observer observer) {
		getObservers().remove(observer);
	}
	
	/**
	 * Gives all the attached Observers a notification when something changes.
	 * 
	 */
	default void notifyObservers(State oldState, State newState, String... changed) {
		List<String> changedList = Arrays.asList(changed);
		for (Observer o : getObservers()) {
			o.update(oldState, newState, changedList);
		}
	}
	
	/**
	 * @return the current state of this Subject.
	 */
	State getState();
}
