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
	 * @return
	 * 		the bounding box of this Collidable
	 */
	BoundingBox getBoundingBox();
	
	/**
	 * @param other
	 * 		the Collidable to check.
	 * @return
	 * 		if this collidable collides with the given collidable.
	 */
	boolean doesCollides(ICollidable other);
}
