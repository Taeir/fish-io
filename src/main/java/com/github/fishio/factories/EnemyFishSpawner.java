package com.github.fishio.factories;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.PlayingField;
import com.github.fishio.listeners.TickListener;

/**
 * Spawns EnemyFishes on a PlayingField.
 */
public class EnemyFishSpawner implements TickListener {

	private PlayingField playingField;
	private int maxEnemies;
	private EnemyFishFactory factory;
	
	/**
	 * Creates a new EnemyFishSpawner and automatically
	 * registers it to the gameListener.
	 * 
	 * @param playingField
	 * 		The playingField on which this spawner should spawn EnemyFishes.
	 * @param maxEnemies
	 * 		The maximum amount of living EnemyFishes that can be in the PlayingField.
	 */
	public EnemyFishSpawner(PlayingField playingField, int maxEnemies) {
		this.playingField = playingField;
		this.maxEnemies = maxEnemies;
		
		this.factory = new EnemyFishFactory();
		
		playingField.getGameThread().registerListener(this);
	}

	@Override
	public void preTick() {
		for (int enemyCount = enemyCount(); enemyCount <= maxEnemies; enemyCount++) {
			addEnemyFish();
		}
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
		playingField.add(factory.randomizedFish(playingField.getPlayers(), 
				playingField.getWidth(), playingField.getHeight()));
	}

	@Override
	public void postTick() { }
	
}
