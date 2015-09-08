package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements IMovable {

	private double vx = 0;
	private double vy = 0;

	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;

	private KeyCode upKey = KeyCode.UP;
	private KeyCode downKey = KeyCode.DOWN;
	private KeyCode leftKey = KeyCode.LEFT;
	private KeyCode rightKey = KeyCode.RIGHT;

	private Image sprite;

	/**
	 * The speed at which the speed of the fish increases /
	 * decreases depending on what keys are pressed by the user.
	 */
	private static final double ACCELERATION = 0.1; //TODO find a nicer acceleration value

	private static final double MAX_SPEED = 4; //TODO find a nicer max speed value

	private static final double GROWTH_SPEED = 1.50;
	private static final double FISH_EAT_THRESHOLD = 1.2;

	/**
	 * @param bb
	 * 		The (inital) bounding box of the PlayerFish
	 * @param stage
	 * 		The scene in which the player fish is located at
	 * @param sprite
	 * 		The sprite of the player fish
	 */
	public PlayerFish(BoundingBox bb, Stage stage, Image sprite) {
		super(bb);		

		this.sprite = sprite;

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			KeyCode pressedKey = event.getCode();
			if (pressedKey == upKey) {
				upPressed = true;
			} else if (pressedKey == downKey) {
				downPressed = true;
			} else if (pressedKey == leftKey) {
				leftPressed = true;
			} else if (pressedKey == rightKey) {
				rightPressed = true;
			}
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			KeyCode releasedKey = event.getCode();
			if (releasedKey == upKey) {
				upPressed = false;
			} else if (releasedKey == downKey) {
				downPressed = false;
			} else if (releasedKey == leftKey) {
				leftPressed = false;
			} else if (releasedKey == rightKey) {
				rightPressed = false;
			}
		});
	}

	/**
	 * Increases or decreases the speed of the fish in the
	 * horizontal direction, depending on which keys are currently
	 * pressed by the user.
	 */
	@SuppressWarnings ("checkstyle:needbraces")
	public void adjustXSpeed() {
		if (leftPressed && -vx < MAX_SPEED) vx -= ACCELERATION;
		if (rightPressed && vx < MAX_SPEED) vx += ACCELERATION;

		if (vx < 0 && (!leftPressed || rightPressed)) vx += ACCELERATION;
		if (vx > 0 && (!rightPressed || leftPressed)) vx -= ACCELERATION;
		
		if (vx < 0.1 && vx > -0.1) vx = 0;	// stop if speed is too slow
	}

	/**
	 * Increases or decreases the speed of the fish in the
	 * vertical direction, depending on which keys are currently
	 * pressed by the user.
	 */
	@SuppressWarnings ("checkstyle:needbraces")
	public void adjustYSpeed() {
		if (downPressed && -vy < MAX_SPEED) vy -= ACCELERATION;
		if (upPressed && vy < MAX_SPEED) vy += ACCELERATION;

		if (vy < 0 && (!downPressed || upPressed)) vy += ACCELERATION;
		if (vy > 0 && (!upPressed || downPressed)) vy -= ACCELERATION;
		
		if (vy < 0.1 && vy > -0.1) vy = 0;	// stop if speed is too slow
	}

	/**
	 * @return the horizontal speed of the PlayerFish. A negative speed means
	 * 		the fish is going left.
	 */
	public double getSpeedX() {
		return vx;
	}

	/**
	 * @return the vertical speed of the PlayerFish. A negative speed means
	 * 		the fish is going down.
	 */
	public double getSpeedY() {
		return vy;
	}

	/**
	 * @return the acceleration of the fish.
	 */
	public double getAcceleration() {
		return ACCELERATION;
	}

	/**
	 * Sets the speed of the PlayerFish in the horizontal direction.
	 * 
	 * @param speedX
	 * 		the speed to set
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
	 * @return
	 * 		The rate at which the PlayerFish grows.
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
	public void hitWall() { }

	@Override
	public boolean canMoveThroughWall() {
		return false;
	}

	@Override
	public void onCollide(ICollidable other) {
		if (other instanceof EnemyFish) {

			EnemyFish fish = (EnemyFish) other;
			if (fish.isDead()) {
				return;
			}

			double tsize = this.getBoundingBox().getSize();
			double osize = fish.getBoundingBox().getSize();

			if (tsize > osize * FISH_EAT_THRESHOLD) {
				fish.setDead();
				double dSize = Math.pow(GROWTH_SPEED * osize / tsize, 0.9);
				getBoundingBox().increaseSize(dSize);
			} else if (osize > tsize * FISH_EAT_THRESHOLD) {
				this.setDead();
			}
		}
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		if (vx >= 0) {
			gc.drawImage(sprite, getX(), getY(), getWidth(), getHeight());
		} else {
			gc.drawImage(sprite, getX() + getWidth(), getY(), -getWidth(), getHeight());
		}		
	}

}