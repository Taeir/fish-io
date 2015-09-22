package com.github.fishio;

import com.github.fishio.logging.LogLevel;

/**
 * Abstract Fish class that is extended by all other fish.
 */
public abstract class Fish extends Entity implements IMovable {

	/**
	 * Constructor for a Fish.
	 * @param ca
	 * 		The collisionArea of a Fish.
	 */
	public Fish(ICollisionArea ca) {
		super(ca);
	}
	
	/**
	 * Eat an other fish.
	 * @param other
	 * 		The other fish that is being eaten.
	 */
	public void eat(Fish other) {
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " ate " + other.getClass().getSimpleName());
		other.kill();
	}

}
