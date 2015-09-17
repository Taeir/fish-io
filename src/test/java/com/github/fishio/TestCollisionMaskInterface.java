package com.github.fishio;

/**
 * Test class for the interfaced part of the collisionMas.
 */
public class TestCollisionMaskInterface extends TestICollisionArea {

	@Override
	public ICollisionArea getCollisionArea() {
		return new CollisionMask(new Vec2d(0, 0), 10, 5, null, 1);
	}
}
