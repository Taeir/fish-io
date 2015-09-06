package com.github.fishio;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;


/**
 * The LevelBuilder is an utility class for creating levels. 
 * This class contains a standard level to be created.
 */
final class LevelBuilder {

	private static Random rand = new Random();
	
	// Fish statistics

	// size
	public static final double MIN_FISH_WIDTH_HEIGHT_RATIO = 1.5;
	public static final double MAX_FISH_WIDTH_HEIGHT_RATIO = 3.5;

	// color
	public static final int RGB_NUMBER = 255;

	// movement
	public static final double MAX_EFISH_SPEED = 4;
	public static final double MIN_EFISH_SPEED = 1;

	/**
	 * Private constructor to prevent initiation.
	 */
	private LevelBuilder() {
		//to prevent initiation
		//TODO add assertion?
	}

	/**
	 * Creates a random EnemyFish..
	 * This fish will spawn outside the screen and always move towards the inside.
	 * @param bb
	 * 		A Bounding Box which decides about what size the fish will have.
	 * @return random Enemyfish
	 */
	public static EnemyFish randomizedFish(BoundingBox bb) {
		//randomize fish properties 
		int minSize = (int) (bb.getSize() * 0.5);
		int maxSize = (int) (bb.getSize() * 2.5);
		
		int size = rand.nextInt(maxSize - minSize + 1) + minSize;
		double ratio = rand.nextDouble() * (MAX_FISH_WIDTH_HEIGHT_RATIO - MIN_FISH_WIDTH_HEIGHT_RATIO) 
				+ MIN_FISH_WIDTH_HEIGHT_RATIO;
		
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
				position.x + width , position.y + height), randomColor() , vx, vy);

		//TODO Check for decent properties
		//eFish.checkProperties()
		return eFish;
	}

	/**
	 * Load level Entities from specified String from file.
	 * @param str name of level
	 * @return a list of specified Entities
	 */
	public static ArrayList<Entity> loadEntities(String str) {
		//TODO for campaign purposes
		return null;
	}

	/**
	 * Create a random rgb color.
	 * @return random color.
	 */
	private static Color randomColor() {
		return Color.rgb(rand.nextInt(RGB_NUMBER - 1), 
				rand.nextInt(RGB_NUMBER - 1), 
				rand.nextInt(RGB_NUMBER - 1));
	}

	/**
	 * Creates a random speed for a fish.
	 * @return a random speed between 1 and MAX_EFISH_SPEED or between -1 and -MAX_FISH_SPEED
	 */
	public static double randomSpeed() {
		double speed = (Math.random() * 2 - 1) * MAX_EFISH_SPEED;

		//check if speed is not too slow
		if (speed < 0) {
			speed = Math.min(speed, -1.0);
		} else {
			speed = Math.max(speed, 1.0);
		}

		return speed;
	}
}
