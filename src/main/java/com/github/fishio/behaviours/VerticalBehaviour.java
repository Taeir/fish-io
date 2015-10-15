package com.github.fishio.behaviours;

import java.io.Serializable;

import com.github.fishio.Vec2d;

/**
 * A behaviour where the entity moves from the top of the screen to
 * the bottom of the screen in a straight line and constant speed.
 */
public class VerticalBehaviour implements IMoveBehaviour, Serializable {
	private static final long serialVersionUID = 7734877862028004209L;
	
	private double speed;
	
	/**
	 * Creates a new Behaviour that makes entities
	 * move from the top of the screen to the bottom.
	 * 
	 * @param speed
	 * 		The speed the entities with this behaviour should move at.
	 */
	public VerticalBehaviour(double speed) {
		this.speed = speed;
	}

	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(0, -speed);
	}
	@Override
	public void preMove() { }
	
}
