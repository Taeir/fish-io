package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Class for testing the BoundingBox.
 */
public class TestBoundingBox extends TestICollisionArea {

	@Override
	public ICollisionArea getCollisionArea(Vec2d center, double width, double height) {
		return new BoundingBox(center, width, height);
	}
	
	/**
	 * Test for {@link BoundingBox#BoundingBox(double, double, double, double)}.
	 */
	@Test
	public void testConstructorDoubles() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(2.0, bb1.getCenterX(), 0.0);
		assertEquals(3.0, bb1.getCenterY(), 0.0);
		assertEquals(2.0, bb1.getWidth(), 0.0);
		assertEquals(2.0, bb1.getHeight(), 0.0);
	}
	
	/**
	 * Test for {@link BoundingBox#BoundingBox(double, double, double, double)},
	 * using the wrong order for min and max.
	 */
	@Test
	public void testConstructorDoubles2() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, -3.0, -4.0);
		
		assertEquals(-1.0, bb1.getCenterX(), 0.0);
		assertEquals(-1.0, bb1.getCenterY(), 0.0);
		assertEquals(4.0, bb1.getWidth(), 0.0);
		assertEquals(6.0, bb1.getHeight(), 0.0);
	}
	
	/**
	 * Test for {@link BoundingBox#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(bb1.hashCode(), bb2.hashCode());
	}
	
	/**
	 * Test for moving a bounding vox by vectors.
	 * {@link BoundingBox#move(Vec2d)}
	 */
	@Test
	public void testMoveVec2d() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		bb.move(new Vec2d(-5.3, 7.1));
		
		assertEquals(-4.3, bb.getTopLeft().x, 0.0000001D);
		assertEquals(-5.1, bb.getTopLeft().y, 0.0000001D);
		assertEquals(-2.3, bb.getBottomRight().x, 0.0000001D);
		assertEquals(-3.1, bb.getBottomRight().y, 0.0000001D);
	}
	
	/**
	 * Test for {@link BoundingBox#increaseSize(double)}.
	 */
	@Test
	public void testIncreaseSize2() {
		BoundingBox bb = new BoundingBox(1.0, 1.0, 3.0, 2.0);
		
		bb.increaseSize(10.0);
		
		assertEquals(0.0, bb.getTopLeft().x, 0000001D);
		assertEquals(0.0, bb.getTopLeft().y, 0000001D);
		assertEquals(4.0, bb.getBottomRight().x, 0000001D);
		assertEquals(3.0, bb.getBottomRight().y, 0000001D);
	}

	/**
	 * Test for {@link BoundingBox#equals(Object)} with null (false).
	 */
	@Test
	public void testEqualsObjectNull() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		assertNotEquals(null, bb);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with itself (true).
	 */
	@Test
	public void testEqualsObjectSelf() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		assertEquals(bb, bb);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an equal object (true).
	 */
	@Test
	public void testEqualsObjectSame() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal1() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(2.0, 2.0, 3.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal2() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 1.0, 3.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal3() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 2.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}

	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal4() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 5.0);
		
		assertNotEquals(bb1, bb2);
	}
}
