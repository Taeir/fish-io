package com.github.fishio;

/**
 * Represents an object that has a position.
 */
public interface IPositional {
	/**
	 * Gives back the minimal x coordinate of the positional object.
	 * 
	 * @return the minimal x coordinate of this object.
	 */
	default double getX() {
		return getBoundingArea().getMinX();
	}
	
	/**
	 * Gives back the minimal y coordinate of the positional object.
	 * 
	 * @return the minimal y coordinate of this object.
	 */
	default double getY() {
		return getBoundingArea().getMinY();
	}
	
	/**
	 * Gives back the width of the positional object.
	 * 
	 * @return the width of this object.
	 */
	default double getWidth() {
		return getBoundingArea().getWidth();
	}
	
	/**
	 * Gives back the height of this object.
	 * 
	 * @return the height of this object.
	 */
	default double getHeight() {
		return getBoundingArea().getHeight();
	}
	
	/**
	 * The position box is used to determine the position of this object.
	 * 
	 * @return
	 * 		a BoundingBox representing the position of this object.
	 */
	ICollisionArea getBoundingArea();
}
