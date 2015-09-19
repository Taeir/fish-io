package com.github.fishio.achievements;

/**
 * Represents an object that is a subject for an observer.
 *
 */
public interface Subject {
	/**
	 * Attaches an observer to the subject.
	 * 
	 * @param observer
	 *            the object that wants to track changes to the subject.
	 */
	void attach(Observer observer);
	
	/**
	 * Removes an observer from the subject.
	 * 
	 * @param observer
	 *            the object that tracked changes to the subject.
	 */
	void detach(Observer observer);
	
	/**
	 * Gives all the attached Observers a notification when something changes.
	 * 
	 */
	void notifyObservers();
}
