package com.github.fishio.factories;

import java.util.Collection;
import java.util.Random;

import com.github.fishio.CollisionMask;
import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.ICollisionArea;
import com.github.fishio.Preloader;
import com.github.fishio.Vec2d;
import com.github.fishio.settings.Settings;

import javafx.scene.image.Image;


/**
 * The EnemyFishFactory is an utility class for creating EnemyFish.
 */
public final class EnemyFishFactory {

	private static Random random = new Random();
	private static Settings settings = Settings.getInstance();
	/**
	 * Private constructor to prevent initiation.
	 */
	private EnemyFishFactory() {
		//to prevent initiation
	}
	
	/**
	 * Creates a random EnemyFish. This fish will get a sprite and always spawn
	 * outside the screen and always move towards the inside.
	 * 
	 * @param ca
	 * 		a Collision Area which decides the minimum and maximum size
	 * 		of the fish being spawned.
	 * 
	 * @return
	 * 		a randomized EnemyFish.
	 */
	public static EnemyFish randomizedFish(ICollisionArea ca) {
		int minSize = (int) (ca.getSize() * 0.2);
		int maxSize = (int) (ca.getSize() * 4.5);
		
		return randomizedFish(minSize, maxSize);
	}
	
	/**
	 * Creates a random EnemyFish. This fish will get a sprite and always spawn
	 * outside the screen and always move towards the inside.
	 * 
	 * @param sizes
	 * 		a collection of entities used for determining the size of the
	 * 		fish being spawned.
	 * 
	 * @return
	 * 		a randomized EnemyFish.
	 */
	public static EnemyFish randomizedFish(Collection<? extends Entity> sizes) {
		if (sizes.isEmpty()) {
			return randomizedFish(500, 1000);
		}
		
		int minSize = Integer.MAX_VALUE;
		int maxSize = Integer.MIN_VALUE;
		
		for (Entity e : sizes) {
			int min = (int) (e.getBoundingArea().getSize() * 0.2);
			int max = (int) (e.getBoundingArea().getSize() * 4.5);
			
			if (min < minSize) {
				minSize = min;
			}
			if (max > maxSize) {
				maxSize = max;
			}
		}
		
		return randomizedFish(minSize, maxSize);
	}

	/**
	 * Creates a random EnemyFish. This fish will get a sprite and always spawn
	 * outside the screen and always move towards the inside.
	 * 
	 * @param minSize
	 * 		the minimum size of the fish
	 * @param maxSize
	 * 		the maximum size of the fish
	 * 
	 * @return
	 * 		a random Enemyfish
	 */
	public static EnemyFish randomizedFish(int minSize, int maxSize) {
		//randomize fish properties 
		int size = random.nextInt(maxSize - minSize + 1) + minSize;
		String spriteString = getRandomSprite();
		Image sprite = Preloader.getImageOrLoad(spriteString);
		boolean[][] data = Preloader.getAlphaDataOrLoad(spriteString);
		double relSize = Preloader.getSpriteAlphaRatioOrLoad(spriteString);
		//TODO use setSize() instead of width/height calculations
		double ratio = sprite.getWidth() / sprite.getHeight();
		double width = Math.sqrt(size * ratio);
		double height = size / width;

		double vx = 0.0, vy = 0.0;
		Vec2d position = null;
		//pick a side
		switch (random.nextInt(4)) {
		case 0: 	// left
			position = new Vec2d(-width, Math.random() * settings.getDouble("SCREEN_HEIGHT"));
			vx = Math.abs(randomSpeed());
			vy = randomSpeed();
			break;
		case 1: 	// top
			position = new Vec2d(Math.random() * settings.getDouble("SCREEN_WIDTH"), -height);
			vx = randomSpeed();
			vy = -Math.abs(randomSpeed());
			break;
		case 2: 	// right
			position = new Vec2d(settings.getDouble("SCREEN_WIDTH") + width,
					Math.random() * settings.getDouble("SCREEN_HEIGHT"));
			vx = -Math.abs(randomSpeed());
			vy = randomSpeed();
			break;
		default: 	// bottom
			position = new Vec2d(Math.random() * settings.getDouble("SCREEN_WIDTH"), 
					settings.getDouble("SCREEN_HEIGHT") + height);
			vx = randomSpeed();
			vy = Math.abs(randomSpeed());
			break;
		}

		EnemyFish eFish =
				new EnemyFish(new CollisionMask(position, width, height, data, relSize), spriteString, vx, vy);
		
		//TODO Check for decent properties
		//eFish.checkProperties()
		return eFish;
	}

	/**
	 * @return
	 * 		a random fish sprite.
	 */
	private static String getRandomSprite() {
		final int i = random.nextInt(28);
		return "sprites/fish/fish" + i + ".png";
	}

	/**
	 * Creates a random speed for an enemy fish.
	 * 
	 * @return
	 * 			a random speed between 1 and MAX_EFISH_SPEED or between -1 and
	 * 			-MAX_FISH_SPEED
	 */
	public static double randomSpeed() {
		double speed = (Math.random() * 2 - 1) * settings.getDouble("MAX_EFISH_SPEED");

		// Check if speed is not too slow
		if (speed < 0) {
			speed = Math.min(speed, -1.0);
		} else {
			speed = Math.max(speed, 1.0);
		}

		return speed;
	}
}
