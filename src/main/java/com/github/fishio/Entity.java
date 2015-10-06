package com.github.fishio;

import java.util.ArrayList;
import java.util.List;

import com.github.fishio.achievements.Observer;
import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;
import com.github.fishio.behaviours.IBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements ICollidable, IPositional, IBehaviour, IDrawable, Subject {
	private List<Observer> observers = new ArrayList<Observer>();
	
	private boolean dead;
	private ICollisionArea ba;
	protected Log logger = Log.getLogger();
	
	private IBehaviour behaviour;
	
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
		//We cannot die twice.
		if (dead) {
			return;
		}
		
		State oldState = getState();
		
		dead = true;
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " got killed");
		
		//Notify observers that we have died.
		notifyObservers(oldState, getState(), "dead");
	}
	
	/**
	 * @return
	 * 		The behaviour of this entity.
	 */
	public IBehaviour getBehaviour() {
		return this;
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
	
	@Override
	public List<Observer> getObservers() {
		return observers;
	}
	
	@Override
	public State getState() {
		State state = new State();
		state.add("dead", isDead());
		return state;
	}
}
