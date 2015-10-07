package com.github.fishio.behaviours;

import com.github.fishio.Vec2d;

/**
 * A behaviour for entities that aren't moving.
 */
public class FrozenBehaviour implements IBehaviour {

	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(0, 0);
	}

	@Override
	public void preMove() { }

}
