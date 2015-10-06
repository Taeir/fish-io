package com.github.fishio.behaviours;

import com.github.fishio.EnemyFishFactory;
import com.github.fishio.Vec2d;

/**
 * A behaviour for entities that can swim in a certain direction,
 * but sometimes slow down in a certain axis, or speed up.
 */
public class RandomBehaviour implements IBehaviour {

	private double vx;
	private double vy;
	
	private double directionChangeChance;
	
	/**
	 * Creates a new Random behaviour.
	 * 
	 * @param startVx
	 * 		Starting speed on the x-direction.
	 * @param startVy
	 * 		Starting speed on the y-direction.
	 * @param directionChangeChance
	 * 		The chance on each tick of the speed to suddenly change.
	 */
	public RandomBehaviour(double startVx, double startVy, double directionChangeChance) {
		this.vx = startVx;
		this.vy = startVy;
		this.directionChangeChance = directionChangeChance;
	}
	
	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(vx, vy);
	}
	
	/** 
	 * Enemy fish sometimes change their movement speed.
	 * Only change one of their movement directions so the change looks more realistic.
	 */
	@Override
	public void preMove() {
		if (Math.random() < directionChangeChance) {
			//Only change one direction
			if (Math.random() <= 0.5) {
				vy = vy + vy * (Math.random() - 0.5);
			} else {
				vx = vx + vx * (Math.random() - 0.5);
			}
			limitSpeed();
		}
	}

	/**
	 * Limits the speed of the fish in both horizontal and vertical direction by
	 * calling the limiter methods for each seperate direction.
	 */
	public void limitSpeed() {
		limitVx();
		limitVy();
	}

	/**
	 * Limits the horizontal (x-directional) speed of the entity to a minimum and
	 * maximum value. These values are retrieved from the EnemyFishFactory class.
	 */
	public void limitVx() {
		if (vx > 0) {
			vx = Math.max(EnemyFishFactory.MIN_EFISH_SPEED, Math.min(vx, EnemyFishFactory.MAX_EFISH_SPEED));
		} else {
			vx = Math.min(-EnemyFishFactory.MIN_EFISH_SPEED, Math.max(vx, -EnemyFishFactory.MAX_EFISH_SPEED));
		}
	}
	
	/**
	 * Limits the vertical (y-directional) speed of the behaviour to a minimum and
	 * maximum value. These values are retrieved from the EnemyFishFactory class.
	 */
	public void limitVy() {
		if (vy > 0) {
			vy = Math.max(EnemyFishFactory.MIN_EFISH_SPEED, Math.min(vy, EnemyFishFactory.MAX_EFISH_SPEED));
		} else {
			vy = Math.min(-EnemyFishFactory.MIN_EFISH_SPEED, Math.max(vy, -EnemyFishFactory.MAX_EFISH_SPEED));
		}
	}
}
