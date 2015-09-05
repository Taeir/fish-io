package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable {
	private double vx;
	private double vy;
	private Color color;
	
	/**
	 * Main constructor of enemy fish.
	 * @param b Bounding box of enemy fish.
	 * @param colour color of the enemy fish.
	 * @param startvx starting speed in x direction
	 * @param startvy starting speed in y direction
	 */
	public EnemyFish(BoundingBox b, Color colour, double startvx, double startvy) {
		super(b);
		color = colour;
		vx = startvx;
		vy = startvy;
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
	public Vec2d getSpeedVector() {
		// TODO Auto-generated method stub
		return new Vec2d(vx, vy);
	}

	@Override
	public void setSpeedVector(Vec2d vector) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Enemy fish should die for now if they hit the wall;
	 */
	@Override
	public void hitWall() {
		setDead();
	}

	@Override
	public void preMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}
	
}
