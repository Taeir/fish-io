package com.github.fishio;

import java.util.Random;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.power_ups.PuFreeze;
import com.github.fishio.power_ups.PuSuperSpeed;

/**
 * A PowerUpSpawner repeatedly spawns random PowerUps
 * in a PlayingField every certain interval.
 */
public class PowerUpSpawner implements TickListener {

	private final PlayingField pf;
	
	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;
	
	private static final int WIDTH = 25;
	private static final int HEIGHT = 25;
	
	/**
	 * The amount of different PowerUps that are supported.
	 * This field should be updated with each new PowerUp added.
	 */
	private static final int POWERUP_COUNT = 2; 
	
	private Random rand = new Random();
	
	/**
	 * The amount of seconds between each time
	 * a PowerUp is spawned.
	 */
	private static final int INTERVAL = 10;

	private final int intervalTicks;
	private int tickCounter;
	
	/**
	 * Creates a new PowerUpSpawner and automatically
	 * registers it to the given PlayingField.
	 * 
	 * @param pf
	 * 		The PlayingField this PowerUpSpawner
	 * 		works on.
	 */
	public PowerUpSpawner(PlayingField pf) {
		this.pf = pf;
		pf.registerGameListener(this);
		
		this.intervalTicks = INTERVAL * pf.getFPS();
		this.tickCounter = 0;
		
		this.minX = 0;
		this.maxX = pf.getWidth();
		this.minY = 0;
		this.maxY = pf.getHeigth();
	}

	@Override
	public void preTick() { }

	@Override
	public void postTick() {
		tickCounter++;
		
		if (tickCounter % intervalTicks == 0) {
			pf.add(getRandomPowerUp());
		}
	}
	
	/**
	 * @return
	 * 		A random PowerUp instances of the existing PowerUp classes.
	 */
	public PowerUp getRandomPowerUp() {
		
		//Creating the BoundingBox for the PowerUp with a random start location.
		int x = rand.nextInt(maxX - minX + 1) + minX;
		int y = rand.nextInt(maxY - minY + 1) + minY;
		BoundingBox bb = new BoundingBox(new Vec2d(x, y), WIDTH, HEIGHT);
		
		//Choosing a random PowerUp.
		switch (rand.nextInt(POWERUP_COUNT)) {
		case 0:
			return new PuFreeze(bb, pf);
		case 1:
			return new PuSuperSpeed(bb, pf);
		default:
			return null;
		}
		
	}
	
	
	/**
	 * Sets the Random object used by this class.
	 * Useful for testing (setting a mocked Random object,
	 * then stubbing it).
	 * 
	 * @param rand
	 * 		The random to set
	 */
	public void setRandom(Random rand) {
		this.rand = rand;
	}
	
	/**
	 * @return
	 * 		The amount of PowerUps supported by this PowerUpSpawner.
	 */
	public int getPowerUpCount() {
		return POWERUP_COUNT;
	}
	
	/**
	 * @return
	 * 		The time in seconds before a new PowerUp spawns.
	 */
	public int getInterval() {
		return INTERVAL;
	}
	
}
