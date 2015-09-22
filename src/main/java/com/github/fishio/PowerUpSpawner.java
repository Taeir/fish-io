package com.github.fishio;

import java.util.Random;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.power_ups.PuFreeze;

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
	 * The amount of seconds between each time
	 * a PowerUp is spawned.
	 */
	private static final int INTERVAL = 60;

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
			
			Random rand = new Random();
			
			int x = rand.nextInt(maxX - minX + 1) + minX;
			int y = rand.nextInt(maxY - minY + 1) + minY;
			
			BoundingBox bb = new BoundingBox(new Vec2d(x, y), WIDTH, HEIGHT);
			
			int powerUpCount = 1;
			switch (rand.nextInt(powerUpCount)) {
			case 0:
				pf.add(new PuFreeze(bb, pf));
				break;
			default:
				break;
			}
		}
	}
	
}
