package com.github.fishio;

import com.github.fishio.audio.AudioEngine;
import com.github.fishio.behaviours.IBehaviour;
import com.github.fishio.behaviours.RandomBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * EnemyFish class. This class contains all methods concerning non-player or
 * enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IEatable {

	private Settings settings = Settings.getInstance();
	private Log logger = Log.getLogger();

	private Image sprite;

	private IBehaviour behaviour;
	
	/**
	 * Main constructor of the enemy fish.
	 * 
	 * @param ca
	 *            ICollisionArea of enemy fish object.
	 * @param sprite
	 *            Sprite of the enemy fish object.
	 * @param startvx
	 *            Starting speed of the enemy fish object in the x direction.
	 * @param startvy
	 *            Starting speed of the enemy fish object in the y direction.
	 */
	public EnemyFish(ICollisionArea ca, Image sprite, double startvx, double startvy) {
		super(ca);
		this.sprite = sprite;
		
		this.behaviour = new RandomBehaviour(startvx, startvy, settings.getDouble("DIRECTION_CHANGE_CHANCE"));
		
		logger.log(LogLevel.TRACE, "Created Enemfish: Properties{[position = " + ca.getCenterX() 
				+ ", " + ca.getCenterY() + "],[height = " + ca.getHeight() + "],[width = "
				+ ca.getWidth() + "],[Vx = " + startvx + "],[Vy = " + startvy + "]}.");
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		getBoundingArea().setRotation(behaviour);	//update rotation
		drawRotatedImage(gc, sprite, getBoundingArea());
	}

	/**
	 * Enemy fish should die if they hit the wall from the inside.
	 */
	@Override
	public void hitWall() {
		logger.log(LogLevel.TRACE, "EnemyFish collided with wall.");
		kill();
	}

@Override
	public void onCollide(ICollidable other) { }

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (other.getSize() > getSize() * settings.getDouble("FISH_EAT_THRESHOLD")) {
			return true;
		}
		return false;
	}

	@Override
	public void eat() {
		kill();
		AudioEngine.getInstance().playEffect("crunch");
	}

	@Override
	public double getSize() {
		return getBoundingArea().getSize();
	}

	@Override
	public IBehaviour getBehaviour() {
		return behaviour;
	}

	@Override
	public void setBehaviour(IBehaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

}
