package com.github.fishio;

import com.github.fishio.logging.LogLevel;

public abstract class Fish extends Entity implements IMovable {

	public Fish(ICollisionArea ba) {
		super(ba);
	}
	
	public void eat(Fish other){
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " ate " + other.getClass().getSimpleName());
		other.kill();
	}

}
