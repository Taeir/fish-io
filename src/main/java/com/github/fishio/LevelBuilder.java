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
	public static final int MAX_FISH_WIDTH = 200;
	public static final double FISH_WIDTH_HEIGHT_RATIO = 0.75;
	public static final double MIN_FISH_WIDTH = 8;
	public static final double MIN_FISH_HEIGTH = MIN_FISH_WIDTH * FISH_WIDTH_HEIGHT_RATIO;

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
	 * @return random Enemyfish
	 */
	public static EnemyFish randomizedFish() {
		//randomize fish properties 
		double width   = Math.max(MIN_FISH_WIDTH, MAX_FISH_WIDTH * Math.random());
		double height  = Math.max(MIN_FISH_HEIGTH, FISH_WIDTH_HEIGHT_RATIO * width * Math.random());

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
