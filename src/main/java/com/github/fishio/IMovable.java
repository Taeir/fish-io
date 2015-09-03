package com.github.fishio;

/**
 * Represents an object that can move.
 */
public interface IMovable extends IPositional {
	/**
	 * Called every tick.
	 */
	void move();
	
	/**
	 * @return
	 * 		the direction this object is moving in.
	 */
	Direction getDirection();

	/**
	 * Sets the direction this object is moving in.
	 * @param dir
	 * 		the new direction.
	 */
	default void setDirection(Direction dir) {
		setRadDirection(dir.getRadians());
	}
	
	/**
	 * @return
	 * 		the direction this object is moving in, in radians.
	 */
	double getRadDirection();
	
	/**
	 * Sets the direction this object is moving in to the given amount
	 * of radians.
	 * 
	 * @param rad
	 * 		the direction in radians.
	 */
	void setRadDirection(double rad);
	
	
	/**
	 * @return
	 * 		the speed this object is moving at.
	 */
	double getSpeed();
	
	/**
	 * Sets the speed this object is moving at.
	 * 
	 * @param speed
	 * 		the new speed.
	 */
	void setSpeed(double speed);
	
	/**
	 * Called when collided with a wall.<br>
	 * <br>
	 * Can be used to turn around for example.
	 */
	void hitWall();
}
