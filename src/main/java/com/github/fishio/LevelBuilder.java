package com.github.fishio;

import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * The LevelBuilder is an utility class for creating levels. 
 * This class contains a standard level to be created.
 */
final class LevelBuilder {
	
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
	
	// safe spawn area.
	public static final double SAFE_RADIUS = 100;
	
	/**
	 * Private constructor to prevent initiation.
	 */
	private LevelBuilder() {
		//to prevent initiation
		//TODO add assertion?
	}
	
	/**
	 * Creates a random EnemyFish, taking in account the current size of the player.
	 * @param playerBox current Playerfish BoundingBox
	 * @return random Enemyfish
	 */
	public static EnemyFish randomizedFish(BoundingBox playerBox) {
		//TODO add a random fish based on player current BoundingBox
		
		//randomize fish properties 
		double width   = MAX_FISH_WIDTH * Math.random();
		double height  = FISH_WIDTH_HEIGHT_RATIO * width * Math.random();
		width = Math.max(MIN_FISH_WIDTH, width);
		height = Math.max(MIN_FISH_HEIGTH, height);
		
		Vec2d position = randomPosition(playerBox, width, height);
		
		double vx = randomSpeed();
		double vy = randomSpeed();
				
		
		//Check for decent properties
		
		
		EnemyFish eFish = new EnemyFish(new BoundingBox(position.x, position.y, 
				position.x + width , position.y + height), randomColor() , vx, vy);
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
	 * Returns a random position as a Vec2d. 
	 * Makes sure that no fish can spawn near the player when player is still small.
	 * @return random position as a Vec2.
	 */
	private static Vec2d randomPosition(BoundingBox playerBox, double width, double height) {		
		
		//TODO Better spawn position calculation
		/*
		Random rand = new Random();
		double minXpb = playerBox.getMinX() - SAFERADIUS;
		double minYpb = playerBox.getMinY() + SAFERADIUS;
		double maxXpb = playerBox.getMaxX() + SAFERADIUS;
		double maxYpb = playerBox.getMaxY() - SAFERADIUS;
		
		double xPos;
		double yPos;
		
		// Check in which of the pairs of quadrant on the screen the fish will spawn
		if (rand.nextBoolean()) {
			// left bottom quadrant
			xPos = rand.nextDouble() * (minXpb - width);
			yPos = rand.nextDouble() * (PlayingField.WINDOW_Y - minYpb + height) + minYpb;
		} else {
			// right top quadrant
			xPos = rand.nextDouble() * (PlayingField.WINDOW_X - maxXpb) + maxXpb;
			yPos = rand.nextDouble() * (maxYpb - height);
		}*/
		
		return new Vec2d(Math.random() * PlayingField.WINDOW_X , Math.random() * PlayingField.WINDOW_Y);
	}
	
	/**
	 * Create a random rgb color.
	 * @return random color.
	 */
	private static Color randomColor() {
		return Color.rgb((int) (Math.random() * RGB_NUMBER), 
				(int) (Math.random() * RGB_NUMBER), 
				(int) (Math.random() * RGB_NUMBER));
	}
	
	public static double randomSpeed() {
		double speed = Math.random() * 2 * MAX_EFISH_SPEED - MAX_EFISH_SPEED;
		
		//check if speed is not too slow
		if (speed < 0) {
			speed = Math.min(speed, -1.0);
		} else {
			speed = Math.min(speed, 1.0);
		}
		
		return speed;
	}
}
