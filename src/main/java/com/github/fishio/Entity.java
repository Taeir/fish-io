package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public class Entity implements ICollidable, IPositional, IDrawable {
	private boolean dead;
	private BoundingBox bb;
	
	/**
	 * @param bb
	 * 		the bounding box of this Entity
	 */
	public Entity(BoundingBox bb) {
		this.bb = bb;
	}
	
	@Override
	public boolean onCollide(ICollidable other) {
		if (dead) {
			return false;
		}
		
		//TODO
		return false;
	}
	
	/**
	 * @return
	 * 		if the Entity is dead or not.
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Mark this Entity as dead.
	 */
	public void setDead() {
		dead = true;
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return bb;
	}
	
	@Override
	public void drawDeath(GraphicsContext gc) {
		//TODO animations
	}
	
	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		
		//No sprite rendering
		gc.setFill(Color.FUCHSIA);
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
