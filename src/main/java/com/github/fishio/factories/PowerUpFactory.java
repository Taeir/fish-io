package com.github.fishio.factories;

import java.util.Random;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayingField;
import com.github.fishio.Sprite;
import com.github.fishio.SpriteStore;
import com.github.fishio.Vec2d;
import com.github.fishio.power_ups.ExtraLifePowerUp;
import com.github.fishio.power_ups.FreezePowerUp;
import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.power_ups.SuperSpeedPowerUp;
import com.github.fishio.settings.Settings;

/**
 * Is able to create different types of instances of the PowerUp class.
 */
public class PowerUpFactory {

	/**
	 * The amount of classes a PowerUpFactory supports.
	 */
	public static final int POWERUP_COUNT = 3;
	public static final int POWERUP_WIDTH = 25;
	public static final int POWERUP_HEIGHT = 25;
	
	private Settings settings = Settings.getInstance();

	private Random random;
	
	private PlayingField playingField;
	
	/**
	 * Creates a new PowerUpFactory.
	 * 
	 * @param playingField
	 * 		The PlayingField the PowerUps should belong to
	 */
	public PowerUpFactory(PlayingField playingField) {
		random = new Random();
		this.playingField = playingField;
	}
	
	/**
	 * @return
	 * 		A random PowerUp instances of the existing PowerUp classes.
	 */
	public PowerUp getRandomPowerUp() {
		
		//Creating the BoundingBox for the PowerUp with a on the
		//top of the screen and with a random x location.
		int x = random.nextInt((int) settings.getDouble("SCREEN_WIDTH") - 1);
		//(top of the screen: y=0, to spawn outside it, we need Ytop - height of the powerup)
		int y = -POWERUP_HEIGHT;
		
		int powerUpNumber = random.nextInt(POWERUP_COUNT);
		
		String spritePath = "sprites/powerup/pu" + powerUpNumber + ".png";
		Sprite sprite = SpriteStore.getSpriteOrLoad(spritePath);
		
		CollisionMask cm = new CollisionMask(new Vec2d(x, y), POWERUP_WIDTH, POWERUP_HEIGHT, sprite);

		//Choosing a random PowerUp.
		switch (powerUpNumber) {
		case 0:
			return new FreezePowerUp(cm, playingField, sprite);
		case 1:
			return new SuperSpeedPowerUp(cm, playingField, sprite);
		case 2:
			return new ExtraLifePowerUp(cm, playingField, sprite);
		default:
			return null;
		}
		
	}
	
	/**
	 * Sets the Random object used by this class.
	 * Useful for testing (setting a mocked Random object,
	 * then stubbing it).
	 * 
	 * @param random
	 * 		The random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}
}
