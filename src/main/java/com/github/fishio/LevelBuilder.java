package com.github.fishio;

import java.util.ArrayList;

/**
 * The LevelBuilder is an utility class for creating levels. 
 * This class contains a standard level to be created.
 */
final class LevelBuilder {
	
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
		//Adding randomized enemy fish.
		EnemyFish eFish = new EnemyFish(new BoundingBox(0, 0, 100 , 100));
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
	
}
