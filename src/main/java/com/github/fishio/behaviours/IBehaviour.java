package com.github.fishio.behaviours;

import com.github.fishio.IPositional;
import com.github.fishio.Vec2d;

/**
 * The behaviour of an entity decides how it moves.
 */
public interface IBehaviour extends IPositional {

	/**
	 * Gets the speedVector of the object.
	 * 
	 * @return the direction this object is moving in with the length of the
	 *         vector being the speed of the object. Example: speedVector (0, 2)
	 *         means the object is moving up with speed 2.
	 */
	Vec2d getSpeedVector();
	
	/**
	 * Sets the speedVector of this object.
	 * Example: speedVector (0, 2) means the object is moving
	 * up with speed 2.
	 * 
	 * @param vector
	 * 		The speed vector of the object.
	 */
	void setSpeedVector(Vec2d vector);
	
	/**
	 * Sets the direction this object is moving in.
	 * 
	 * @param direction
	 * 		The direction the object is moving at.
	 * 		This method does not affect the speed.
	 */
	default void setDirection(Vec2d direction) {
		Vec2d norm = direction.normalize();
		double speed = getSpeedVector().length();
		norm.x = norm.x * speed;
		norm.y = norm.y * speed;
		setSpeedVector(norm);
	}
	
	/**
	 * Gives back the speed of the object, defined by the length of the
	 * SpeedVector.
	 * 
	 * @return The speed of this object.
	 */
	default double getSpeed() {
		return getSpeedVector().length();
	}
	
	/**
	 * Sets the speed this object is moving at.
	 * 
	 * @deprecated If the speed is 0, this method does not work properly.
	 * 
	 * @param speed
	 * 		the new speed.
	 */
	@Deprecated
	default void setSpeed(double speed) {
		Vec2d norm = getSpeedVector().normalize();
		norm.x = norm.x * speed;
		norm.y = norm.y * speed;
		setSpeedVector(norm);
	}
	
	/**
	 * Allows the object to move through the walls.
	 * 
	 * @return true iff the object doesn't get blocked when trying to move
	 *         through walls.
	 */
	boolean canMoveThroughWall();
	
	/**
	 * Called when collided with a wall.<br>
	 * <br>
	 * Can be used to turn around for example.
	 * 
	 * This method will be called AFTER preMove(), but
	 * BEFORE the object actually moves.
	 */
	void hitWall();
	
	/**
	 * Called just before the object moves.
	 * 
	 * Can be used to adjust the speedVector for AI for example.
	 */
	void preMove();
}
