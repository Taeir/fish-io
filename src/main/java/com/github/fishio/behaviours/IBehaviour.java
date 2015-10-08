package com.github.fishio.behaviours;

import com.github.fishio.Vec2d;

/**
 * The behaviour of an entity decides how it moves.
 */
public interface IBehaviour {

	/**
	 * Gets the speedVector of the object.
	 * 
	 * @return the direction this object is moving in with the length of the
	 *         vector being the speed of the object. Example: speedVector (0, 2)
	 *         means the object is moving up with speed 2.
	 */
	Vec2d getSpeedVector();
	
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
	 * Called just before the object moves.
	 * 
	 * Can be used to adjust the speedVector for AI for example.
	 */
	void preMove();
}
