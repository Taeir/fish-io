package com.github.fishio;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements IMovable {
	
	private double speedX = 0;
	private double speedY = 0;
	
	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	
	private KeyCode upKey = KeyCode.UP;
	private KeyCode downKey = KeyCode.DOWN;
	private KeyCode leftKey = KeyCode.LEFT;
	private KeyCode rightKey = KeyCode.RIGHT;
	
	/**
	 * The speed at which the speed of the fish increases /
	 * decreases depending on what keys are pressed by the user.
	 */
	private static final double ACCELERATION = 0.25; //TODO find a nicer acceleration value
	
	private static final double MAX_SPEED = 10; //TODO find a nicer max speed value
	
	/**
	 * @param bb
	 * 		The (inital) bounding box of the PlayerFish
	 * @param stage
	 * 		The scene in which the player fish is located at
	 */
	public PlayerFish(BoundingBox bb, Stage stage) {
		super(bb);
		
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
		if (leftPressed && -speedX < MAX_SPEED) speedX -= ACCELERATION;
		if (rightPressed && speedX < MAX_SPEED) speedX += ACCELERATION;
		
		if (speedX < 0 && (!leftPressed || rightPressed)) speedX += ACCELERATION;
		if (speedX > 0 && (!rightPressed || leftPressed)) speedX -= ACCELERATION;
	}
	
	/**
	 * Increases or decreases the speed of the fish in the
	 * vertical direction, depending on which keys are currently
	 * pressed by the user.
	 */
	@SuppressWarnings ("checkstyle:needbraces")
	public void adjustYSpeed() {
		if (downPressed && -speedY < MAX_SPEED) speedY -= ACCELERATION;
		if (upPressed && speedY < MAX_SPEED) speedY += ACCELERATION;
		
		if (speedY < 0 && (!downPressed || upPressed)) speedY += ACCELERATION;
		if (speedY > 0 && (!upPressed || downPressed)) speedY -= ACCELERATION;
	}
	
	/**
	 * @return the horizontal speed of the PlayerFish. A negative speed means
	 * 		the fish is going left.
	 */
	public double getSpeedX() {
		return speedX;
	}
	
	/**
	 * @return the vertical speed of the PlayerFish. A negative speed means
	 * 		the fish is going down.
	 */
	public double getSpeedY() {
		return speedY;
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
		this.speedX = speedX;
	}
	
	/**
	 * Sets the speed of the PlayerFish in the vertical direction.
	 * 
	 * @param speedY
	 * 		the speed to set
	 */
	public void setSpeedY(double speedY) {
		this.speedY = speedY;
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

	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(speedX, speedY);
	}

	@Override
	public void setSpeedVector(Vec2d vector) {
		speedX = vector.x;
		speedY = vector.y;
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
			double osize = other.getBoundingBox().getSize();
			
			if (tsize > osize) {
				fish.setDead();
				getBoundingBox().increaseSize(Math.pow(osize, 0.9));
			} else if (osize > tsize) {
				this.setDead();
			}
		}
	}
	
}
