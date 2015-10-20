package com.github.fishio;

/**
 * Interface for objects that can eat or can be eaten.
 */
public interface IEatable {

	/**
	 * Checks of this IEatable can be eaten by the given IEatable.
	 * 
	 * @param other
	 * 		The IEatable to check for.
	 * 
	 * @return
	 * 		<code>true</code> is this IEatable can be eaten by the given IEatable,
	 * 		<code>false</code> otherwise.
	 */
	boolean canBeEatenBy(IEatable other);
	
	/**
	 * Method called when this IEatable is eaten.
	 */
	void eat();
	
	/**
	 * Eats the given IEatable.<br>
	 * <br>
	 * Precondition: {@code other.canBeEatenBy(this) == true}
	 * 
	 * @param other
	 * 		the eatable to eat.
	 */
	default void eat(IEatable other) {
		other.eat();
	}
	
	/**
	 * @return
	 * 		The size of this eatable.
	 */
	double getSize();
}
