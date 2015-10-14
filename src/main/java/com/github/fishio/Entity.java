package com.github.fishio;

import java.util.ArrayList;
import java.util.List;

import com.github.fishio.achievements.AchievementObserver;
import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements ICollidable, IPositional, IDrawable, Subject {
	private List<AchievementObserver> observers = new ArrayList<AchievementObserver>();
	
	private boolean isDead;
	private ICollisionArea boundingArea;
	protected Log logger = Log.getLogger();
	
	/**
	 * This constructor creates an entity in the game.
	 * 
	 * @param boundingArea
	 *            the bounding area of this Entity
	 */
	public Entity(ICollisionArea boundingArea) {
		this.boundingArea = boundingArea;
	}
	
	@Override
	public abstract void onCollide(ICollidable other);
	
	/**
	 * This method checks whether the entity is dead or not.
	 * 
	 * @return if the Entity is dead or not.
	 */
	public boolean isDead() {
		return isDead;
	}
	
	/**
	 * Marks this Entity as dead.
	 */
	public void kill() {
		//We cannot die twice.
		if (isDead) {
			return;
		}
		
		State oldState = getState();
		
		isDead = true;
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " got killed");
		
		//Notify observers that we have died.
		notifyObservers(oldState, getState(), "dead");
	}
	
	/**
	 * @return
	 * 		The behaviour of this entity.
	 */
	public abstract IMoveBehaviour getBehaviour();
	
	/**
	 * Changes the behaviour of this entity.
	 * 
	 * @param behaviour
	 * 		The behaviour this entity should adopt
	 */
	public abstract void setBehaviour(IMoveBehaviour behaviour);

	
	
	@Override
	public CollisionMask getBoundingArea() {
		return (CollisionMask) boundingArea;
	}
	
	@Override
	public void setBoundingArea(CollisionMask area) {
		this.boundingArea = area;
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
	public List<AchievementObserver> getObservers() {
		return observers;
	}
	
	@Override
	public State getState() {
		State state = new State();
		state.add("dead", isDead());
		return state;
	}
	
	/**
	 * @return
	 * 		True iff this entity is able to move through walls.
	 */
	public abstract boolean canMoveThroughWall();

	/**
	 * What happens when the entity hits a wall, by default it kills the entity.
	 */
	public void hitWall() {
		kill();
	}
}
