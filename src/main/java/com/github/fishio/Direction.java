package com.github.fishio;

/**
 * Enum to represent the 4 basic (2d) directions:
 * Up, down, left, right.
 */
public enum Direction {
	RIGHT(0),
	UP(0.5 * Math.PI),
	LEFT(Math.PI),
	DOWN(1.5 * Math.PI);
	
	private final double rad;
	
	/**
	 * @param rad
	 * 		the radian rotation this Direction represents.
	 */
	Direction(double rad) {
		this.rad = rad;
	}
	
	/**
	 * @return
	 * 		the radian rotation corresponding to this Direction.
	 */
	public double getRadians() {
		return this.rad;
	}
}
