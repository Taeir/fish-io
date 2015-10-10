package com.github.fishio;

/**
 * Test class for the interfaced part of the collisionMas.
 */
public class TestCollisionMaskInterface extends TestICollisionArea {

	@Override
	public ICollisionArea getCollisionArea(Vec2d center, double width, double height) {
		return new CollisionMask(center, width, height, null, 1);
	}
}
