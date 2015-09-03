package com.github.fishio;

/**
 * Represents an object that has a position.
 */
public interface IPositional {
	/**
	 * @return
	 * 		the (centre) x coordinate of this object.
	 */
	default double getX() {
		return getBoundingBox().getCenterX();
	}
	
	/**
	 * @return
	 * 		the (centre) y coordinate of this object.
	 */
	default double getY() {
		return getBoundingBox().getCenterY();
	}
	
	/**
	 * @return
	 * 		the width of this object.
	 */
	default double getWidth() {
		return getBoundingBox().getWidth();
	}
	
	/**
	 * @return
	 * 		the height of this object.
	 */
	default double getHeight() {
		return getBoundingBox().getHeight();
	}
	
	/**
	 * The position box is used to determine the position of this object.
	 * 
	 * @return
	 * 		a BoundingBox representing the position of this object.
	 */
	BoundingBox getBoundingBox();
}
