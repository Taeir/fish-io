package com.github.fishio.factories;

import com.github.fishio.PlayingField;
import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.settings.Settings;

/**
 * A PowerUpSpawner repeatedly spawns random PowerUps
 * in a PlayingField every certain interval.
 */
public class PowerUpSpawner implements TickListener {
	
	private Log logger;
	private Settings settings = Settings.getInstance();

	private final int intervalTicks;
	private final PlayingField playingField;
	private int tickCounter;
	private PowerUpFactory factory;
	
	/**
	 * Creates a new PowerUpSpawner and automatically
	 * registers it to the given PlayingField.
	 * 
	 * @param pf
	 * 		The PlayingField this PowerUpSpawner
	 * 		works on.
	 */
	public PowerUpSpawner(PlayingField pf) {
		this.playingField = pf;
		this.intervalTicks = getInterval() * pf.getFPS();
		this.tickCounter = 0;
		this.factory = new PowerUpFactory(pf);
		
		this.logger = Log.getLogger();
		
		pf.getGameThread().registerListener(this);
	}

	@Override
	public void preTick() { }

	@Override
	public void postTick() {
		tickCounter++;
		
		if (tickCounter % intervalTicks == 0) {
			PowerUp pu = factory.getRandomPowerUp();
			playingField.add(pu);
			logger.log(LogLevel.DEBUG, "Added a PowerUp of type \"" + pu.getName() + "\"");
		}
	}
	
	/**
	 * @return
	 * 		The time in seconds before a new PowerUp spawns.
	 */
	public int getInterval() {
		return settings.getInteger("POWERUP_SPAWN_INTERVAL");
	}
	
}
