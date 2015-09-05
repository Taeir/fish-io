package com.github.fishio;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity {
	private double vx;
	private double vy;
	
	/**
	 * Main constructor of enemy fish.
	 * @param bb Bounding box of enemy fish.
	 */
	public EnemyFish(BoundingBox bb) {
		super(bb);
		
	}

	
}