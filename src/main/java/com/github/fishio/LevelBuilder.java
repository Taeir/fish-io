package com.github.fishio;

import java.util.Random;

import javafx.scene.image.Image;


/**
 * The LevelBuilder is an utility class for creating levels. 
 * This class contains a standard level to be created.
 */
public final class LevelBuilder {

	private static Random rand = new Random();

	// Fish statistics

	// movement
	/**
	 * The minimal and maximal speed the enemy fish can move are specified here.
	 */
	public static final double MAX_EFISH_SPEED = 4;
	public static final double MIN_EFISH_SPEED = 1;
	
	/**
	 * The amount of fish sprites that we have.
	 */
	public static final int FISH_SPRITES = 28;

	/**
	 * Private constructor to prevent initiation.
	 */
	private LevelBuilder() {
		//to prevent initiation
	}

	/**
	 * Creates a random EnemyFish. This fish will get a sprite and always spawn
	 * outside the screen and always move towards the inside.
	 * 
	 * @param bb
	 *            A Bounding Box which decides about what size the fish will
	 *            have.
	 * @return random Enemyfish
	 */
	public static EnemyFish randomizedFish(BoundingBox bb) {
		//randomize fish properties 
		int minSize = (int) (bb.getSize() * 0.5);
		int maxSize = (int) (bb.getSize() * 2.5);

		int size = rand.nextInt(maxSize - minSize + 1) + minSize;
		Image sprite = getRandomSprite();
		double ratio = sprite.getWidth() / sprite.getHeight();
		double width = Math.sqrt(size * ratio);
		double height = size / width;

		double vx = 0.0, vy = 0.0;
		Vec2d position = null;
		//pick a side
		switch (rand.nextInt(5)) {
		case 0: 	// left
			position = new Vec2d(-width, Math.random() * PlayingField.WINDOW_Y);
			vx = Math.abs(randomSpeed());
			vy = randomSpeed();
			break;
		case 1: 	// top
			position = new Vec2d(Math.random() * PlayingField.WINDOW_X, -height);
			vx = randomSpeed();
			vy = -Math.abs(randomSpeed());
			break;
		case 2: 	// right
			position = new Vec2d(PlayingField.WINDOW_X + width, Math.random() * PlayingField.WINDOW_Y);
			vx = -Math.abs(randomSpeed());
			vy = randomSpeed();
			break;
		default: 	// bottom
			position = new Vec2d(Math.random() * PlayingField.WINDOW_X, PlayingField.WINDOW_Y + height);
			vx = randomSpeed();
			vy = Math.abs(randomSpeed());
			break;
		}

		EnemyFish eFish = new EnemyFish(new BoundingBox(position.x, position.y, 
				position.x + width , position.y + height), sprite , vx, vy);

		//TODO Check for decent properties
		//eFish.checkProperties()
		return eFish;
	}

	/**
	 * @return
	 * 		a random fish sprite.
	 */
	private static Image getRandomSprite() {
		final int i = rand.nextInt(FISH_SPRITES);
		return Preloader.getImageOrLoad("sprites/fish/fish" + i + ".png");
	}

	/**
	 * Creates a random speed for an enemy fish.
	 * 
	 * @return
	 * 			a random speed between 1 and MAX_EFISH_SPEED or between -1 and
	 * 			-MAX_FISH_SPEED
	 */
	public static double randomSpeed() {
		double speed = (Math.random() * 2 - 1) * MAX_EFISH_SPEED;

		// Check if speed is not too slow
		if (speed < 0) {
			speed = Math.min(speed, -1.0);
		} else {
			speed = Math.max(speed, 1.0);
		}

		return speed;
	}
}
