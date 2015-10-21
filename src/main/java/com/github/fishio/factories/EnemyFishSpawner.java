package com.github.fishio.factories;

import javafx.beans.property.SimpleIntegerProperty;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.PlayingField;

/**
 * Spawns EnemyFishes on a PlayingField.
 */
public class EnemyFishSpawner {

	private PlayingField playingField;
	private EnemyFishFactory factory;
	private SimpleIntegerProperty maxEnemiesProperty = new SimpleIntegerProperty();
	
	/**
	 * Creates a new EnemyFishSpawner for the given playingField.
	 * 
	 * @param playingField
	 * 		The playingField on which this spawner should spawn EnemyFishes.
	 * @param maxEnemies
	 * 		The maximum amount of living EnemyFishes that can be in the PlayingField.
	 */
	public EnemyFishSpawner(PlayingField playingField, int maxEnemies) {
		this.playingField = playingField;
		this.maxEnemiesProperty.set(maxEnemies);
		
		this.factory = new EnemyFishFactory();
	}

	/**
	 * Spawns new enemy fish, until the maximum is reached.
	 */
	public void spawnEnemyFish() {
		for (int enemyCount = enemyCount(); enemyCount < getMaxEnemies(); enemyCount++) {
			addEnemyFish();
		}
	}
	
	/**
	 * @return
	 * 		the max enemies property.
	 */
	public SimpleIntegerProperty getMaxEnemiesProperty() {
		return maxEnemiesProperty;
	}
	
	/**
	 * @return
	 * 		the maximum amount of enemies.
	 */
	public int getMaxEnemies() {
		return maxEnemiesProperty.get();
	}
	
	/**
	 * Sets the maximum amount of enemies in the field to the given value.
	 * 
	 * @param maxEnemies
	 * 		the new maximum
	 */
	public void setMaxEnemies(int maxEnemies) {
		maxEnemiesProperty.set(maxEnemies);
	}
	
	/**
	 * @return
	 * 		The amount of living enemy fishes located in the playingField.
	 */
	private int enemyCount() {
		int enemyCount = 0;
		
		// Calculating the amount of enemies
		for (Entity e : playingField.getEntities()) {
			if (e instanceof EnemyFish && !((EnemyFish) e).isDead()) {
				enemyCount++;
			}
		}
		
		return enemyCount;
	}
	
	/**
	 * Adds a random EnemyFish to the PlayingField.
	 */
	private void addEnemyFish() {
		EnemyFish fish = factory.randomizedFish(playingField.getPlayers(),
				playingField.getWidth(), playingField.getHeight());
		playingField.add(fish);
	}
	
}
