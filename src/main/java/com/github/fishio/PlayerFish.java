package com.github.fishio;

import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.KeyListenerBehaviour;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements IEatable, IPositional, Subject {

	private static final KeyCode KEY_UP = KeyCode.UP;
	private static final KeyCode KEY_DOWN = KeyCode.DOWN;
	private static final KeyCode KEY_LEFT = KeyCode.LEFT;
	private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;

	private Image sprite;

	private SimpleIntegerProperty score = new SimpleIntegerProperty(0);

	private static final double GROWTH_SPEED = 500;
	private static final double FISH_EAT_THRESHOLD = 1.2;
	private static final double DEFAULT_ACCELERATION = 0.1;
	private static final double DEFAULT_MAX_SPEED = 4;
	private static final int START_LIVES = 3;
	public static final int MAX_LIVES = 5;
	
	private SimpleIntegerProperty lives = new SimpleIntegerProperty(START_LIVES);
	
	private long invincible;
	
	private IMoveBehaviour behaviour;

	/**
	 * Creates the Player fish which the user will be able to control.
	 * 
	 * @param ca
	 *            The (inital) bounding area of the PlayerFish
	 * @param stage
	 *            The scene in which the player fish is located at
	 * @param sprite
	 *            The sprite of the player fish
	 */
	public PlayerFish(ICollisionArea ca, Stage stage, Image sprite) {
		super(ca);		

		this.sprite = sprite;
		
		this.behaviour = new KeyListenerBehaviour(stage, KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT,
				DEFAULT_ACCELERATION, DEFAULT_MAX_SPEED);
	}

	/**
	 * Gives back the growth rate of the Player Fish.
	 * 
	 * @return The rate at which the PlayerFish grows.
	 */
	public double getGrowthSpeed() {
		return GROWTH_SPEED;
	}

	@Override
	public void hitWall() {
		State old = getState();
		old.add("HitWall", false);
		
		State newState = getState();
		newState.add("HitWall", true);
		notifyObservers(old, newState, "HitWall");
	}
	
	/**
	 * Removes a life.
	 */
	@Override
	public void kill() {
		if (isDead()) {
			return;
		}
		//If invincible, ignore death.
		if (isInvincible()) {
			return;
		}
		int nvalue = Math.max(lives.get() - 1, 0);
		lives.set(nvalue);
		
		if (nvalue == 0) {
			super.kill();			
			return;
		}		
	}

	@Override
	public boolean canMoveThroughWall() {
		return false;
	}

	@Override
	public void onCollide(ICollidable other) {
		if (other instanceof IEatable) {
			IEatable eatable = (IEatable) other;
		
			if (eatable.canBeEatenBy(this)) {
				eatable.eat();
				this.addPoints((int) (eatable.getSize() / 200));
				double dSize = GROWTH_SPEED * eatable.getSize() / getSize();
				getBoundingArea().increaseSize(dSize);	
				State old = getState();
				notifyObservers(old, getState(), "EnemyKill");
			} 

			if (this.canBeEatenBy(eatable)) {
				eatable.eat(this);
			}
		}
	}

	/**
	 * Add points to the fish' score.
	 * 
	 * @param points
	 * 			points to add to the score.
	 */
	private void addPoints(int points) {
		score.set(points + score.intValue());
	}

	/**
	 * Getter for the score property.
	 * 
	 * @return
	 * 		the score
	 */
	public SimpleIntegerProperty scoreProperty() {
		return score;		
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		
		getBoundingArea().setRotation(behaviour); //update rotation;
		drawRotatedImage(gc, sprite, getBoundingArea());
	}
	

	/**
	 * Insta-kills the fish.
	 */
	public void setDead() {
		super.kill();
	}
	
	/**
	 * Adds a life.
	 */
	public void addLife() {
		lives.set(Math.min(lives.get() + 1, MAX_LIVES));
	}
	
	/**
	 * @return
	 * 		the amount of lives left.
	 */
	public int getLives() {
		return lives.get();
	}
	
	/**
	 * Getter for the lives property.
	 * 
	 * @return
	 * 		the amount of lives left.
	 */
	public SimpleIntegerProperty livesProperty() {
		return lives;
	}
	
	@Override
	public State getState() {
		State state = super.getState();
		state.add("EnemyKill", isDead()).add("score", score.get()).add("Lives", getLives());
		return state;
	}
	/**
	 * Make this PlayerFish invincible until endTime.
	 * 
	 * @param endTime
	 * 		the time in milliseconds when this PlayerFish should stop 
	 * 		being invincible. If <code>-1</code>, the invincibility
	 * 		lasts forever.
	 */
	public void setInvincible(long endTime) {
		if (endTime == -1) {
			invincible = Long.MAX_VALUE;
		} else {
			invincible = endTime;
		}
	}
	
	/**
	 * @return
	 * 		the time in milliseconds when this PlayerFish should stop
	 * 		being invincible.
	 */
	public long getInvincible() {
		if (!isInvincible()) {
			invincible = 0L;
		}
		
		return invincible;
	}
	
	/**
	 * @return
	 * 		if this PlayerFish is currently invincible.
	 */
	public boolean isInvincible() {
		return invincible != 0L && invincible > System.currentTimeMillis();
		
	}

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (isInvincible()) {
			return false;
		}		
		if (other.getSize() > getSize() * FISH_EAT_THRESHOLD) {
			return true;
		}
		return false;
	}

	@Override
	public void eat() {
		State old = getState();
		kill();
		notifyObservers(old, getState(), "Lives");
	}

	@Override
	public double getSize() {
		return getBoundingArea().getSize();
	}

	@Override
	public IMoveBehaviour getBehaviour() {
		return behaviour;
	}

	@Override
	public void setBehaviour(IMoveBehaviour behaviour) {
		this.behaviour = behaviour;
	}

}