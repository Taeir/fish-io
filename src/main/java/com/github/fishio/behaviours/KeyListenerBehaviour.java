package com.github.fishio.behaviours;

import com.github.fishio.Vec2d;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * A behaviour that listens for the user pressing keys.
 */
public class KeyListenerBehaviour implements IMoveBehaviour {

	private double vx;
	private double vy;
	
	private double maxSpeed;
	private double acceleration;
	
	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	
	/**
	 * Creates a new KeyListenerBehaviour.
	 * 
	 * @param stage
	 * 		The stage of the GUI on which the KeyListener should get registered to.
	 * @param upKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes up. 
	 * @param downKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes down. 
	 * @param leftKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes left. 
	 * @param rightKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes right. 
	 * @param acceleration
	 * 		The amount of speed that can change each tick.
	 * @param maxSpeed
	 * 		The maximum speed of the entity.
	 */
	public KeyListenerBehaviour(Stage stage, KeyCode upKey, KeyCode downKey, 
			KeyCode leftKey, KeyCode rightKey, double acceleration, double maxSpeed) {
		
		this.acceleration = acceleration;
		this.maxSpeed = maxSpeed;
		
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
	
	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(vx, vy);
	}

	@Override
	public void preMove() {
		adjustXSpeed();
		adjustYSpeed();
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
	 * @return
	 * 		The acceleration of the speedVector in this behaviour.
	 */
	public double getAcceleration() {
		return acceleration;
	}
	
	/**
	 * Sets the acceleration of the speedVector in this behaviour.
	 * 
	 * @param acceleration
	 * 		The new acceleration.
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	/**
	 * @return
	 * 		The highest speed the speedVector can take.
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
	 * Sets the maximum speed of the speedVector in this behaviour.
	 * 
	 * @param maxSpeed
	 * 		The new maximum speed.
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
