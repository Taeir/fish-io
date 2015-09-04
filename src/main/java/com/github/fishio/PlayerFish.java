package com.github.fishio;

import com.github.fishio.listeners.TickListener;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Represents a fish that the user can control using
 * the keyboard.
 */
public class PlayerFish extends Entity implements TickListener {
	
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
	private static final double ACCELERATION = 0.01; //TODO find a nicer acceleration value
	
	private static final double MAX_SPEED = Integer.MAX_VALUE; //TODO find a nicer max speed value
	
	/**
	 * @param bb
	 * 		The (inital) bounding box of the PlayerFish
	 * @param surface
	 * 		The scene in which the player fish is located at
	 */
	public PlayerFish(BoundingBox bb, Scene surface) {
		super(bb);
		
		surface.setOnKeyPressed(event -> {
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
		
		surface.setOnKeyReleased(event -> {
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
		if (leftPressed && -speedX > MAX_SPEED) speedX -= ACCELERATION;
		if (rightPressed && speedX > MAX_SPEED) speedX += ACCELERATION;
		
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
		if (downPressed && -speedY > MAX_SPEED) speedY -= ACCELERATION;
		if (upPressed && speedY > MAX_SPEED) speedY += ACCELERATION;
		
		if (speedY < 0 && (!downPressed || upPressed)) speedY += ACCELERATION;
		if (speedY > 0 && (!upPressed || downPressed)) speedY -= ACCELERATION;
	}
	
	/**
	 * @return the direction in which the fish is moving (not the one it's facing).
	 */
	public double getDirectionRad() {
		//If the fish only goes up or down
		if (speedX == 0) {
			if (speedY > 0) {
				return 0.5 * Math.PI;
			} else if (speedY < 0) {
				return -0.5 * Math.PI;
			} else {
				//Edge case, there is no direction.
				return -1;
			}
		}
		
		//All other rads can be calculated using arcTan(y/x)
		return Math.atan(speedY / speedX);
	}
	
	/**
	 * @return the total speed of the fish in the direction
	 * 		it is moving.
	 */
	public double getSpeed() {
		//Calculated using Pythagoras
		return Math.sqrt(speedX * speedX + speedY * speedY);
	}
	
	@Override
	public void preTick() {
		adjustXSpeed();
		adjustYSpeed();
		
		getBoundingBox().move(new Vec2d(speedX, speedY));
	}

	@Override
	public void postTick() {
		// TODO Auto-generated method stub
		
	}
	
}
