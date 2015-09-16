package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for the ICollisionArea Interface.
 */
public abstract class TestICollisionArea {
	
	private ICollisionArea ca = getCollisionArea();
	
	/**
	 * @return
	 * 		an ICollisionArea object at position 0,0 with size 10x5.
	 */
	public abstract ICollisionArea getCollisionArea();
	
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test for intersection with an object with the same properties.
	 */
	@Test
	public void testBoxIntersectsSame() {
		ICollisionArea ca2 = getCollisionArea();
		assertTrue(ca.boxIntersects(ca2));
	}
	
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test for intersection at the left.
	 */
	@Test
	public void testBoxIntersectsLeft() {
		ICollisionArea ca2 = getCollisionArea();
		ca2.move(new Vec2d(-4, 0));
		assertTrue(ca.boxIntersects(ca2));
	}
	
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test for intersection at the right.
	 */
	@Test
	public void testBoxIntersectsRight() {
		ICollisionArea ca2 = getCollisionArea();
		ca2.move(new Vec2d(4, 0));
		assertTrue(ca.boxIntersects(ca2));
	}
	
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test for intersection at the top.
	 */
	@Test
	public void testBoxIntersectsTop() {
		ICollisionArea ca2 = getCollisionArea();
		ca2.move(new Vec2d(0, -2));
		assertTrue(ca.boxIntersects(ca2));
	}
	
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test for intersection at the bottom.
	 */
	@Test
	public void testBoxIntersectsBottom() {
		ICollisionArea ca2 = getCollisionArea();
		ca2.move(new Vec2d(0, -2));
		assertTrue(ca.boxIntersects(ca2));
	}
	/**
	 * Test for {@link ICollisionArea#boxIntersects(ICollisionArea).
	 * Test two boxes that don't collide.
	 */
	@Test
	public void testBoxIntersectsNot() {
		ICollisionArea ca2 = getCollisionArea();
		ca2.move(new Vec2d(10, 6));
		assertFalse(ca.boxIntersects(ca2));
	}
	
	/**
	 * Test for {@link ICollisionArea#getTopLeft().
	 */
	@Test
	public void testGetTopLeft() {
		assertEquals(new Vec2d(-5, -2.5), ca.getTopLeft());
	}
	
	/**
	 * Test for {@link ICollisionArea#getTopLeft().
	 * Check if the corner is correct after rotation.
	 */
	@Test
	public void testGetTopLeftRotated() {
		ca.setRotation(45);
		double a = Math.toRadians(45);
		Vec2d tl = new Vec2d(-5 * Math.cos(a) - 2.5 * Math.sin(a), 
				5 * Math.sin(a) - 2.5 * Math.cos(a));
		assertEquals(tl, ca.getTopLeft());
	}
	
	/**
	 * Test for {@link ICollisionArea#getTopRight().
	 */
	@Test
	public void testGetTopRight() {
		assertEquals(new Vec2d(5, -2.5), ca.getTopRight());
	}
	
	/**
	 * Test for {@link ICollisionArea#getTopRight().
	 * Check if the corner is correct after rotation.
	 */
	@Test
	public void testGetTopRightRotated() {
		ca.setRotation(45);
		double a = Math.toRadians(45);
		Vec2d tr = new Vec2d(5 * Math.cos(a) - 2.5 * Math.sin(a), 
				-5 * Math.sin(a) - 2.5 * Math.cos(a));
		assertEquals(tr, ca.getTopRight());
	}
	
	/**
	 * Test for {@link ICollisionArea#getBottomLeft().
	 */
	@Test
	public void testGetBottomLeft() {
		assertEquals(new Vec2d(-5, 2.5), ca.getBottomLeft());
	}
	
	/**
	 * Test for {@link ICollisionArea#getBottomLeft().
	 * Check if the corner is correct after rotation.
	 */
	@Test
	public void testGetBottomLeftRotated() {
		ca.setRotation(45);
		double a = Math.toRadians(45);
		Vec2d bl = new Vec2d(-5 * Math.cos(a) + 2.5 * Math.sin(a), 
				5 * Math.sin(a) + 2.5 * Math.cos(a));
		assertEquals(bl, ca.getBottomLeft());
	}
	
	/**
	 * Test for {@link ICollisionArea#getBottomRight().
	 */
	@Test
	public void testGetBottomRight() {
		assertEquals(new Vec2d(5, 2.5), ca.getBottomRight());
	}
	
	/**
	 * Test for {@link ICollisionArea#getBottomRight().
	 * Check if the corner is correct after rotation.
	 */
	@Test
	public void testGetBottomRightRotated() {
		ca.setRotation(45);
		double a = Math.toRadians(45);
		Vec2d br = new Vec2d(5 * Math.cos(a) + 2.5 * Math.sin(a), 
				-5 * Math.sin(a) + 2.5 * Math.cos(a));
		assertEquals(br, ca.getBottomRight());
	}
	
	/**
	 * Test for {@link ICollisionArea#getCenterX().
	 */
	@Test
	public void testGetCenterX() {
		assertEquals(0.0, ca.getCenterX(), 0.1E-12);
		ca.move(new Vec2d(1, 1));
		assertEquals(1.0, ca.getCenterX(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#getCenterY().
	 */
	@Test
	public void testGetCenterY() {
		assertEquals(0.0, ca.getCenterY(), 0.1E-12);
		ca.move(new Vec2d(1, 1));
		assertEquals(-1.0, ca.getCenterY(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#getWidth().
	 */
	@Test
	public void testGetWidth() {
		assertEquals(10.0, ca.getWidth(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#getHeight().
	 */
	@Test
	public void testGetHeight() {
		assertEquals(5.0, ca.getHeight(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#getSize().
	 */
	@Test
	public void testGetSize() {
		assertEquals(50.0, ca.getSize(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#increaseSize(double).
	 */
	@Test
	public void testIncreaseSize() {
		assertEquals(50.0, ca.getSize(), 0.1E-12);
		ca.increaseSize(100.0);
		assertEquals(150.0, ca.getSize(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#increaseSize(double).
	 * Check if the ratio is still the same.
	 */
	@Test
	public void testRatioIncreaseSize() {
		double ratio = ca.getWidth() / ca.getHeight();
		ca.increaseSize(100.0);
		assertEquals(ratio, ca.getWidth() / ca.getHeight(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#move(Vec2d).
	 */
	@Test
	public void testMove() {
		ca.move(new Vec2d(10.2, 5.6));

		assertEquals(10.2, ca.getCenterX(), 0.1E-12);
		assertEquals(-5.6, ca.getCenterY(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#setRotation(double).
	 */
	@Test
	public void testSetRotation() {
		assertEquals(0.0, ca.getRotation(), 0.1E-12);
		ca.setRotation(4.23);
		assertEquals(4.23, ca.getRotation(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#setRotation(double).
	 * Test if upside down boxes are eliminated.
	 */
	@Test
	public void testSetRotationMod() {
		assertEquals(0.0, ca.getRotation(), 0.1E-12);
		ca.setRotation(184.23);
		assertEquals(4.23, ca.getRotation(), 0.1E-12);
	}
	
	/**
	 * Test for {@link ICollisionArea#setSize().
	 */
	@Test
	public void testSetSize() {
		ca.setSize(12345.4);
		assertEquals(12345.4, ca.getSize(), 0.1E-8);		
	}
	
	/**
	 * Test for {@link ICollisionArea#setSize().
	 * Test if the width/height ratio stays the same
	 */
	@Test
	public void testSetSizeRatio() {
		double ratio = ca.getWidth() / ca.getHeight();
		ca.setSize(12345.4);
		assertEquals(ratio, ca.getWidth() / ca.getHeight(), 0.1E-8);		
	}

}
