package com.github.fishio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fishio.achievements.AchievementObserver;
import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements ICollidable, IDrawable, Subject, Serializable {
	private static final long serialVersionUID = 650039406095374770L;
	
	protected static Log logger = Log.getLogger();
	protected static Settings settings = Settings.getInstance();
	
	private static AtomicInteger entityIdCounter = new AtomicInteger(0);
	
	private transient List<AchievementObserver> observers = new ArrayList<AchievementObserver>();
	
	private int entityId;
	private SimpleBooleanProperty deathProperty = new SimpleBooleanProperty();
	private CollisionMask boundingArea;
	private IMoveBehaviour behaviour;
	
	/**
	 * This constructor creates an entity in the game.
	 * 
	 * @param boundingArea
	 *            the bounding area of this Entity
	 * @param behaviour
	 * 		The behaviour of this entity.
	 */
	public Entity(CollisionMask boundingArea, IMoveBehaviour behaviour) {
		this.boundingArea = boundingArea;
		this.behaviour = behaviour;
		
		this.entityId = getFreeEntityId();
	}

	/**
	 * @return
	 * 		a free entity id.
	 */
	private static int getFreeEntityId() {
		return entityIdCounter.getAndIncrement();
	}
	
	/**
	 * @return
	 * 		the entity id of this entity.
	 */
	public int getEntityId() {
		return entityId;
	}
	
	@Override
	public abstract void onCollide(ICollidable other);
	
	/**
	 * @return
	 * 		the death property of this Entity.
	 */
	public SimpleBooleanProperty getDeathProperty() {
		return this.deathProperty;
	}
	
	/**
	 * This method checks whether the entity is dead or not.
	 * 
	 * @return if the Entity is dead or not.
	 */
	public boolean isDead() {
		return deathProperty.get();
	}
	
	/**
	 * Kills this Entity.
	 */
	public void kill() {
		//We cannot die twice.
		if (isDead()) {
			return;
		}
		
		State oldState = getState();
		
		deathProperty.set(true);
		logger.log(LogLevel.TRACE, this.getClass().getSimpleName() + " got killed");
		
		//Notify observers that we have died.
		notifyObservers(oldState, getState(), "dead");
	}
	
	/**
	 * @return
	 * 		the current behaviour of this entity.
	 */
	public IMoveBehaviour getBehaviour() {
		return behaviour;
	}
	
	/**
	 * Changes the behaviour of this entity.
	 * 
	 * @param behaviour
	 * 		the behaviour this entity should adopt
	 */
	public void setBehaviour(IMoveBehaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	@Override
	public CollisionMask getBoundingArea() {
		return boundingArea;
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
	public void render(GraphicsContext gc, BoundingBox view) {
		//Don't render if dead or outside view.
		if (isDead() || !getBoundingArea().boxIntersects(view)) {
			return;
		}
		
		//Normally this method will be overridden by subclasses to render sprites.
		//So we simply draw a red box here.
		
		//No sprite rendering
		gc.setFill(Color.RED);
		
		CollisionMask cm = getBoundingArea();
		gc.fillRect(cm.getMinX(), cm.getMinY(), cm.getWidth(), cm.getHeight());
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
	 * Called when an entity hits a wall.
	 * 
	 * If not overrideen, this method will kill the entity.
	 */
	public void hitWall() {
		kill();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entity)) {
			return false;
		}
		
		return ((Entity) obj).getEntityId() == this.getEntityId();
	}
	
	@Override
	public int hashCode() {
		return this.getEntityId();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeBoolean(this.isDead());
		out.writeObject(this.boundingArea);
		out.writeObject(this.behaviour);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.entityId = in.readInt();
		this.deathProperty = new SimpleBooleanProperty(in.readBoolean());
		this.boundingArea = (CollisionMask) in.readObject();
		this.behaviour = (IMoveBehaviour) in.readObject();
		
		this.observers = new ArrayList<AchievementObserver>(0);
	}
}
