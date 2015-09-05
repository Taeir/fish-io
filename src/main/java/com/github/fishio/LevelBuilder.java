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
	}
	
	/**
	 * Create standard Entities.
	 * @return a list of standard Entities
	 */
	public static ArrayList<Entity> standardEntities() {
		ArrayList<Entity> entityList = new ArrayList<>();
		//TODO add PlayerFish
		
		//Adding randomized enemy fish.
		entityList.add(new EnemyFish(new BoundingBox(0, 0, 10 , -10)));
		return entityList;
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
