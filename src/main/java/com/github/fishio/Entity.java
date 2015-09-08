package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements ICollidable, IPositional, IDrawable {
	private boolean dead;
	private IBoundingArea ba;
	
	/**
	 * This constructor creates an entity in the game.
	 * 
	 * @param ba
	 *            the bounding area of this Entity
	 */
	public Entity(IBoundingArea ba) {
		this.ba = ba;
	}
	
	@Override
	public abstract void onCollide(ICollidable other);
	
	/**
	 * This method checks whether the entity is dead or not.
	 * 
	 * @return if the Entity is dead or not.
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Marks this Entity as dead.
	 */
	public void setDead() {
		dead = true;
	}
	
	@Override
	public IBoundingArea getBoundingArea() {
		return ba;
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
		gc.setFill(Color.RED);
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
