package com.github.fishio;

import java.util.Random;

import javafx.scene.image.Image;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.power_ups.PuExtraLife;
import com.github.fishio.power_ups.PuFreeze;
import com.github.fishio.power_ups.PuSuperSpeed;
import com.github.fishio.settings.Settings;

/**
 * A PowerUpSpawner repeatedly spawns random PowerUps
 * in a PlayingField every certain interval.
 */
public class PowerUpSpawner implements TickListener {

	private final PlayingField pf;
	
	private final int minX;
	private final int maxX;
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	
	private Log log;
	private Settings settings = Settings.getInstance();
	
	/**
	 * The amount of different PowerUps that are supported.
	 * This field should be updated with each new PowerUp added.
	 */
	private static final int POWERUP_COUNT = 3; 
	
	private Random rand = new Random();	

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
		
		this.intervalTicks = settings.getInteger("POWERUP_SPAWN_INTERVAL") * pf.getFPS();
		this.tickCounter = 0;
		
		this.minX = 0;
		this.maxX = pf.getWidth();
		
		this.log = Log.getLogger();
		
		pf.getGameThread().registerListener(this);
	}

	@Override
	public void preTick() { }

	@Override
	public void postTick() {
		tickCounter++;
		
		if (tickCounter % intervalTicks == 0) {
			PowerUp pu = getRandomPowerUp();
			pf.add(pu);
			log.log(LogLevel.DEBUG, "Added a PowerUp of type \"" + pu.getName() + "\"");
		}
	}
	
	/**
	 * @return
	 * 		A random PowerUp instances of the existing PowerUp classes.
	 */
	public PowerUp getRandomPowerUp() {
		
		//Creating the BoundingBox for the PowerUp with a on the
		//top of the screen and with a random x location.
		int x = rand.nextInt(maxX - minX + 1) + minX;
		int y = -HEIGHT; //(top of the screen: y=0, to spawn outside it, we need Ytop - height)
		
		int powerUpNumber = rand.nextInt(POWERUP_COUNT);
		
		String spritePath = "sprites/powerup/pu" + powerUpNumber + ".png";
		Image sprite = Preloader.getImageOrLoad(spritePath);
		
		CollisionMask cm = new CollisionMask(new Vec2d(x, y), WIDTH, HEIGHT, 
				Preloader.getAlphaDataOrLoad(spritePath),
				Preloader.getSpriteAlphaRatioOrLoad(spritePath));
		
		//Choosing a random PowerUp.
		switch (powerUpNumber) {
		case 0:
			return new PuFreeze(cm, pf, sprite);
		case 1:
			return new PuSuperSpeed(cm, pf, sprite);
		case 2:
			return new PuExtraLife(cm, pf, sprite);
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
		return settings.getInteger("POWERUP_SPAWN_INTERVAL");
	}
	
}
