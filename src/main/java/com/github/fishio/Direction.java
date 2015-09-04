package com.github.fishio;

/**
 * Enum to represent the 4 basic (2d) directions:
 * Up, down, left, right.
 */
public enum Direction {
	RIGHT(0, new Vec2d(1, 0)),
	UP(0.5 * Math.PI, new Vec2d(0, 1)),
	LEFT(Math.PI, new Vec2d(-1, 0)),
	DOWN(1.5 * Math.PI, new Vec2d(0, -1));
	
	private final double rad;
	private final Vec2d normalVector;
	
	/**
	 * @param rad
	 * 		the radian rotation this Direction represents.
	 * @param normalVector
	 * 		the normalized vector corresponding to the Direction. 
	 */
	Direction(double rad, Vec2d normalVector) {
		this.rad = rad;
		this.normalVector = normalVector;
	}
	
	/**
	 * @return
	 * 		the radian rotation corresponding to this Direction.
	 */
	public double getRadians() {
		return this.rad;
	}
	
	/**
	 * @return
	 * 		the normalized vector corresponding to the Direction. 
	 */
	public Vec2d getNormalVector() {
		return new Vec2d(normalVector);
	}
}
