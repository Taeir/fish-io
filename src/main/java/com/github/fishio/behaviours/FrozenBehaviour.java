package com.github.fishio.behaviours;

import java.io.Serializable;

import com.github.fishio.Vec2d;

/**
 * A behaviour for entities that aren't moving.
 */
public class FrozenBehaviour implements IMoveBehaviour, Serializable {
	private static final long serialVersionUID = -5760123782325835897L;

	@Override
	public Vec2d getSpeedVector() {
		return new Vec2d(0, 0);
	}

	@Override
	public void preMove() { }

	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		//All frozen behaviours are born equal, and they are frozen solid, so they never change.
		//Therefore, all Frozen behaviour are equal. (QED)
		if (obj == null) {
			return false;
		}
		
		return this.getClass() == obj.getClass();
	}
	
	@Override
	public void updateTo(IMoveBehaviour behaviour) {
		if (!(behaviour instanceof FrozenBehaviour)) {
			throw new IllegalArgumentException("Cannot update behaviour to different type!");
		}
	}
}
