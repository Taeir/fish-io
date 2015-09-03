package com.github.fishio;

/**
 * Represents an object that can be collided with.
 */
public interface ICollidable {
	/**
	 * Called when this collidable collides with another collidable.
	 * 
	 * @param other
	 * 		The collidable that we have collided with.
	 * 
	 * @return
	 * 		?
	 */
	boolean onCollide(ICollidable other);
	
	/**
	 * The Collision Box is used to determine when one object collides with another object.
	 * 
	 * @return
	 * 		the collision box of this ICollidable.
	 */
	BoundingBox getBoundingBox();
	
	/**
	 * @param other
	 * 		the Collidable to check.
	 * @return
	 * 		if this collidable collides with the given collidable.
	 */
	default boolean doesCollides(ICollidable other) {
		if (other == null) {
			return false;
		}
		
		return getBoundingBox().intersects(other.getBoundingBox());
	}
}
