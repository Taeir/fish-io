package com.github.fishio.behaviours;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.github.fishio.PlayerFish;
import com.github.fishio.Vec2d;
import com.github.fishio.settings.Settings;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A behaviour that listens for the user pressing keys.
 */
public class KeyListenerBehaviour implements IMoveBehaviour, Serializable {
	private static final long serialVersionUID = -8462037501296518403L;
	
	private double vx;
	private double vy;
	
	private double maxSpeed;
	private double acceleration;
	
	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	
	private Scene scene;
	private EventHandler<? super KeyEvent> pressHandler;
	private EventHandler<? super KeyEvent> releaseHandler;
	
	/**
	 * Creates a new KeyListenerBehaviour.
	 * 
	 * @param scene
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
	public KeyListenerBehaviour(Scene scene, KeyCode upKey, KeyCode downKey, 
			KeyCode leftKey, KeyCode rightKey, double acceleration, double maxSpeed) {
		this(acceleration, maxSpeed);
		
		registerHandlers(scene, upKey, downKey, leftKey, rightKey);
	}
	
	/**
	 * Creates a new KeyListenerBehaviour that does not register any key
	 * listeners.<br>
	 * <br>
	 * This should only be used by the
	 * {@link com.github.fishio.multiplayer.server.FishIOServer FishIOServer}.
	 * 
	 * @param acceleration
	 * 		The amount of speed that can change each tick.
	 * @param maxSpeed
	 * 		The maximum speed of the entity.
	 */
	public KeyListenerBehaviour(double acceleration, double maxSpeed) {
		this.acceleration = acceleration;
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * Creates a new KeyListenerBehaviour with default settings.
	 * 
	 * @param scene
	 * 		the scene to attach the keylisteners to.
	 * 
	 * @return
	 * 		a new KeyListenerBehaviour with default settings for the given stage.
	 */
	public static KeyListenerBehaviour createWithDefaultSettings(Scene scene) {
		Settings settings = Settings.getInstance();
		
		double maxSpeed = settings.getDouble("MAX_PLAYER_SPEED");
		double acceleration = PlayerFish.FISH_ACCELERATION;
		KeyCode keyUp = settings.getKeyCode("SWIM_UP");
		KeyCode keyDown = settings.getKeyCode("SWIM_DOWN");
		KeyCode keyLeft = settings.getKeyCode("SWIM_LEFT");
		KeyCode keyRight = settings.getKeyCode("SWIM_RIGHT");
		
		return new KeyListenerBehaviour(scene, keyUp, keyDown, keyLeft, keyRight, acceleration, maxSpeed);
	}

	/**
	 * Registers handlers for key events.
	 * 
	 * @param scene
	 * 		the stage to register the handlers on.
	 * @param upKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes up. 
	 * @param downKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes down. 
	 * @param leftKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes left. 
	 * @param rightKey
	 * 		The KeyCode of the key that when pressed, the entity with this behaviour goes right. 
	 */
	private void registerHandlers(Scene scene, KeyCode upKey, KeyCode downKey, KeyCode leftKey, KeyCode rightKey) {
		this.scene = scene;
		
		//Create the press handler
		pressHandler = event -> {
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
		};
		scene.addEventHandler(KeyEvent.KEY_PRESSED, pressHandler);

		//Create the release handler
		releaseHandler = event -> {
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
		};
		scene.addEventHandler(KeyEvent.KEY_RELEASED, releaseHandler);
	}
	
	/**
	 * Unregisters key handlers from the scene they were registered to.
	 */
	public void unregisterKeyHandlers() {
		if (this.scene == null) {
			return;
		}
		
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, pressHandler);
		scene.removeEventHandler(KeyEvent.KEY_RELEASED, releaseHandler);
		this.scene = null;
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

	/**
	 * Sets whether this behaviour believes if the upKey is pressed.
	 * This should be used for testing purposes only.
	 * 
	 * @param upPressed
	 * 		Whether this behaviour believes that the upKey is pressed.
	 */
	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	/**
	 * Sets whether this behaviour believes if the downKey is pressed.
	 * This should be used for testing purposes only.
	 * 
	 * @param downPressed
	 * 		Whether this behaviour believes that the downKey is pressed.
	 */
	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	/**
	 * Sets whether this behaviour believes if the leftKey is pressed.
	 * This should be used for testing purposes only.
	 * 
	 * @param leftPressed
	 * 		Whether this behaviour believes that the leftKey is pressed.
	 */
	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	/**
	 * Sets whether this behaviour believes if the rightKey is pressed.
	 * This should be used for testing purposes only.
	 * 
	 * @param rightPressed
	 * 		Whether this behaviour believes that the rightKey is pressed.
	 */
	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}
	
	/**
	 * Sets the speedVector of this behaviour. Should be
	 * used for testing purposes only.
	 * 
	 * @param speedVector
	 * 		The speedVector of this behaviour.
	 */
	public void setSpeedVector(Vec2d speedVector) {
		this.vx = speedVector.x;
		this.vy = speedVector.y;
	}
	
	@Override
	public void updateTo(IMoveBehaviour behaviour) {
		if (!(behaviour instanceof KeyListenerBehaviour)) {
			throw new IllegalArgumentException("Cannot update behaviour to different type!");
		}
		
		KeyListenerBehaviour other = (KeyListenerBehaviour) behaviour;
		this.vx = other.vx;
		this.vy = other.vy;
		this.acceleration = other.acceleration;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeDouble(vx);
		out.writeDouble(vy);
		out.writeDouble(acceleration);
		out.writeDouble(maxSpeed);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.vx = in.readDouble();
		this.vy = in.readDouble();
		this.acceleration = in.readDouble();
		this.maxSpeed = in.readDouble();
	}
}
