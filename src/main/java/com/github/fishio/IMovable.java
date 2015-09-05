package com.github.fishio;

/**
 * Represents an object that can move.
 */
public interface IMovable extends IPositional {

	/**
	 * @return
	 * 		the direction this object is moving in with
	 * 		the length of the vector being the speed of the object.
	 * 		Example: speedVector (0, 2) means the object is moving
	 * 		up with speed 2.
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
	 * Sets the direction this object is moving in.
	 * @param dir
	 * 		the new direction.
	 */
	default void setDirection(Direction dir) {
		setDirection(dir.getNormalVector());
	}
	
	/**
	 * @return
	 * 		The speed of this object.
	 */
	default double getSpeed() {
		return getSpeedVector().length();
	}
	
	/**
	 * Sets the speed this object is moving at.
	 * 
	 * @param speed
	 * 		the new speed.
	 */
	default void setSpeed(double speed) {
		Vec2d norm = getSpeedVector().normalize();
		norm.x = norm.x * speed;
		norm.y = norm.y * speed;
		setSpeedVector(norm);
	}
	
	/**
	 * Called when collided with a wall.<br>
	 * <br>
	 * Can be used to turn around for example.
	 */
	void hitWall();
	
	/**
	 * Called just before the object moves.
	 * 
	 * Can be used to adjust the speedVector for AI for example.
	 */
	void preMove();
}
