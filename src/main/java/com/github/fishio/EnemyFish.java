package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy Fish class.
 * This class contains all methods concerning enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IMovable {
	public double vx;
	public double vy;
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
	 * Enemy fish should die if they hit the wall from the inside.
	 * Otherwise do nothing
	 */
	@Override
	public void hitWall() {
		setDead();
	}
	
	/**
	 * Enemy fish sometimes change their movement speed.
	 * Only change one of their movement directions so the change looks more realistic.
	 */
	@Override
	public void preMove() {
//		if (Math.random() < 0.01) {
//			//Only change one direction
//			if (Math.random() <= 0.5) {
//				vy = vy + vy * (Math.random() - 0.5);
//			} else {
//				vx = vx + vx * (Math.random() - 0.5);
//			}
//		}

	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	@Override
	public void onCollide(ICollidable other) { }

}
