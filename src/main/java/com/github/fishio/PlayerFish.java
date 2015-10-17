package com.github.fishio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;
import com.github.fishio.behaviours.KeyListenerBehaviour;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements IEatable, Subject {
	private static final long serialVersionUID = 4226766216723804140L;
	
	public static final String SPRITE_LOCATION = "sprites/fish/playerFish.png";
	public static final double FISH_ACCELERATION = 0.1;
	
	private Image sprite;

	private SimpleIntegerProperty score = new SimpleIntegerProperty(0);	
	private SimpleIntegerProperty lives = new SimpleIntegerProperty(settings.getInteger("START_LIVES"));
	
	private long invincible;

	/**
	 * Creates the Player fish which the user will be able to control.
	 * 
	 * @param collisionMask
	 *            The (initial) collision mask of the PlayerFish
	 * @param scene
	 *            The scene in which the player fish is located at
	 * @param sprite
	 *            The sprite of the player fish
	 */
	public PlayerFish(CollisionMask collisionMask, Scene scene, Image sprite) {
		super(collisionMask, new KeyListenerBehaviour(scene, settings.getKeyCode("SWIM_UP"), 
				settings.getKeyCode("SWIM_DOWN"), settings.getKeyCode("SWIM_LEFT"), 
				settings.getKeyCode("SWIM_RIGHT"), FISH_ACCELERATION, 
				settings.getDouble("MAX_PLAYER_SPEED")));
		
		this.sprite = sprite;
	}
	
	/**
	 * Creates a PlayerFish with a KeyListenerBehaviour not listening to
	 * any keys.<br>
	 * <br>
	 * This constructor should only be used by the
	 * {@link com.github.fishio.multiplayer.server.FishIOServer FishIOServer}.
	 * 
	 * @param collisionMask
	 * 		the collision area to use
	 * @param sprite
	 * 		the sprite to use for the player fish.
	 */
	public PlayerFish(CollisionMask collisionMask, Image sprite) {
		super(collisionMask, new KeyListenerBehaviour(FISH_ACCELERATION, 
				settings.getDouble("MAX_PLAYER_SPEED")));
		
		this.sprite = sprite;
	}

	/**
	 * Gives back the growth rate of the Player Fish.
	 * 
	 * @return The rate at which the PlayerFish grows.
	 */
	public int getGrowthSpeed() {
		return settings.getInteger("GROWTH_SPEED");
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
			//Unregister key handlers
			if (getBehaviour() instanceof KeyListenerBehaviour) {
				((KeyListenerBehaviour) getBehaviour()).unregisterKeyHandlers();
			}
			
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
				State old = getState();
				
				eatable.eat();
				this.addPoints((int) (eatable.getSize() / 200));
				double dSize = getGrowthSpeed() * eatable.getSize() / getSize();
				getBoundingArea().increaseSize(dSize);	
				
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
		
		//TODO Move this to game thread.
		getBoundingArea().setRotation(getBehaviour()); //update rotation;
		
		//Only render if the fish has a sprite
		if (sprite != null) {
			drawRotatedImage(gc, sprite, getBoundingArea());
		} else {
			//Call the render method of entity, which simply renders a red box.
			super.render(gc);
		}
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
		lives.set(Math.min(lives.get() + 1, settings.getInteger("MAX_LIVES")));
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
		return invincible > System.currentTimeMillis();
	}

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (isInvincible()) {
			return false;
		}		
		if (other.getSize() > getSize() * settings.getDouble("FISH_EAT_THRESHOLD")) {
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

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(this.score.intValue());
		out.writeInt(this.lives.intValue());
		out.writeLong(this.invincible);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.score = new SimpleIntegerProperty(in.readInt());
		this.lives = new SimpleIntegerProperty(in.readInt());
		this.invincible = in.readLong();
		
		//Load the sprite
		this.sprite = SpriteStore.getSpriteOrLoad(SPRITE_LOCATION);
	}
}