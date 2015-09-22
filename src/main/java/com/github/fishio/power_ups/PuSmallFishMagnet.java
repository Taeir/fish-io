package com.github.fishio.power_ups;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;

/**
 * A PowerUp with the effect that it makes all EnemyFishes
 * smaller than the PlayerFish move towards the PlayerFish.
 */
public class PuSmallFishMagnet extends DurationPowerUp {

	private static final int DURATION = 10;
	private PlayerFish pfish;
	
	/**
	 * Creates a new PowerUp of the Small Fish Magnet type.
	 * 
	 * @param ba
	 * 		The CollisonArea of the PowerUp.
	 * @param pfield
	 * 		The PlayingField in which this PowerUp is located.
	 */
	public PuSmallFishMagnet(ICollisionArea ba, PlayingField pfield) {
		super(ba, pfield);
	}

	@Override
	public int getDuration() {
		return DURATION;
	}

	@Override
	public void startEffect(PlayerFish pf) {
		this.pfish = pf;
	}

	/** 
	 * Setting the speedVector of every EnemyFish smaller
	 * than the PlayerFish, so that the EnemyFish will move
	 * towards the PlayerFish.
	 */
	@Override
	public void preTickEffect() {
		for (Entity e : getPField().getEntities()) {
			//Only look at EnemyFishes whose size is smaller than that of the PlayerFish
			if (!(e instanceof EnemyFish) 
					|| e.getBoundingArea().getSize() >= pfish.getBoundingArea().getSize()) {
				return;
			}
			
			EnemyFish efish = (EnemyFish) e;
			
			//If ef is still self controlling, stop that, because we want to control it.
			efish.setSelfControlling(false);
		
			//Calculating the speedVector to give the EnemyFish
			ICollisionArea pfishCa = pfish.getBoundingArea();
			Vec2d pfishLoc = new Vec2d(pfishCa.getCenterX(), pfishCa.getCenterY());
			
			ICollisionArea efishCa = efish.getBoundingArea();
			Vec2d efishLoc = new Vec2d(efishCa.getCenterX(), efishCa.getCenterY());
			
			Vec2d delta = new Vec2d(pfishLoc.x - efishLoc.x, pfishLoc.y - efishLoc.y);
			Vec2d speedVector = delta.normalize();
			
			//Setting the speedVector of the enemyFish.
			efish.setSpeedVector(speedVector);
		}
	}

	@Override
	public void postTickEffect() { }

	/** 
	 * Making all the EnemyFishes selfControllable again.
	 */
	@Override
	public void endEffect() {
		for (Entity e : getPField().getEntities()) {
			if (e instanceof EnemyFish) {
				((EnemyFish) e).setSelfControlling(true);
			}
		}
	}

}
