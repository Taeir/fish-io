package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable {
	//private double vx;
	//private double vy;
	private Color color;
	
	/**
	 * Main constructor of enemy fish.
	 * @param b Bounding box of enemy fish.
	 * @param colour color of the enemy fish.
	 */
	public EnemyFish(BoundingBox b, Color colour) {
		//, double startvx, double startvy
		//* @param bb Bounding box of enemy fish.
		super(b);
		color = colour;
		//vx = startvx;
		//vy = startvy;
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

	@Override
	public void move() {
		//TODO
		
	}

	@Override
	public Direction getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getRadDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRadDirection(double rad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpeed(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hitWall() {
		// TODO Auto-generated method stub
		
	}
	
}
