package com.github.fishio.power_ups;

import com.github.fishio.CollisionMask;
import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.IEatable;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.VerticalBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A PowerUp is an entity that, when colliding with a player fish,
 * executes a certain (positive) effect. 
 */
public abstract class PowerUp extends Entity implements IEatable {

	private PlayingField playingField;
	
	private final Log logger = Log.getLogger();
	
	private Image sprite;
	
	private IMoveBehaviour behaviour;
	
	private static final double DEFAULT_SPEED = 2;
	
	/**
	 * Creates a new PowerUp.
	 * 
	 * @param ba
	 *            The CollisionMask of the Power-Up
	 * @param pfield
	 *            The PlayingField this PowerUp is located in
	 * @param image
	 *            The sprite of this PowerUp
	 */
	public PowerUp(CollisionMask ba, PlayingField pfield, Image image) {
		super(ba);
		
		this.playingField = pfield;
		this.sprite = image;
		
		this.behaviour = new VerticalBehaviour(DEFAULT_SPEED);
	}
	
	/**
	 * @return
	 * 		The PlayingField this PowerUp is located in.
	 */
	public PlayingField getPField() {
		return playingField;
	}
	
	/**
	 * Executes the effect the power-up should do
	 * when a collision happens with a PlayerFish.
	 * 
	 * @param pfish
	 * 		The PlayerFish this PowerUp collided with
	 */
	public abstract void executeEffect(PlayerFish pfish);
	
	@Override
	public IMoveBehaviour getBehaviour() {
		return behaviour;
	}
	
	@Override
	public void setBehaviour(IMoveBehaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	/**
	 * @return
	 * 		A name for this PowerUp.
	 */
	public abstract String getName();
	
	@Override
	public void onCollide(ICollidable other) { 
		executeEffect((PlayerFish) other);
		logger.log(LogLevel.DEBUG, "Effect of \"" + getName() + "\" started");
	}

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (other instanceof PlayerFish) {
			return true;
		}
		return false;
	}

	@Override
	public void eat() {
		kill();
	}

	@Override
	public double getSize() {
		return 0; // A PowerUp shouldn't have a "size"
	}
	
	@Override
	public void render(GraphicsContext gc) {
		drawRotatedImage(gc, sprite, getBoundingArea());
	}
	
	@Override
	public boolean canMoveThroughWall() {
		return true;
	}
	
}
