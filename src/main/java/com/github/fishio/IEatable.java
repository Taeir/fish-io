package com.github.fishio;

/**
 * Interface for objects that can eat or can get eaten.
 */
public interface IEatable {

	/**
	 * checks of this object can be eaten by the other object.
	 * @param other
	 * 		The object to check for
	 * @return
	 * 		True is this can be eaten by other, false otherwise
	 */
	boolean canBeEatenBy(IEatable other);
	
	/**
	 * Method called when this is eaten.
	 */
	void eat();
	
	/**
	 * Eat the specified eatable.
	 * Pre: other.canBeEatenBy(this) == true
	 * @param other
	 * 		The eatable to eat.
	 */
	default void eat(IEatable other) {
		other.eat();
	}
	
	/**
	 * @return
	 * 		The size of this instance.
	 */
	double getSize();
}
