package com.github.fishio;

import com.github.fishio.achievements.State;
import com.github.fishio.achievements.Subject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements IEatable, IBehaviour, Subject {

	private double vx;
	private double vy;

	/**
	 * These factors have values for whether each of the arrow keys is pressed.
	 */
	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;

	private static final KeyCode KEY_UP = KeyCode.UP;
	private static final KeyCode KEY_DOWN = KeyCode.DOWN;
	private static final KeyCode KEY_LEFT = KeyCode.LEFT;
	private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;

	private Image sprite;

	private SimpleIntegerProperty score = new SimpleIntegerProperty(0);

	/**
	 * The speed at which the speed of the fish increases /
	 * decreases depending on what keys are pressed by the user.
	 */
	private double acceleration = 0.1;

	private double maxSpeed = 4;

	private static final double GROWTH_SPEED = 500;
	private static final double FISH_EAT_THRESHOLD = 1.2;
	private static final int START_LIVES = 3;
	public static final int MAX_LIVES = 5;
	
	private SimpleIntegerProperty lives = new SimpleIntegerProperty(START_LIVES);
	
	private long invincible;

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

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			KeyCode pressedKey = event.getCode();
			if (pressedKey == KEY_UP) {
				upPressed = true;
			} else if (pressedKey == KEY_DOWN) {
				downPressed = true;
			} else if (pressedKey == KEY_LEFT) {
				leftPressed = true;
			} else if (pressedKey == KEY_RIGHT) {
				rightPressed = true;
			}
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			KeyCode releasedKey = event.getCode();
			if (releasedKey == KEY_UP) {
				upPressed = false;
			} else if (releasedKey == KEY_DOWN) {
				downPressed = false;
			} else if (releasedKey == KEY_LEFT) {
				leftPressed = false;
			} else if (releasedKey == KEY_RIGHT) {
				rightPressed = false;
			}
		});
	}

	/**
	 * Increases or decreases the speed of the fish in the
	 * horizontal direction, depending on which keys are currently
	 * pressed by the user.
	 */
	public void adjustXSpeed() {
		if (leftPressed && -vx < maxSpeed) {
			vx -= acceleration;
		}
		if (rightPressed && vx < maxSpeed) {
			vx += acceleration;
		}

		if (vx < 0 && (!leftPressed || rightPressed)) {
			vx += acceleration;
		} else if (vx > 0 && (!rightPressed || leftPressed)) {
			vx -= acceleration;
		}

		if (vx < 0.1 && vx > -0.1) {
			vx = 0;	// stop if speed is too slow
		}
	}

	/**
	 * Increases or decreases the speed of the fish in the
	 * vertical direction, depending on which keys are currently
	 * pressed by the user.
	 */
	public void adjustYSpeed() {
		if (downPressed && -vy < maxSpeed) {
			vy -= acceleration;
		}

		if (upPressed && vy < maxSpeed) {
			vy += acceleration;
		}

		if (vy < 0 && (!downPressed || upPressed)) {
			vy += acceleration;
		} else if (vy > 0 && (!upPressed || downPressed)) {
			vy -= acceleration;
		}

		if (vy < 0.1 && vy > -0.1) {
			vy = 0;	// stop if speed is too slow
		}
	}

	/**
	 * Gives the horizontal speed of the Player Fish.
	 * 
	 * @return the horizontal speed of the PlayerFish. A negative speed means
	 *         the fish is going left.
	 */
	public double getSpeedX() {
		return vx;
	}

	/**
	 * Gives the vertical speed of the Player Fish.
	 * 
	 * @return the vertical speed of the PlayerFish. A negative speed means the
	 *         fish is going down.
	 */
	public double getSpeedY() {
		return vy;
	}

	/**
	 * Gives the acceleration the Player Fish has.
	 * 
	 * @return the acceleration of the fish.
	 */
	public double getAcceleration() {
		return acceleration;
	}
	
	/**
	 * Sets the acceleration of this PlayerFish.
	 * 
	 * @param acceleration
	 * 		The new acceleration of this PlayerFish
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	/**
	 * @return
	 * 		the maximum speed of this PlayerFish
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
	 * Sets the maximum speed of this PlayerFish.
	 * 
	 * @param maxSpeed
	 * 		The new maximum speed of this PlayerFish.
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Sets the speed of the Player Fish in the horizontal direction.
	 * 
	 * @param speedX
	 *            the speed to set
	 */
	public void setSpeedX(double speedX) {
		this.vx = speedX;
	}

	/**
	 * Sets the speed of the PlayerFish in the vertical direction.
	 * 
	 * @param speedY
	 * 		the speed to set
	 */
	public void setSpeedY(double speedY) {
		this.vy = speedY;
	}

	/**
	 * Sets whether the class believes if the 
	 * up key is pressed or not. 
	 * 
	 * @param upPressed
	 * 		Whether the up key is pressed or not.
	 */
	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	/**
	 * Sets whether the class believes if the 
	 * down key is pressed or not. 
	 * 
	 * @param downPressed
	 * 		Whether the down key is pressed or not.
	 */
	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	/**
	 * Sets whether the class believes if the 
	 * left key is pressed or not. 
	 * 
	 * @param leftPressed
	 * 		Whether the left key is pressed or not.
	 */
	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	/**
	 * Sets whether the class believes if the 
	 * right key is pressed or not. 
	 * 
	 * @param rightPressed
	 * 		Whether the right key is pressed or not.
	 */
	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
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
	public Vec2d getSpeedVector() {
		return new Vec2d(getSpeedX(), getSpeedY());
	}

	@Override
	public void setSpeedVector(Vec2d vector) {
		vx = vector.x;
		vy = vector.y;
	}

	@Override
	public void preMove() {
		adjustXSpeed();
		adjustYSpeed();
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
		getBoundingArea().setRotation(this); //update rotation;

		if (vx > 0) {
			drawRotatedImage(gc, sprite, getBoundingArea(), false);
		} else if (vx < 0) {
			drawRotatedImage(gc, sprite, getBoundingArea(), true);
		} else {
			drawRotatedImage(gc, sprite, getBoundingArea(), vy < 0);
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

}