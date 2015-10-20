package com.github.fishio;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Contains methods to get sprites.
 */
public final class SpriteStore {
	private static final ConcurrentHashMap<String, Sprite> SPRITES = new ConcurrentHashMap<>();
	
	private static final HashSet<String> LOADING = new HashSet<>();
	
	private SpriteStore() { }
	
	/**
	 * Called to start loading the sprites.
	 */
	public static void loadSprites() {
		Log.getLogger().log(LogLevel.DEBUG, "[SpriteStore] Loading sprites...");
		
		MultiThreadedUtility.submitTask(() -> {
			//Load player fish
			getSpriteOrLoad("sprites/fish/playerFish.png");
			
			//Load enemy fish
			for (int i = 0; i < 29; i++) {
				getSpriteOrLoad("sprites/fish/fish " + i + ".png");
			}
			
			//Load special fish
			getSpriteOrLoad("sprites/fish/special/barrelFish.png");
			getSpriteOrLoad("sprites/fish/special/clownFish1.png");
			getSpriteOrLoad("sprites/fish/special/clownFish2.png");
			getSpriteOrLoad("sprites/fish/special/jellyfish.png");
			getSpriteOrLoad("sprites/fish/special/submarine.png");
			getSpriteOrLoad("sprites/fish/special/swordfish.png");
			getSpriteOrLoad("sprites/fish/special/turtle.png");
			
			//Load powerup sprites
			for (int i = 0; i < 3; i++) {
				getSpriteOrLoad("sprites/powerup/pu" + i + ".png");
			}
		}, false);
		
		//Load extra sprites
		MultiThreadedUtility.submitTask(() -> {
			getSpriteOrLoad("sprites/anchor1.png");
			getSpriteOrLoad("sprites/anchor2.png");
			getSpriteOrLoad("sprites/fishingPole.png");
			getSpriteOrLoad("sprites/float.png");
			getSpriteOrLoad("sprites/seaweed1.png");
			getSpriteOrLoad("sprites/starfish.png");
		}, false);
	}
	
	/**
	 * Gets the sprite for the given location.
	 * If this sprite is not yet loaded, it is loaded first.
	 * 
	 * @param location
	 * 		the location of the sprite.
	 * 
	 * @return
	 * 		the Sprite at the given location.
	 */
	public static Sprite getSpriteOrLoad(String location) {
		Sprite sprite = SPRITES.get(location);
		if (sprite != null) {
			return sprite;
		}
		
		boolean load = false;
		try {
			synchronized (LOADING) {
				//NOTE: Add returns true when the item was not yet in the set.
				//If not yet being loaded, we start loading it.
				if (LOADING.add(location)) {
					load = true;
				} else {
					//Otherwise, we wait for it to be loaded.
					try {
						while (LOADING.contains(location)) {
							LOADING.wait();
						}
					} catch (InterruptedException ex) {
						return null;
					}
				}
			}
			
			//The sprite is now loaded, so we can return it.
			if (!load) {
				return SPRITES.get(location);
			}
			
			//Load and add the sprite
			sprite = new Sprite(location);
			SPRITES.put(location, sprite);
			return sprite;
		} finally {
			if (load) {
				synchronized (LOADING) {
					LOADING.remove(location);
					LOADING.notifyAll();
				}
			}
		}
	}
}
