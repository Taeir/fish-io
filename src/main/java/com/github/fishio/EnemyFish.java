package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity {
	private double vx;
	private double vy;
	private Color color;
	
	/**
	 * Main constructor of enemy fish.
	 * @param bb Bounding box of enemy fish.
	 * @param colour color of the enemy fish.
	 */
	public EnemyFish(BoundingBox bb, Color colour) {
		super(bb);
		color = colour;
	}
	
	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		
		//No sprite rendering
		gc.setFill(color);
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
	}
	
}
