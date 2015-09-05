package com.github.fishio;

import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * The LevelBuilder is an utility class for creating levels. 
 * This class contains a standard level to be created.
 */
final class LevelBuilder {
	
	//Fish statistics
	private static final int MAXFISHWIDTH = 200;
	private static final double FISHWIDTHHEIGHTRATIO = 0.75;
	private static final double MINFISHWIDTH = 8;
	private static final double MINFISHHEIGTH = MINFISHWIDTH * FISHWIDTHHEIGHTRATIO;
	private static final int RGBNUMBER = 255;
	
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
		Vec2d position = randomPosition();
		double width   = MAXFISHWIDTH * Math.random();
		double height  = FISHWIDTHHEIGHTRATIO * width * Math.random();
		Color color = Color.rgb((int) (Math.random() * RGBNUMBER), 
				(int) (Math.random() * RGBNUMBER), 
				(int) (Math.random() * RGBNUMBER));
		
		//Check for decent properties
		width = Math.max(MINFISHWIDTH, width);
		height = Math.max(MINFISHHEIGTH, height);
		
		EnemyFish eFish = new EnemyFish(new BoundingBox(position.x, position.y, 
				position.x + width , position.y + height), color);
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
	 * @return random position as a Vec2.
	 */
	private static Vec2d randomPosition() {
		int maxX = PlayingField.WINDOW_X;
		int maxY = PlayingField.WINDOW_Y;
		
		double xPos = maxX * Math.random();
		double yPos = maxY * Math.random();
		return new Vec2d(xPos, yPos);
	}
}
