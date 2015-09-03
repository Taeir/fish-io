package com.github.fishio;

/**
 * Represents an object that has a position.
 */
public interface IPositional {
	/**
	 * @return
	 * 		the (centre) x coordinate of this object.
	 */
	double getX();
	
	/**
	 * @return
	 * 		the (centre) y coordinate of this object.
	 */
	double getY();
}
