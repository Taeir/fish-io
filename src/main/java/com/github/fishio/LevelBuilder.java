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

	private static Image[] enemySpriteList;
	
	static {
		//Preload all the enemy fish sprites.
		enemySpriteList = new Image[28];
		for (int i = 0; i < 28; i++) {
			enemySpriteList[i] = new Image("sprites/fish/fish" + i + ".png");
		}
	}

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
	public static EnemyFish randomizedFish(IBoundingArea ba) {
		//randomize fish properties 
		int minSize = (int) (ba.getSize() * 0.5);
		int maxSize = (int) (ba.getSize() * 2.5);

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

	private static Image getRandomSprite() {
		int i = (int) (Math.random() * enemySpriteList.length);
		return enemySpriteList[i];
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
