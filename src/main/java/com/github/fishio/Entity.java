package com.github.fishio;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements ICollidable, IPositional, IDrawable {
	private boolean dead;
	private ICollisionArea ba;
	protected Log logger = Log.getLogger();
	
	/**
	 * This constructor creates an entity in the game.
	 * 
	 * @param ba
	 *            the bounding area of this Entity
	 */
	public Entity(ICollisionArea ba) {
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
	public void kill() {
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " got killed");
		dead = true;
	}
	
	@Override
	public ICollisionArea getBoundingArea() {
		return ba;
	}
	
	@Override
	public void setBoundingArea(ICollisionArea area) {
		this.ba = area;
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
